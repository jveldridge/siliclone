package edu.brown.cs32.siliclone.client.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;
import edu.brown.cs32.siliclone.database.client.WorkspaceService;
import edu.brown.cs32.siliclone.database.client.WorkspaceServiceAsync;

public class WorkspaceForm extends DynamicForm{

	private final SelectItem workspacePick = new SelectItem("select");
	private final SelectItem addUser = new SelectItem("addU");
	private final SelectItem removeUser = new SelectItem("removeU");
	private final SelectItem addGroup = new SelectItem("addG");
	private final SelectItem removeGroup = new SelectItem("removeG");
	private final SectionItem ownedSection = new SectionItem();
	private final SectionItem availableSection = new SectionItem();
	private final StaticTextItem availableWorkspacesList = new StaticTextItem("available");

	private final WorkspaceServiceAsync workspaceService = GWT.create(WorkspaceService.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);

	public WorkspaceForm(){
		loadSelect();
		loadAddU();
		loadAddG();
		loadAvailable();

		addUser.setDisabled(true);
		removeUser.setDisabled(true);
		addGroup.setDisabled(true);
		removeGroup.setDisabled(true);
		
		ownedSection.setValue("Owned workspaces:");
		ownedSection.setItemIds("select", "addU", "removeU", "addG", "removeG");
		availableSection.setValue("Available workspaces:");
		availableSection.setItemIds("available");
	
		
		setFields(	ownedSection,
					workspacePick,
					addUser,
					removeUser,
					addGroup,
					removeGroup,
					availableSection,
					availableWorkspacesList);
		
		

		workspacePick.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				loadRemoveU(workspacePick.getDisplayValue());
				loadRemoveG(workspacePick.getDisplayValue());
				addUser.clearValue();
				addGroup.clearValue();
				addUser.setDisabled(false);
				removeUser.setDisabled(false);
				addGroup.setDisabled(false);
				removeGroup.setDisabled(false);
			}
		});	

		addUser.addChangedHandler(new ChangedHandler(){
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(Void result) {
					loadRemoveU(workspacePick.getDisplayValue());
					addUser.clearValue();
					SC.say("Added user permission to group.");
				}
			};
			public void onChanged(ChangedEvent event) {
				workspaceService.permitUser(workspacePick.getDisplayValue(),
						addUser.getDisplayValue(), callback);
			}
		});
		
		removeUser.addChangedHandler(new ChangedHandler(){
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(Void result) {
					loadRemoveU(workspacePick.getDisplayValue());
					SC.say("Removed user permission to group.");
				}
			};
			public void onChanged(ChangedEvent event) {
				workspaceService.disallowUser(workspacePick.getDisplayValue(),
						removeUser.getDisplayValue(), callback);
			}
		});
		addGroup.addChangedHandler(new ChangedHandler(){
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(Void result) {
					loadRemoveG(workspacePick.getDisplayValue());
					addGroup.clearValue();
					SC.say("Added group permission to group.");
				}
			};
			public void onChanged(ChangedEvent event) {
				workspaceService.permitGroup(workspacePick.getDisplayValue(),
						addGroup.getDisplayValue(), callback);
			}
		});
		
		removeGroup.addChangedHandler(new ChangedHandler(){
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(Void result) {
					loadRemoveG(workspacePick.getDisplayValue());
					SC.say("Removed group permission to group.");
				}
			};
			public void onChanged(ChangedEvent event) {
				workspaceService.disallowGroup(workspacePick.getDisplayValue(),
						removeGroup.getDisplayValue(), callback);
			}
		});
	}
	
	
	
	private void loadSelect(){
		workspacePick.setTitle("Select a workspace");
		AsyncCallback<List<String>> callback = new AsyncCallback<List <String>>(){
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<String> result) {
				ArrayList<String> names = new ArrayList<String>();
				for(String name : result){
					if(name != null){
						names.add(name);
					}
				}
				workspacePick.setValueMap(names.toArray(new String[0]));
			}
		};
		workspaceService.getOwnedWorkspaces(callback);
	}

	private void loadAddU(){
		addUser.setTitle("Give user permission:");
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<User> result) {
				ArrayList<String> users = new ArrayList<String>();
				for(User u : result){
					users.add(u.getName());
				}
				addUser.setValueMap(users.toArray(new String[0]));
			}
		};
		try {
			userService.getAllUsers(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
	
	
	
	private void loadRemoveU(String workspace){
		removeUser.setTitle("Remove user permission.");
		removeUser.clearValue();
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<User> result) {
				ArrayList<String> users = new ArrayList<String>();
				for(User u : result){
					users.add(u.getName());
				}
				removeUser.setValueMap(users.toArray(new String[0]));
			}
		};
		workspaceService.getPermittedUsers(workspace, callback);
	}
	
	
	
	
	
	
	private void loadAddG(){
		addGroup.setTitle("Give group permission:");
		final LinkedHashMap<String, String> allGroups = new LinkedHashMap<String, String>();
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<String> result) {
				ArrayList<String> groups = new ArrayList<String>();
				for(String g : result){
					groups.add(g);
				}
				addGroup.setValueMap(groups.toArray(new String[0]));
			}
		};
		try {
			userService.getOwnedGroups(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
	
	
	
	
	
	private void loadRemoveG(String workspace){
		removeGroup.clearValue();
		removeGroup.setTitle("Remove group permission:");
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<String> result) {
				ArrayList<String> groups = new ArrayList<String>();
				for(String g : result){
					groups.add(g);
				}
				removeGroup.setValueMap(groups.toArray(new String[0]));
			}
		};
		workspaceService.getPermittedGroups(workspace, callback);
	}
	
	
	
	
	
	private void loadAvailable(){
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			public void onSuccess(List<String> result) {
				String title = " Available Workspaces : ";
				for(String w : result){
					title += " \n    " + w;
					System.out.println(w);
				}
				availableWorkspacesList.setTitle(title);
			}
		};
		try {
			workspaceService.getAvailableWorkspaces(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
}
