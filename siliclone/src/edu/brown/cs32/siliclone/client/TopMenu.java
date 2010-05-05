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
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import edu.brown.cs32.siliclone.client.forms.GroupsForm;
import edu.brown.cs32.siliclone.client.forms.UserForm;
import edu.brown.cs32.siliclone.client.forms.WorkspaceForm;
import edu.brown.cs32.siliclone.client.workspace.BasicWorkspace;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;
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
        settingsButton.addClickHandler(new SettingsClickHandler());
        ToolStripButton logoutButton = new ToolStripButton();
        logoutButton.setTitle("Logout");
        logoutButton.addClickHandler(new ClickHandler() {	
        	private UserServiceAsync service = GWT.create(UserService.class);
        	private AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
					main.showLoginScreen();
				}
				public void onSuccess(Void result) {
					main.showLoginScreen();
				}
        		
        	};
			public void onClick(ClickEvent event) {
				service.logout(callback);
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
					if (_sequenceGrid.getSelection().length > 0) {
						_workspaceToLoad = _sequenceGrid.getSelection()[0].getAttribute("name");
					}
				}
			});
			
			ListGridField name = new ListGridField("name", "Workspace Name");
			_sequenceGrid.setFields(name);
			
			Button okButton = new Button("Load");
			okButton.addClickHandler(new ClickHandler() {
				AsyncCallback<Workspace> callback = new AsyncCallback<Workspace>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}

					public void onSuccess(Workspace result) {
						_main.addWorkspace(result);
					}
				};
				
				public void onClick(ClickEvent event) {
					try {
						_service.findWorkspace(_workspaceToLoad, callback);
					} catch (DataServiceException e) {}
					
					_dialog.hide();
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
					ListGridRecord[] data = new ListGridRecord[result.size()];
					for (int i = 0; i < result.size(); i++) {
						data[i] = new ListGridRecord();
						data[i].setAttribute("name", result.get(i));
					}
					_sequenceGrid.setData(data);
					_dialog.show();
				}
				
			};
			try{
				_service.getAvailableWorkspaces(callback);
			}catch (DataServiceException e){
				e.printStackTrace();
				SC.say(e.getMessage());
			}
		}
	}
	
	/**
	 * ClickHandler for the "Save" workspace button.
	 */
	private class SaveClickHandler implements ClickHandler {
		private WorkspaceServiceAsync _service;
		private AsyncCallback<Void> _callback;
		
		public SaveClickHandler() {
			_service = GWT.create(WorkspaceService.class);
			_callback = new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
					caught.printStackTrace();
				}

				public void onSuccess(Void result) {
					SC.say("Workspace Saved");
				}
			};
		}
		
		public void onClick(ClickEvent event) {
			ValueCallback dialogCallback = new ValueCallback() {	
				public void execute(String value) {
					if (value != null) {
						Workspace w = _main.getCurrentWorkspace();
						w.setName(value);
						try{
							_service.saveWorkspace(w, w.getName(), _callback);
							_main.changeWorkspaceName(w, w.getName());
						}catch (DataServiceException e) {
							e.printStackTrace();
							SC.say(e.getMessage());
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
		private int counter = 1;
		public void onClick(ClickEvent event) {
			_main.addWorkspace(new BasicWorkspace("New Workspace (" + counter + ")"));
			counter++;
		}
	}
	
	
	private class SettingsClickHandler implements ClickHandler{
		public void onClick(ClickEvent e){
			final Window w = new Window();
			TabSet t = new TabSet();
			w.setSize("400px", "300px");
			
			
			Tab t1 = new Tab();
			t1.setTitle("Group Settings");
			t1.setPane(new GroupsForm());
			
			Tab t2 = new Tab();
			t2.setTitle("User Settings");
			t2.setPane(new UserForm(_main));
			
			Tab t3 = new Tab();
			t3.setTitle("Workspace Permissions");
			t3.setPane(new WorkspaceForm());
			
			t.addTab(t1);
			t.addTab(t2);
			t.addTab(t3);
			
			w.setTitle("Settings");
			w.setIsModal(true);  
			w.setShowModalMask(true);  
			w.setShowCloseButton(true);
			
			w.addItem(t);
			
			//w.setAutoSize(true);
			w.setAutoCenter(true);
			w.show();
		}
	}
}
