package edu.brown.cs32.siliclone.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.WorkspaceService;
import edu.brown.cs32.siliclone.database.client.WorkspaceServiceAsync;

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
		
		private Dialog _dialog;
		private ListGrid _sequenceGrid;
		private WorkspaceServiceAsync _service;
		private String _workspaceToLoad;
		
		public LoadClickHandler() {
			_dialog = new Dialog();
			_dialog.setSize("400px","300px");
			_dialog.setTitle("Load workspace...");
			
			_sequenceGrid = new ListGrid();
			_sequenceGrid.setSelectionType(SelectionStyle.SINGLE);
			_sequenceGrid.setShowAllRecords(true);
			_sequenceGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
				public void onSelectionChanged(SelectionEvent event) {
					_workspaceToLoad = _sequenceGrid.getSelection()[0].getAttribute("name");
				}
			});
			
			ListGridField name = new ListGridField("name", "Workspace Name");
			_sequenceGrid.setFields(name);
			
			Button okButton = new Button("Load");
			okButton.addClickHandler(new ClickHandler() {	
				public void onClick(ClickEvent event) {
					SC.say(_workspaceToLoad);
				}
			});
			
			_dialog.addItem(_sequenceGrid);
			_dialog.addItem(okButton);
								
			_service = GWT.create(WorkspaceService.class);
		}
		
		public void onClick(ClickEvent event) {
			
			
			AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
				public void onFailure(Throwable caught) {
					SC.say("failure");
					caught.printStackTrace();
				}

				public void onSuccess(List<String> result) {
					for (String w : result) {
						ListGridRecord rec = new ListGridRecord();
						rec.setAttribute("name", w);
						_sequenceGrid.addData(rec);
					}
					_dialog.show();
				}
				
			};
			_service.getAvailableWorkspaces(callback);
		}
	}
	
	/**
	 * ClickHandler for the "Save" workspace button.
	 */
	private class SaveClickHandler implements ClickHandler {
		private WorkspaceServiceAsync _service;
		private AsyncCallback<Boolean> _callback;
		
		public SaveClickHandler() {
			_service = GWT.create(WorkspaceService.class);
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
							//TODO catch an appropriate exception for when a workspace with the
							//	   passed name already exists, and display an appropriate message to the user
							e.printStackTrace();
						}
					}
				}
			};
			
			SC.askforValue("Save workspace...", "Enter a name for this workspace:", dialogCallback);
		}
	}
	
	/**
	 * ClickHandler for the "New" workspace button
	 */
	private class NewClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			_main.makeNewWorkspace();
		}
	}
}
