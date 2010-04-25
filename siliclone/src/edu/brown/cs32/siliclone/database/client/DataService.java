package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.operators.Operator;

/**
 * Services for saving and retrieving user data
 * Note that all methods assume that the user has already been validated, 
 * and simply check the username.
 */
public interface DataService extends RemoteService {
	/**
	 * Saves the given workspace to the server's database.
	 * The workspace defines a layout of many operators.
	 * The user need only be partially defined - getUserName() should 
	 * return a string for a user who has access to the workspace. 
	 * User permissions are saved within the workspace object.
	 * @param user The user saving the workspace. Not null
	 * @param w The workspace to be saved. Not null
	 * @return true if saving was successful, false otherwise.
	 */
	boolean saveWorkspace(User user, Workspace w);
	/**
	 * Retrieves the requested workspace, if the workspace exists and the user 
	 * has permission. 
	 * @param u The user, with username defined. not null
	 * @param id The "filename" of the workspace. not null
	 * @return If successful the requested workspace is returned, null otherwise.
	 */
	Workspace findWorkspace(User u, String id);
	/**
	 * Gives a list of the saved workspace filenames available to the user. 
	 * If no workspaces are available, returns an empty list.
	 * @param u The user, with name defined. not null
	 * @return A list of workspace names that the user can retrieve, not null
	 */
	List<String> getAvailableWorkspaces(User u);
	
	/**
	 * Saves the given sequence to the database.
	 * If a sequence of the same name already exists and is accessible, 
	 * does not write anything but returns true.
	 * @param user The user with username defined. not null
	 * @param s The sequence to write. not null
	 * @return True is success (the sequence is now in the database) false otherwise.
	 */
	boolean saveSequence(User user, DNASequence s);
	/**
	 * Retrieves the requested sequence if available.
	 * @param u The user with username defined. not null
	 * @param id The filename identifying the sequence.
	 * @return The sequence requested, null if failure (does not exists, 
	 * 	permissions, or error)
	 */
	DNASequence findSequence(User u, String id);
	/**
	 * Lists available sequence filenames. An empty list is returned 
	 * if none are available.
	 * @param u The user, with username defined. not null
	 * @return The list of available sequences, not null
	 */
	List<String> getAvailableSequences(User u);
}
