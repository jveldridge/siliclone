package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;

public class FailedConnectionException extends IOException {
	public FailedConnectionException(){}
	
	public String getMessage(){
		return "Unable to connect to database.";
	}
}
