package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void changePassword(String username, String password,
			AsyncCallback<Boolean> callback);

	void login(String username, String password, AsyncCallback<Boolean> callback);

	void register(String username, String email, AsyncCallback<Boolean> callback);

}
