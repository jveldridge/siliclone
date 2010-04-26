package edu.brown.cs32.siliclone.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

//TODO serious work here
public class TopMenu extends ToolStrip {
	
	private Siliclone _main;
	
	public TopMenu(final Siliclone main){
        _main = main;
		Canvas logoCanvas = new Canvas();
        Img logo = new Img("logo.gif", 100, 38);
        logoCanvas.addChild(logo);
        this.addMember(logoCanvas);		
		
		ToolStripButton newButton = new ToolStripButton();
        newButton.setTitle("New");
        newButton.setWidth("100px");
        newButton.addClickHandler(new NewClickHandler());
        
        ToolStripButton loadButton = new ToolStripButton();
        loadButton.setTitle("Load");
        loadButton.setWidth("100px");
        loadButton.addClickHandler(new LoadClickHandler());
        
        ToolStripButton saveButton = new ToolStripButton();
        saveButton.setTitle("Save");
        saveButton.setWidth("100px");
        saveButton.addClickHandler(new SaveClickHandler());
        
        this.addButton(newButton); 
        this.addButton(loadButton); 
        this.addButton(saveButton);
        
        this.addSeparator();
        
        ToolStripButton settingsButton = new ToolStripButton();
        settingsButton.setTitle("Settings");
        ToolStripButton logoutButton = new ToolStripButton();
        logoutButton.setTitle("Logout");
        logoutButton.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				main.showLoginScreen();
			}
		});
        
        
        this.addButton(settingsButton); 
        this.addButton(logoutButton);
              
        this.setAlign(Alignment.CENTER);  
        this.setOverflow(Overflow.HIDDEN);  
        this.setHeight("38px");  
        this.setWidth100();
        this.setShowResizeBar(false);
        
	}
	
	private class LoadClickHandler implements ClickHandler {
		
		private Dialog openDialog;
		
		public LoadClickHandler() {
			openDialog = new Dialog();
		}
		
		public void onClick(ClickEvent event) {
			openDialog.show();
		}
	}
	
	private class SaveClickHandler implements ClickHandler {
		
		private Dialog saveDialog;
		
		public SaveClickHandler() {
			saveDialog = new Dialog();
		}
		
		public void onClick(ClickEvent event) {
			saveDialog.show();
		}
	}
	
	private class NewClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			_main.makeNewWorkspace();
		}
	}
}
