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
	
	private String _name; //unique, valid, username 
	private String _email; //unique, valid, email (one account per email)
	private String _password; //valid password
	private int _id; //id in database
	
	public User(){
		
	}
	
	public User(String name, String password){
		_name = name;
		_password = password;
	}
	
	public User(String name, String password, String email){
		_name = name;
		_password = password;
		_email = email;
	}
	
	/**
	 * @return The name of the user account, null if not yet successfully initialized.
	 */
	public String getName(){
		return _name;
	}
	
	public String getPassword(){
		return _password;	
	}
	
	public String getEmail(){
		return _email;
	}
	
	public int getId(){
		return _id;
	}
	
	/**
	 * @param name The name to be set for this user. (not null)
	 */
	public void setName(String name){
		_name  = name;
	}
	
	public void setPassword(String password){
		_password = password;
	}
	
	public void setEmail(String email){
		_email = email;
	}
	
	public void setId(int id){
		_id = id;
	}
}
