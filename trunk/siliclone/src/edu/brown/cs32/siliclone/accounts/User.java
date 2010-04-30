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

	private static final long serialVersionUID = 8738599268117382574L;
	
	private String name; //unique, valid, username 
	private String email; //unique, valid, email (one account per email)
	private String password; //valid password
	private int id; //id in database
	
	public User(){
		
	}
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	public User(String name, String password, String email){
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
	
	public String getPassword(){
		return this.password;	
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public int getId(){
		return this.id;
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
	
	public void setId(int id){
		this.id = id;
	}
}
