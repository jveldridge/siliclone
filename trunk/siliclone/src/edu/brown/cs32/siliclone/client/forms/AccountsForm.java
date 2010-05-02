package edu.brown.cs32.siliclone.client.forms;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;



import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;


public class AccountsForm extends DynamicForm {
	//current groups + new group
	private LinkedHashMap<String, String> ownedGroups;
	private LinkedHashMap<String, String> availableGroups;
	private LinkedHashMap<String, String> groupsUsers;
	private LinkedHashMap<String, String> allUsers;
	public AccountsForm(){
		loadOwnedGroups();
		loadAllUsers();
		loadAllAvailableGroups();
		
		SectionItem ownedGroupsSection = new SectionItem();
		ownedGroupsSection.setDefaultValue("Owned Groups");
		ownedGroupsSection.setItemIds("selectGrp", "newGrp",
									"addUsr", "rmUsr");
		
		
		final SelectItem groupPick = new SelectItem("selectGrp");
		groupPick.setTitle("Select Group");
		groupPick.setValueMap(ownedGroups);
		
		
		//create group option
		ButtonItem createGroup = new ButtonItem( "newGrp");
		createGroup.setTitle("Create New Group");
		
		//add user to group
		final SelectItem addUser = new SelectItem("addUsr");
		addUser.setTitle("Add User to Group");
		addUser.setDisabled(true);
		
		
		//remove user from group
		final SelectItem removeUser = new SelectItem("rmUsr");
		removeUser.setTitle("Remove User from Group");
		removeUser.setDisabled(true);
		
		groupPick.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				loadGroupsUsers(groupPick.getDisplayValue());
				addUser.setDisabled(false);
				removeUser.setDisabled(false);
			}
		});
		
		addUser.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				final UserServiceAsync service = GWT.create(UserService.class); 
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
					public void onSuccess(Void result) {
						loadGroupsUsers(groupPick.getDisplayValue());
						SC.say("User added successfully");
					}
				};
				try {
					service.addToGroup(groupPick.getDisplayValue(), addUser.getDisplayValue(), callback);
				} catch (DataServiceException e) {
					e.printStackTrace();
				}
			}
		});
		
		removeUser.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				final UserServiceAsync service = GWT.create(UserService.class); 
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
					public void onSuccess(Void result) {
						loadGroupsUsers(groupPick.getDisplayValue());
						SC.say("User removed successfully");
					}
				};
				try {
					service.removeFromGroup(groupPick.getDisplayValue(), removeUser.getDisplayValue(), callback);
				} catch (DataServiceException e) {
					e.printStackTrace();
				}
			}
		});
		
		final StaticTextItem allGroups = new StaticTextItem("Available");
		
		
		createGroup.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				final UserServiceAsync service = GWT.create(UserService.class); 
				final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
					public void onSuccess(Void result) {
						loadOwnedGroups();
						loadAllAvailableGroups();
						String contents = "";
						for(String s : availableGroups.keySet()){
							contents += s + "\n";
						}
						allGroups.setTitle(contents);
						SC.say("Group created.");
					}
				};
				SC.askforValue("Create Group", "Enter Group Name:", new ValueCallback(){
					public void execute(String value){
						try {
							service.createGroup(value, callback);
						} catch (DataServiceException e) {
							e.printStackTrace();
						}							
					}
				});
			}
			
		});
		
		
		SectionItem memberGroupsSection = new SectionItem();
		memberGroupsSection.setDefaultValue("Available Groups");
		memberGroupsSection.setItemIds("Available");
		
		String contents = "";
		for(String s : availableGroups.keySet()){
			contents += s + "\n";
		}
		allGroups.setTitle(contents);
		
		
		setFields(ownedGroupsSection,
				groupPick, 
				createGroup, 
				addUser, 
				removeUser,
				memberGroupsSection,
				allGroups);
		
	}
	
	
	private void loadOwnedGroups(){
		ownedGroups = new LinkedHashMap<String, String>();
		
		final UserServiceAsync service = GWT.create(UserService.class); 
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<String> result) {
				for(String group : result){
					ownedGroups.put(group, group);
				}
			}
		};
		
		try {
			service.getOwnedGroups(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	
	}
	
	
	
	
	
	private void loadAllAvailableGroups(){
		availableGroups = new LinkedHashMap<String, String>();
		final UserServiceAsync service = GWT.create(UserService.class); 
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<String> result) {
				for(String group : result){
					availableGroups.put(group, group);
				}
			}
		};
		
		try {
			service.getAvailableGroups(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
	
	
	
	
	
	private void loadAllUsers(){
		allUsers = new LinkedHashMap<String, String>();
		final UserServiceAsync service = GWT.create(UserService.class); 
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<User> result) {
				for(User u : result){
					allUsers.put(u.getName(), u.getEmail());
				}
			}
		};
		
		try {
			service.getAllUsers(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
		
	}
	
	private void loadGroupsUsers(String group){
		groupsUsers = new LinkedHashMap<String, String>();
		final UserServiceAsync service = GWT.create(UserService.class); 
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<User> result) {
				for(User u : result){
					groupsUsers.put(u.getName(), u.getEmail());
				}
			}
		};
		
		try {
			service.getUsersWithAccessToGroup(group, callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
		
	}
}

