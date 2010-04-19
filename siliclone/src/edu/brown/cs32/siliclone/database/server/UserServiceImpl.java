package edu.brown.cs32.siliclone.database.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.UserService;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	public boolean changePassword(User u, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean login(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean register(User u) {
		// TODO Auto-generated method stub
		return false;
	}

}
