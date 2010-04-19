package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.brown.cs32.siliclone.accounts.User;

/**
 * The User Service manages account access.
 */
public interface UserService extends RemoteService {
	/**
	 * Given a user object (username and password are set)
	 * determines whether that user exists in the server's user database.
	 * @param u The user being validated, with username and password. not null
	 * @return True if the user was validated, false otherwise.
	 */
	boolean login(User u);
	/**
	 * Given a user that was already validated, changes their password in the 
	 * database. Note that the user object needs to be updated with the password after.
	 * @param u The user with old password still set. not null
	 * @param newPassword The new password for the user.
	 * @return true if success, false otherwise
	 */
	boolean changePassword(User u, String newPassword);
	/**
	 * Given a new user object (with username, password, email address), 
	 * attempts to register that user to the database with the given email address.
	 * @param u The new user to be added. not null
	 * @return true if registration was successful, false otherwise.
	 */
	boolean register(User u);
}
