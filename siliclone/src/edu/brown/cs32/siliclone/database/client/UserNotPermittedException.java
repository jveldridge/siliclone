package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;

public class UserNotPermittedException extends IOException {
	public UserNotPermittedException (){}
	
	public String getMessage(){
		return "You do not have permission to access the requested data.";
	}
}
