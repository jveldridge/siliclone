package edu.brown.cs32.siliclone.accounts;

import java.io.Serializable;

/**
 * A user object represents the current client's account information. 
 * This object can be partially not initialized (fields left null).
 * A User object will be constructed when a user logs in or registers
 * for an account.  This User object is then serialized and passed to
 * the database, which will check if it is valid and throw an exception
 * if it is not.
 */
public class User implements Serializable {

	private static final long serialVersionUID = 8738599268117382574L;
	
	private String name; //unique, valid, username 
	private String email; //unique, valid, email (one account per email)
	private String password; //valid password
	private int id; //id in database
	
	public User(){
		
	}
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public User(String name, String password, String email) {
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * @return The name of the user account, null if not yet successfully initialized.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * @return The password of the user account, null if not yet initialized
	 */
	public String getPassword() {
		return this.password;	
	}
	
	/**
	 * @return The email of the user account, null if not yet initialized
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * @return The ID of the database object representing this user account
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @param name The name to be set for this user. (not null)
	 */
	public void setName(String name) {
		this.name  = name;
	}
	
	/**
	 * Sets the password for this User object
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets the email for this User object
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Sets the ID of the database object representing this user account.
	 * This method should not be used by any class other than UserServiceImpl
	 * (which calls it in the login and register methods)
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
}
