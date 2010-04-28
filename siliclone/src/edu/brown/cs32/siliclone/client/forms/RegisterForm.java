package edu.brown.cs32.siliclone.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.Siliclone;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class RegisterForm extends DynamicForm {
	
	public RegisterForm(final Siliclone main, final Window w){
		this.setAutoFocus(true);
		this.setSize("400px", "300px");
		
		//TODO look into "spanning" form components to make layout nicer
		this.setTitleOrientation(TitleOrientation.TOP);

		//username
		final TextItem username = new TextItem("uname", "Enter username");
		username.setRequired(true);
		username.setSelectOnFocus(true);
		username.setEndRow(true);
		
		//email-- validator to make sure it has the form of a valid email address
		final TextItem email = new TextItem("eaddr", "Enter email address");
		email.setRequired(true);
		email.setEndRow(true);
		
		RegExpValidator emailValidator = new RegExpValidator();
		emailValidator.setErrorMessage("Invalid email address");
		emailValidator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
		
		email.setValidators(emailValidator);

		//password-- validator to make sure that password fields match
		final PasswordItem password = new PasswordItem("pwd", "Enter password");
		password.setRequired(true);
		password.setEndRow(true);
		
		final PasswordItem password2 = new PasswordItem("pwdd", "Enter password again");
		password2.setRequired(true);
		password2.setEndRow(true);
		
		MatchesFieldValidator matchesValidator = new MatchesFieldValidator();
		matchesValidator.setOtherField("pwd");
		matchesValidator.setErrorMessage("Passwords do not match");
		password2.setValidators(matchesValidator);
		
		ButtonItem submit = new ButtonItem("register", "Register!");
		submit.setAlign(Alignment.CENTER);
		submit.addClickHandler(new ClickHandler() {
			private final UserServiceAsync service = GWT.create(UserService.class); 
			public void onClick(ClickEvent event) {
				if (RegisterForm.this.validate()) {
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
			}
		});
		
		setFields(new FormItem[] {username, email, password, password2, submit});
		
		setAlign(Alignment.CENTER);
	}


}
