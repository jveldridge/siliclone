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
	//private LinkedHashMap<String, String> ownedGroups;
	//private LinkedHashMap<String, String> groupsUsers;
	//private LinkedHashMap<String, String> allUsers;
	
	private final SelectItem groupPick = new SelectItem("selectGrp");
	private final ButtonItem createGroup = new ButtonItem( "newGrp");
	private final SelectItem addUser = new SelectItem("addUsr");
	private final SelectItem removeUser = new SelectItem("rmUsr");
	private final SectionItem memberGroupsSection = new SectionItem();
	private final StaticTextItem availableGroupsList = new StaticTextItem("Available");
	
	public AccountsForm(){
		loadAllUsers();
		loadOwnedGroups();
		loadAllAvailableGroups();
		
		SectionItem ownedGroupsSection = new SectionItem();
		ownedGroupsSection.setDefaultValue("Owned Groups");
		ownedGroupsSection.setItemIds("selectGrp", "newGrp",
									"addUsr", "rmUsr");
		
		
		memberGroupsSection.setDefaultValue("Available Groups");
		memberGroupsSection.setItemIds("Available");
		

		final StaticTextItem allGroups = new StaticTextItem("Available");
		
		
		groupPick.setTitle("Select Group");

		createGroup.setTitle("Create New Group");

		addUser.setTitle("Add User to Group");
		addUser.setDisabled(true);
		
		removeUser.setTitle("Remove User from Group");
		removeUser.setDisabled(true);
		
		
		groupPick.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				//AccountsForm.this.setDisabled(true);
				loadGroupsUsers(groupPick.getDisplayValue());
				addUser.setDisabled(false);
				removeUser.setDisabled(false);
				//AccountsForm.this.setDisabled(true);
			}
		});
		
		
		addUser.addChangedHandler(new ChangedHandler(){
			public void onChanged(ChangedEvent event) {
				//AccountsForm.this.setDisabled(true);
				final UserServiceAsync service = GWT.create(UserService.class); 
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
						//AccountsForm.this.setDisabled(true);
					}
					public void onSuccess(Void result) {
						loadGroupsUsers(groupPick.getDisplayValue());
						SC.say("User added successfully");
						//AccountsForm.this.setDisabled(false);
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
				//AccountsForm.this.setDisabled(true);
				final UserServiceAsync service = GWT.create(UserService.class);
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
						//ccountsForm.this.setDisabled(false);
					}
					public void onSuccess(Void result) {
						loadGroupsUsers(groupPick.getDisplayValue());
						SC.say("User removed successfully");
						//AccountsForm.this.setDisabled(false);
					}
				};
				try {
					service.removeFromGroup(groupPick.getDisplayValue(), removeUser.getDisplayValue(), callback);
				} catch (DataServiceException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		createGroup.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				//AccountsForm.this.setDisabled(true);
				final UserServiceAsync service = GWT.create(UserService.class); 
				final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
						//AccountsForm.this.setDisabled(false);
					}
					public void onSuccess(Void result) {
						loadOwnedGroups();
						loadAllAvailableGroups();
						SC.say("Group created.");
						//AccountsForm.this.setDisabled(false);
					}
				};
				SC.askforValue("Create Group", "Enter Group Name:", new ValueCallback(){
					public void execute(String value){
						if(value == null){
							AccountsForm.this.setDisabled(false);
							return;
						}
						try {
							service.createGroup(value, callback);
						} catch (DataServiceException e) {
							e.printStackTrace();
						}							
					}
				});
			}
			
		});
		setFields(ownedGroupsSection,
				groupPick, 
				createGroup, 
				addUser, 
				removeUser,
				memberGroupsSection,
				availableGroupsList);
	}
	
	
	
	
	
	
	
	
	private void loadOwnedGroups(){
		final LinkedHashMap<String, String> ownedGroups = new LinkedHashMap<String, String>();
		
		final UserServiceAsync service = GWT.create(UserService.class); 
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<String> result) {
				for(String group : result){
					ownedGroups.put(group, group);
				}
				groupPick.setValueMap(ownedGroups);
			}
		};
		
		try {
			service.getOwnedGroups(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	
	}
	
	
	
	
	
	private void loadAllAvailableGroups(){
		final UserServiceAsync service = GWT.create(UserService.class); 
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<String> result) {
				String content = "Available groups: ";
				for(String group : result){
					content += (group + "     \n");
				}
				availableGroupsList.setValue(content);
			}
		};
		
		try {
			service.getAvailableGroups(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
	
	
	
	
	
	private void loadAllUsers(){
		final LinkedHashMap<String, String> allUsers = new LinkedHashMap<String, String>();
		final UserServiceAsync service = GWT.create(UserService.class); 
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<User> result) {
				for(User u : result){
					allUsers.put(u.getEmail(), u.getName());
				}
				addUser.setValueMap(allUsers);
			}
		};
		try {
			service.getAllUsers(callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
	}
	
	
	
	
	private void loadGroupsUsers(String group){
		final LinkedHashMap<String, String> groupsUsers = new LinkedHashMap<String, String>();
		final UserServiceAsync service = GWT.create(UserService.class); 
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
			
			public void onSuccess(List<User> result) {
				for(User u : result){
					groupsUsers.put(u.getEmail(), u.getName());
				}
				removeUser.setValueMap(groupsUsers);
			}
		};
		try {
			service.getUsersWithAccessToGroup(group, callback);
		} catch (DataServiceException e) { e.printStackTrace(); }
		
	}
}

