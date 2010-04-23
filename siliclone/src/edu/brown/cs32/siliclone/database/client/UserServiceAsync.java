package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;

public interface UserServiceAsync {

	void changePassword(User u, String newPassword,
			AsyncCallback<User> callback);

	void login(User u, AsyncCallback<User> callback);

	void register(User u, AsyncCallback<User> callback);

	void remove(User u, AsyncCallback<User> callback);

	void createGroup(User u, String group, AsyncCallback<String> callback);

	void addToGroup(User u, String group, String userToAdd,
			AsyncCallback<String> callback);

	void getAvailableGroups(User u, AsyncCallback<List<String>> callback);

	void getOwnedGroups(User u, AsyncCallback<List<String>> callback);

	void getUsersWithAccessToGroup(String group,
			AsyncCallback<List<String>> callback);

	void removeFromGroup(User u, String group, String userToRemove,
			AsyncCallback<String> callback);
	
}
