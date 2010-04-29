package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.accounts.User;

/**
 * The User Service manages account access.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	/**
	 * Given a user object (username and password are set)
	 * determines whether that user exists in the server's user database.
	 * @param u The user being validated, with username and password. not null
	 * @return True if the user was validated, false otherwise.
	 */
	User login(User u) throws IOException;
	/**
	 * Given a user that was already validated, changes their password in the 
	 * database. Note that the user object needs to be updated with the password after.
	 * @param u The user with old password still set. not null
	 * @param newPassword The new password for the user.
	 * @return true if success, false otherwise
	 */
	User changePassword(String newPassword) throws IOException;
	/**
	 * Given a new user object (with username, password, email address), 
	 * attempts to register that user to the database with the given email address.
	 * @param u The new user to be added. not null
	 * @return true if registration was successful, false otherwise.
	 */
	User register(User u) throws IOException;
	
	
	User remove(User u) throws IOException;
	
	/**
	 * @param groupname group that doesn't already exist
	 * @param u owner of group
	 * @return message describing success or failure
	 */
	String createGroup(String group) throws IOException;
	
	List<String> getOwnedGroups() throws IOException;
	List<String> getAvailableGroups() throws IOException;
	
	String addToGroup(String group, String userToAdd) throws IOException;
	String removeFromGroup(String group, String userToRemove) throws IOException;
	
	/**
	 * 
	 * @param group
	 * @return empty if group not found
	 */
	List<User> getUsersWithAccessToGroup(String group) throws IOException;
}
