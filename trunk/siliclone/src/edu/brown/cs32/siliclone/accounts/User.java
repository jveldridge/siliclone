package edu.brown.cs32.siliclone.accounts;

import java.io.Serializable;

/**
 * A user object represents the current client's account information. 
 * This object can be partially not initialized (fields left null).
 * Whenever a field is initialized, the object is responsible for contacting the
 * server to make sure that the requested value is valid before actually setting 
 * the field to that value.
 *
 */
public class User implements Serializable { //TODO: finish this class

	private String name; //unique, valid, username 
	private String email; //unique, valid, email (one account per email)
	private String password; //valid password 
	
	/**
	 * TODO serialization
	 */
	private static final long serialVersionUID = 1L;

	public User(){
	}
	public User(String name, String password){
		
	}
	public User(String name, String password, String email){
		
	}
	/**
	 * @return The name of the user account, null if not yet successfully initialized.
	 */
	public String getName(){
		return name;
	}
	public String getPassword(){
		return password;	
	}
	public String getEmail(){
		return email;
	}
	/**
	 * @param name The name to be set for this user. (not null)
	 * @return true if success, false if name invalid/ already in use
	 */
	public boolean setName(String name){
		return false;
	}
	public boolean setPassword(String password){
		return false;
	}
	public boolean setEmail(String email){
		return false;
	}
}
