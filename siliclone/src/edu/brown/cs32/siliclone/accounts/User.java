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
	private boolean valid;
	
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
	public boolean getValid(){
		return valid;
	}
	/**
	 * @param name The name to be set for this user. (not null)
	 */
	public void setName(String name){
		this.name  = name;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setValid(boolean valid){
		this.valid = valid;
	}
}
