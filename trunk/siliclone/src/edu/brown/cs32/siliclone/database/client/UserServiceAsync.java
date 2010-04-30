package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;

public interface UserServiceAsync {

	void changePassword(String newPassword,
			AsyncCallback<User> callback) throws DataServiceException;

	void login(User u, AsyncCallback<User> callback) throws DataServiceException;

	void register(User u, AsyncCallback<User> callback) throws DataServiceException;

	void remove(User u, AsyncCallback<Void> callback) throws DataServiceException;

	void createGroup(String group, AsyncCallback<Void> callback) throws DataServiceException;

	void addToGroup(String group, String userToAdd,
			AsyncCallback<Void> callback) throws DataServiceException;

	void getAvailableGroups(AsyncCallback<List<String>> callback) throws DataServiceException;

	void getOwnedGroups(AsyncCallback<List<String>> callback) throws DataServiceException;

	void getUsersWithAccessToGroup(String group,
			AsyncCallback<List<User>> callback) throws DataServiceException;

	void removeFromGroup(String group, String userToRemove,
			AsyncCallback<Void> callback) throws DataServiceException;
	
}
