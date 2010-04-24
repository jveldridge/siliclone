package edu.brown.cs32.siliclone.client;


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.forms.LoginForm;
import edu.brown.cs32.siliclone.client.forms.RegisterForm;


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
		// TODO Auto-generated method stub
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
			
			this.setMembersMargin(20);
			this.setLayoutLeftMargin(20);
			this.setLayoutAlign(Alignment.CENTER);
			

			Button register = new Button("Register");
			register.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					final Window w = new Window();
					
					w.setTitle("Register");
					w.setIsModal(true);  
					w.setShowModalMask(true);  
					w.setShowCloseButton(true);
					
					w.addItem(new RegisterForm(_main, w));
					
					w.setAutoSize(true);
					w.setAutoCenter(true);
					w.show();
				}
			});

			
			
			this.addMember(new LayoutSpacer());
			this.addMember(new LoginForm(_main));
			this.addMember(register);
			
			this.addMember(new LayoutSpacer());
			
		}
	}
	
}
