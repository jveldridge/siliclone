package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataService;
import edu.brown.cs32.siliclone.database.client.DataServiceAsync;

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
		private DataServiceAsync _service;
		private AsyncCallback<Boolean> _callback;
		
		public SaveClickHandler() {
			_service = GWT.create(DataService.class);
			_callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					SC.say("failure");
					caught.printStackTrace();
				}

				public void onSuccess(Boolean result) {
					SC.say("Workspace Saved");
				}
			};
		}
		
		public void onClick(ClickEvent event) {
			ValueCallback dialogCallback = new ValueCallback() {			
				public void execute(String value) {
					if (value != null) {
						Workspace w = _main.getCurrentWorkspace();
						try {
							_service.saveWorkspace(w, w.getName(), _callback);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			
			SC.askforValue("Save workspace...", "Enter a name for this workspace:", dialogCallback);
		}
	}
	
	private class NewClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			_main.makeNewWorkspace();
		}
	}
}
