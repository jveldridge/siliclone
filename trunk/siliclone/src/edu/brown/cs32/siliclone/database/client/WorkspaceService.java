package edu.brown.cs32.siliclone.database.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;

/**
 * Services for saving and retrieving user data
 * Note that all methods assume that the user has already been validated, 
 * and simply check the username.
 */
@RemoteServiceRelativePath("workspace")
public interface WorkspaceService extends RemoteService {
	
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
	void saveWorkspace(Workspace w, String name) throws DataServiceException;
	
	void overwriteWorkspace(Workspace w, String name) throws DataServiceException;
	
	
	/**
	 * Retrieves the requested workspace, if the workspace exists and the user 
	 * has permission. 
	 * @param u The user, with username defined. not null
	 * @param id The "filename" of the workspace. not null
	 * @return If successful the requested workspace is returned, null otherwise.
	 */
	Workspace findWorkspace(String name) throws DataServiceException;
	
	/**
	 * Gives a list of the saved workspace filenames available to the user. 
	 * If no workspaces are available, returns an empty list.
	 * @param u The user, with name defined. not null
	 * @return A list of workspace names that the user can retrieve, not null
	 */
	List<String> getAvailableWorkspaces() throws DataServiceException;
	
	
	List<String> getOwnedWorkspaces() throws DataServiceException;
	

	public List<String> getPermittedGroups(String workspace) throws DataServiceException;

	public List<User> getPermittedUsers(String workspace) throws DataServiceException;

	public void permitGroup(String workspace, String group) throws DataServiceException ;

	public void permitUser(String workspace, String user) throws DataServiceException;

	public void disallowGroup(String workspace, String group) throws DataServiceException ;
	public void disallowUser(String workspace, String user) throws DataServiceException ;
	
}
