package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService {
	boolean login(String username, String password);
	boolean changePassword(String username, String password);
	boolean register(String username, String email);
}
