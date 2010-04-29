package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;

public interface UserServiceAsync {

	void changePassword(String newPassword,
			AsyncCallback<User> callback) throws IOException;

	void login(User u, AsyncCallback<User> callback) throws FailedConnectionException;

	void register(User u, AsyncCallback<User> callback) throws FailedConnectionException;

	void remove(User u, AsyncCallback<User> callback) throws IOException;

	void createGroup(String group, AsyncCallback<String> callback) throws IOException;

	void addToGroup(String group, String userToAdd,
			AsyncCallback<String> callback) throws IOException;

	void getAvailableGroups(AsyncCallback<List<String>> callback) throws IOException;

	void getOwnedGroups(AsyncCallback<List<String>> callback) throws IOException;

	void getUsersWithAccessToGroup(String group,
			AsyncCallback<List<User>> callback) throws IOException;

	void removeFromGroup(String group, String userToRemove,
			AsyncCallback<String> callback) throws IOException;
	
}
