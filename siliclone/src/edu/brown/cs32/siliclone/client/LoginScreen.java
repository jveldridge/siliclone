package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
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

/**
 * This class is responsible for displaying the main
 * login screen that greets users when they navigate to
 * the home page of the web application.
 * 
 * It allows users to login, register for an account,
 * or read the Siliclone User Guide.
 */
public class LoginScreen extends HLayout {

	private Siliclone _main;
	
	public LoginScreen(Siliclone main) {
		_main = main;
		
		this.setOverflow(Overflow.HIDDEN);
		this.setHeight100();
		this.setWidth100();
		//this.setMembersMargin(20);
		
		InfoPanel iP = new InfoPanel();
		iP.setHeight100();
		iP.setWidth("70%");
		
		LoginPanel lP = new LoginPanel();
		lP.setHeight100();
		lP.setWidth("30%");
		
		
		this.addMember(iP);
		this.addMember(lP);
	}
	
	private class InfoPanel extends HLayout {
		// TODO Auto-generated method stub
		public InfoPanel() {
			this.setOverflow(Overflow.HIDDEN);
			this.setBackgroundImage("T7RNAP.png");
			this.setBackgroundPosition("center");
			
			Window introduction = new Window();
			introduction.setTitle("Welcome to Siliclone");
			introduction.setShowHeader(false);
		//	introduction.setCanDragReposition(true);
		//	introduction.setCanDragResize(true);
			introduction.setCanDrag(false);
			introduction.setCanDragReposition(false);
			introduction.setWidth(400);
			introduction.setHeight(200);
			introduction.setAutoCenter(true);
			Label titleLabel = new Label("<h3>Siliclone is an in-silico molecular biology cloning " +
					"simulator and productivity tool.  Using Siliclone, you can rapidly develop cloning strategies" +
					" in workspaces by linking together logical operators. You can then share these workspaces " +
					"with other users.</h3>" +
					"<h2>To get started using Siliclone, login or register for an account!</h2>" +
					"<p><a href='UserGuide.pdf'>User Guide</a></p>");
			titleLabel.setBackgroundColor("white");
			titleLabel.setAlign(Alignment.CENTER);
			introduction.addItem(titleLabel);
			introduction.setShowHeader(true);
			this.addChild(introduction);
			
			
			
		}
	}
	
	private class LoginPanel extends VLayout {
		public LoginPanel() {
			this.setBackgroundColor("#CCFFFF");
			
			this.setMembersMargin(20);
			this.setLayoutLeftMargin(20);
			this.setLayoutAlign(Alignment.CENTER);
			

			//Button register = new Button("Register");
			
			Img reg = new Img("register.jpg", 90, 30);
			reg.addClickHandler(new ClickHandler(){
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
			
			HLayout buttonPanel = new HLayout();
			buttonPanel.addMember(new LayoutSpacer());
			buttonPanel.addMember(reg);
			buttonPanel.addMember(new LayoutSpacer());
			
			this.addMember(new LayoutSpacer());
			this.addMember(new LoginForm(_main));
			this.addMember(buttonPanel);
			this.setAlign(Alignment.CENTER);
			
			this.addMember(new LayoutSpacer());
			
		}
	}
	
}
