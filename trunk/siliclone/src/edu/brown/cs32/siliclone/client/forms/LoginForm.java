package edu.brown.cs32.siliclone.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
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
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class LoginForm extends DynamicForm {
	
	private Siliclone _main;
	
	public LoginForm(final Siliclone main) {
		_main = main;
		setAutoFocus(true);
		
		final TextItem username = new TextItem("Username");
		username.setRequired(true);
		username.setSelectOnFocus(true);
		
		final PasswordItem password = new PasswordItem("Password");
		password.setRequired(true);
		
		ButtonItem submit = new ButtonItem("Login!");
		submit.setAlign(Alignment.CENTER);
		
		
		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				User u = new User(username.getDisplayValue(), password.getDisplayValue());
				LoginForm.this.login(u);
			}
		});
		
		password.addKeyPressHandler(new KeyPressHandler() {	
			public void onKeyPress(KeyPressEvent event) {
				if(!event.getKeyName().equals("Enter")){
					return;
				}
				
				User u = new User(username.getDisplayValue(), password.getDisplayValue());
				LoginForm.this.login(u);
			}
		});
		
		
		setFields(new FormItem[] {username, password, submit});
		
		setAlign(Alignment.CENTER);
	}
	
	private void login(User u) {
		final UserServiceAsync service = GWT.create(UserService.class); 
		
		AsyncCallback<User> callback = new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(User result) {
				_main.showMainView();
			}
		};
	
		/*try {
			service.login(u, callback);
		} catch (DataServiceException e) {
			SC.say(e.getMessage());
		}*/
		_main.showMainView();
	}

}
