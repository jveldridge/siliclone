package edu.brown.cs32.siliclone.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.Siliclone;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class RegisterForm extends DynamicForm {
	
	public RegisterForm(final Siliclone main, final Window w){

		setAutoFocus(true);

		final TextItem username = new TextItem("uname", "Enter username");
		username.setRequired(true);
		username.setSelectOnFocus(true);
		
		final TextItem email = new TextItem("eaddr", "Enter email address");
		email.setRequired(true);

		final PasswordItem password = new PasswordItem("pwd", "Enter password");
		password.setRequired(true);
		
		final PasswordItem password2 = new PasswordItem("pwdd", "Enter password again");
		password2.setRequired(true);
		
		ButtonItem submit = new ButtonItem("register", "Register!");
		submit.setAlign(Alignment.CENTER);
		submit.addClickHandler(new ClickHandler() {
			private final UserServiceAsync service = GWT.create(UserService.class); 
			public void onClick(ClickEvent event) {
				if(!password.getDisplayValue().equals(password2.getDisplayValue())){
					SC.say("Passwords entered are not the same.");
					return;
				}
				
				User u = new User(username.getDisplayValue(), password.getDisplayValue(), 
						email.getDisplayValue());
				AsyncCallback<User> callback = new AsyncCallback<User>() {

					public void onFailure(Throwable caught) {
						SC.say("Connection error");
					}
					
					public void onSuccess(User result) {
						if (result.isValid()) {
							w.hide();
							main.showMainView();
						}
						else {
							SC.say("User already exists.");
						}
					}
				};
				service.register(u, callback);
			}
		});
		
		setFields(new FormItem[] {username, email, password, password2, submit});
		
		setAlign(Alignment.CENTER);
	}


}
