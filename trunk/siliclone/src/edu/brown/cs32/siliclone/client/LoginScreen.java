package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;

public class LoginScreen extends HLayout {

	private Siliclone _main;
	
	public LoginScreen(Siliclone main) {
		_main = main;
		
		this.setHeight100();
		this.setWidth100();
		this.setMembersMargin(20);
		
		InfoPanel iP = new InfoPanel();
		iP.setHeight100();
		iP.setWidth("65%");
		
		LoginPanel lP = new LoginPanel();
		lP.setHeight100();
		lP.setWidth("35%");
		
		this.addMember(iP);
		this.addMember(lP);
	}
	
	private class InfoPanel extends HLayout {
		public InfoPanel() {
			Img dna = new Img("dna.gif");
			dna.setHeight100();
			dna.setWidth(250);
			this.addMember(dna);
			
			Label silicloneLabel = new Label("<html><h1><b>siliclone</b></h1></html>" +
					"text..text...text...text..text...text...text..text...text...text..text...text...\n" +
					"text..text...text...text..text...text...text..text...text...text..text...text...");
			silicloneLabel.setAlign(Alignment.CENTER);
			this.addMember(silicloneLabel);
		}
	}
	
	private class LoginPanel extends VLayout {
		public LoginPanel() {
			this.setBackgroundColor("#CCFFFF");
			DynamicForm loginForm = new DynamicForm();
			loginForm.setAutoFocus(true);
			
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
							caught.printStackTrace();
						}
						
						public void onSuccess(User result) {
							if (result.isValid()) {
								_main.showMainView();
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
			
			loginForm.setFields(new FormItem[] {username, password, submit});
			
			loginForm.setAlign(Alignment.CENTER);
			this.addMember(loginForm);

			this.setAlign(Alignment.CENTER);
			this.setAlign(VerticalAlignment.CENTER);
		}
	}
	
}
