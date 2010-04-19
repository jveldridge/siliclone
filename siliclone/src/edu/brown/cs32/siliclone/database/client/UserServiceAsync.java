package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;

public interface UserServiceAsync {

	void changePassword(User u, String newPassword,
			AsyncCallback<Boolean> callback);

	void login(User u, AsyncCallback<Boolean> callback);

	void register(User u, AsyncCallback<Boolean> callback);

}
