package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;

public class NoSuchWorkspaceException extends IOException {
	public NoSuchWorkspaceException(){}
	
	public String getMessage(){
		return "The requested workspace was not found in the database.";
	}
}
