package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;

public interface UserServiceAsync {

	void changePassword(User u, String newPassword,
			AsyncCallback<User> callback);

	void login(User u, AsyncCallback<User> callback);

	void register(User u, AsyncCallback<User> callback);
	
}
