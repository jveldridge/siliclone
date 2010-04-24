package edu.brown.cs32.siliclone.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.Siliclone;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class LoginForm extends DynamicForm {
	
	public LoginForm(final Siliclone main){
		setAutoFocus(true);
		
		final TextItem username = new TextItem("Username");
		username.setRequired(true);
		username.setSelectOnFocus(true);
		
		final PasswordItem password = new PasswordItem("Password");
		password.setRequired(true);
		
		ButtonItem submit = new ButtonItem("Login!");
		submit.setAlign(Alignment.CENTER);
		
		
		submit.addClickHandler(new ClickHandler() {
			private final UserServiceAsync service = GWT.create(UserService.class); 
			public void onClick(ClickEvent event) {
				User u = new User(username.getDisplayValue(), password.getDisplayValue());
				AsyncCallback<User> callback = new AsyncCallback<User>() {

					public void onFailure(Throwable caught) {
						SC.say("Error connecting.");
					}
					
					public void onSuccess(User result) {
						if (result.isValid()) {
							main.showMainView();
							
							//SC.say("success!");
						}
						else {
							SC.say("Bad username or password");
						}
					}
				};
				service.login(u, callback);
			}
		});
		
		
		
		password.addKeyPressHandler(new KeyPressHandler() {
			private final UserServiceAsync service = GWT.create(UserService.class); 
			public void onKeyPress(KeyPressEvent event) {
				if(!event.getKeyName().equals("Enter")){
					return;
				}
				User u = new User(username.getDisplayValue(), password.getDisplayValue());
				AsyncCallback<User> callback = new AsyncCallback<User>() {

					public void onFailure(Throwable caught) {
						SC.say("Error connecting.");
					}
					
					public void onSuccess(User result) {
						if (result.isValid()) {
							main.showMainView();
							
							//SC.say("success!");
						}
						else {
							SC.say("Bad username or password");
						}
					}
				};
				service.login(u, callback);
			}
		});
		
		
		
		
		
		setFields(new FormItem[] {username, password, submit});
		
		setAlign(Alignment.CENTER);
	}

}
