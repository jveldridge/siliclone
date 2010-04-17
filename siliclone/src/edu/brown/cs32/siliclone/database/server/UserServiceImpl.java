package edu.brown.cs32.siliclone.database.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.database.client.UserService;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	@Override
	public boolean changePassword(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean register(String username, String email) {
		// TODO Auto-generated method stub
		return false;
	}

}
