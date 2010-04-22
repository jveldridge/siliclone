package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class LoginScreen extends HLayout {

	public LoginScreen() {
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
			
			TextItem username = new TextItem("Username");
			username.setRequired(true);
			username.setSelectOnFocus(true);
			
			PasswordItem password = new PasswordItem("Password");
			password.setRequired(true);
			
			SubmitItem submit = new SubmitItem("Login!");
			submit.setAlign(Alignment.CENTER);
			
			loginForm.setFields(new FormItem[] {username, password, submit});
			
			loginForm.setAlign(Alignment.CENTER);
			this.addMember(loginForm);

			this.setAlign(Alignment.CENTER);
			this.setAlign(VerticalAlignment.CENTER);
		}
	}
	
}
