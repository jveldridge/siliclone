package edu.brown.cs32.siliclone.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.Siliclone;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class UserForm extends DynamicForm {
	private final StaticTextItem name = new StaticTextItem();
	private final StaticTextItem email = new StaticTextItem();
	private final PasswordItem changePassword = new PasswordItem();
	private final PasswordItem changePassword2 = new PasswordItem();
	private final ButtonItem submitChange = new ButtonItem();
	private final ButtonItem deleteAccount = new ButtonItem();
	
	private Siliclone main;

	private User loggedInUser;

	private final UserServiceAsync service = GWT.create(UserService.class); 

	public UserForm(Siliclone main){
		this.main = main;
		loadName();
		loadChangePassword();
		loadDelete();
		
		setFields(	name,
					email,
					changePassword,
					changePassword2,
					submitChange,
					deleteAccount);
		
	}
	
	private void loadName(){
		name.setTitle("User name");
		email.setTitle("Email");
		AsyncCallback<User> callback = new AsyncCallback<User>(){
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}

			public void onSuccess(User result) {
				loggedInUser = result;
				
				name.setValue(result.getName());
				email.setValue(result.getEmail());
			}
		};
		service.getLoggedIn(callback);
	}
	
	
	
	
	
	private void loadChangePassword(){
		final AsyncCallback<User> callback = new AsyncCallback<User>(){
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(User result) {
				SC.say("Password changed.");
			}
			
		};
		
		changePassword.setTitle("Change password");
		changePassword2.setTitle("Enter new password again:");
		submitChange.setTitle("Submit change");
		submitChange.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event) {
				if(changePassword.getDisplayValue() == null ||
						!changePassword.getDisplayValue().equals(changePassword2.getDisplayValue())){
					SC.say("Please re-enter the same password twice.");
				}else {
					try {
						service.changePassword(changePassword.getDisplayValue(), callback);
					} catch (DataServiceException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	

	private void loadDelete(){
		final AsyncCallback<Void> callback = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(Void result) {
				SC.say("User account deleted.");
				main.logout();
			}
		};
		final BooleanCallback boolCallback = new BooleanCallback(){
			public void execute(Boolean value) {
				if(value){
					try {
						service.remove(loggedInUser, callback);
					} catch (DataServiceException e) {
						e.printStackTrace();
					}
				}
			}
		};
		deleteAccount.setTitle("Delete account");
		deleteAccount.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete your account forever? " +
						"Your workspaces and sequences will not be deleted.", boolCallback);
			}
		});
	}
}
