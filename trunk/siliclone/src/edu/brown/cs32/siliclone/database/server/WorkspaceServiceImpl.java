package edu.brown.cs32.siliclone.database.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.WorkspaceService;

/**
 * WorkspaceService is responsible for communicating with the database about workspace data.
 */
@SuppressWarnings("serial")
public class WorkspaceServiceImpl extends RemoteServiceServlet implements
		WorkspaceService {

	/**
	 * Calls user.getLoggedIn to get the user saved to the http session
	 * @return The user object saved using setLoggedIn()
	 * @throws DataServiceException If no user was saved - gives message "User is no longer logged in." 
	 */
	private User getLoggedIn() throws DataServiceException{
		return UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
	}
	
	/**
	 * Gives the integer index of the workspace in the database.
	 * @param conn The connection to the database. not null
	 * @param name  The name of the existing workspace to find. not null
	 * @return The index of the workspace in the database.
	 * @throws DataServiceException : "Workspace with given name not found." or
	 * 		"Error communicating with database."				
	 */
	private int getWorkspaceId(Connection conn, String name) throws DataServiceException{
		
		try {
			PreparedStatement statement = conn.prepareStatement("Select id from " + Database.WORKSPACES + 
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt(1);
			}else{
				throw new DataServiceException("Workspace with given name not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataServiceException("Error communicating with database.");
		}
	}
	
	
	/**
	 * Gives the integer index of the workspace in the database, but only if the current user owns it.
	 * @param conn The connection to the database. not null
	 * @param name  The name of the existing workspace to find. not null
	 * @param u The current user.
	 * @return The index of the workspace in the database.
	 * @throws DataServiceException : "Workspace with given name not found with user as owner." or
	 * 		"Error communicating with database."				
	 */
	private int getOwnedWorkspaceId(Connection conn, String name, User u) throws DataServiceException{
		try {
			PreparedStatement statement = conn.prepareStatement("Select id from " + Database.WORKSPACES + 
					" where name = ? and owner = ?");
			statement.setString(1, name);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt(1);
			}else{
				throw new DataServiceException("Workspace with given name not found with user as owner.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataServiceException("Error communicating with database.");
		}
	}
	
	
	/**
	 * Saves given workspace data to given name, but does not overwrite if that name is already in use.
	 * Records the owner as the user who is currently logged in.
	 * @param w The workspace object to serialize and save to the database. not null
	 * @param name The name to map the workspace to in the database. not null
	 * @throws DataServiceException: "null value given to WorkspaceService.saveWorkspace"
	 * 		"User is no longer logged in." 
	 * 		"Workspace with name " + name + " already exists."
	 * 		"Error writing workspace data."
	 * 		"Error connecting to database."
	 */
	public void saveWorkspace(Workspace w, String name) throws DataServiceException {
		if(w == null || name == null){
			throw new DataServiceException("null value given to WorkspaceService.saveWorkspace");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.WORKSPACES +
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				throw new DataServiceException("Workspace with name " + name + " already exists.");
			}
			
			statement = conn.prepareStatement("insert into "+ Database.WORKSPACES + "(name, data, owner) values (?,?,?)");
			statement.setString(1, name);
			statement.setInt(3, u.getId());
			Database.saveCompressedObject(statement, 2, w);
			statement.executeUpdate();
			
			try{
				getWorkspaceId(conn, name); //tries to check if data was actually saved.
			}catch(DataServiceException e){
				throw new DataServiceException("Error writing workspace data.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error writing workspace data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}
	
	/**
	 * Returns workspace from the database if the user has permission to view this workspace.
	 * @param name The name of the workspace requested. not null
	 * @return The requested workspace if successful.
	 * @throws DataServiceException "User is no longer logged in." 
	 * 		"No workspace with name " + name + " was found with permissions granted to user "
	 * 		"Error reading data."
	 * 		"Error connecting to database."
	 */
	public Workspace findWorkspace(String name) throws DataServiceException {
		
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select data from " + 
					Database.WORKSPACES + " where name = ? and owner = ?");
			statement.setString(1, name);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return (Workspace) Database.loadCompressedObject(res.getBlob(1));
			}
			
			
			statement = conn.prepareStatement("select t2.data from " +
					Database.WORKSPACE_USER_PERMISSIONS + " as t1 left join " + 
					Database.WORKSPACES + " as t2 on t1.workspace_id = t2.id where t2.name = ? " +
							"and t1.user_id = ?;");
			statement.setString(1, name);
			statement.setInt(2, u.getId());
			res = statement.executeQuery();

			if(res.next()){
				return (Workspace) Database.loadCompressedObject(res.getBlob("data"));
			}
			
			statement = conn.prepareStatement("select t2.data from " + 
					Database.WORKSPACE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.WORKSPACES + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t3) on (t1.group_id = t3.group_id and " +
					"t1.workspace_id = t2.id) where t3.member_id = ? and t2.name = ?");
			statement.setInt(1, u.getId());
			statement.setString(2, name);
			res = statement.executeQuery();
			if(res.next()){
				return (Workspace) Database.loadCompressedObject(res.getBlob("data"));
			}else{
				throw new DataServiceException("No workspace with name " + name + 
						" was found with permissions granted to user " + u.getName());
			}
			
		} catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	
	/**
	 * Lists the workspaces that the current user owns.
	 * @return A list of workspace names where the current user is the owner. An empty list if none are found.
	 * @throws DataServiceException  "User is no longer logged in." 
	 * 		or "Error communicating with database."
	 */
	public List<String> getOwnedWorkspaces() throws DataServiceException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		ArrayList<String> owned = new ArrayList<String>();
		try {
			PreparedStatement statement = conn.prepareStatement("select name from " + 
					Database.WORKSPACES + " where owner = ?");
			statement.setInt(1, u.getId());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String name = res.getString(1);
				if(name != null){
					owned.add(name);
				}
			}
			return owned;
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error communicating with database.");
		}finally {
			try{
				conn.close();
			}catch(SQLException e){ e.printStackTrace();}
		}
		
	}
	
	
	/**
	 * Lists workspaces that user has access to. 
	 * @return A list of workspace names where the current user has permission 
	 * 			explicitly or through group permissions. An empty list if none are found.
	 * @throws DataServiceException  "User is no longer logged in." 
	 * 		or "Error communicating with database."
	 */
	public List<String> getAvailableWorkspaces() throws DataServiceException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		List<String> available = getOwnedWorkspaces();
		try{
			// add those with explicit user access
			PreparedStatement statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_USER_PERMISSIONS + " as t1 left join " + 
					Database.WORKSPACES + " as t2 on t1.workspace_id = t2.id where t1.user_id = ?;");
			statement.setInt(1, u.getId());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if (r != null) {
					available.add(r);
				}
			}
			
			//add those with group member access (not group owner)
			statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.WORKSPACES + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t3) on (t1.group_id = t3.group_id and " +
					"t1.workspace_id = t2.id) where t3.member_id = ?;");
			statement.setInt(1, u.getId());
			res = statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if(r != null){
					available.add(r);
				}
			}
			return available;
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}

	/**
	 * Saves workspace over existing data - or makes new entry if no workspace has given name
	 * The owner must be the user currently logged in.
	 * @param w The workspace object to serialize and save to the database. not null
	 * @param name The name to map the workspace to in the database. not null
	 * @throws DataServiceException: "null value given to WorkspaceService.overwriteWorkspace"
	 * 		"User is no longer logged in." 
	 * 		"User does not own workspace, so cannot overwrite it."
	 * 		"Error writing workspace data."
	 * 		"Error connecting to database."
	 */
	public void overwriteWorkspace(Workspace w, String name)
			throws DataServiceException {
		if(w == null || name == null){
			throw new DataServiceException("null value given to WorkspaceService.overwriteWorkspace");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id, owner from " + Database.WORKSPACES +
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				saveWorkspace(w, name);
				return;
			}
			int id = res.getInt(1);
			
			if(res.getInt(2) != u.getId()){
				throw new DataServiceException("User does not own workspace, so cannot overwrite it.");
			}
			
			statement = conn.prepareStatement("update " + Database.WORKSPACES + " set data = ? where id = ?");
			statement.setInt(2, id);
			Database.saveCompressedObject(statement, 1, w);
			if( 0 >= statement.executeUpdate()){
				throw new DataServiceException("Error writing workspace.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error writing workspace.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
		
	}

	/**
	 * Lists all groups that are permitted to view the given workspace.
	 * @param workspace The name of the existing workspace in question. (not null).
	 * @return The list of group names where the groups can view the workspace. Empty if there are none.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 		"Workspace with given name not found."
	 * 		"Error connecting to database."
	 */
	public List<String> getPermittedGroups(String workspace) throws DataServiceException {
		if(workspace == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		ArrayList<String> groups = new ArrayList<String>();
		Connection conn = Database.getConnection();
		try{
			int workID = getWorkspaceId(conn, workspace);//res.getInt(1);
			
			PreparedStatement statement = conn.prepareStatement("select t2.group_name from " + Database.WORKSPACE_GROUP_PERMISSIONS + 
					" as t1 left join " + Database.GROUPS + " as t2 on t1.group_id = t2.id where t1.workspace_id = ?;"); 
			statement.setInt(1, workID);
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if(r != null){
					groups.add(r);
				}
			}
			return groups;
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
		
	}
	
	
	
	/**
	 * Lists the users with explicit permission to read the given workspace.
	 * @param workspace The name of the existing workspace in question. not null
	 * @return The list of Users, with name and email initialized.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 		"Workspace with given name not found."
	 * 		"Error connecting to database."
	 */
	public List<User> getPermittedUsers(String workspace) throws DataServiceException {
		if(workspace == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = Database.getConnection();
		try{
			int workID = getWorkspaceId(conn, workspace);//res.getInt(1);
			
			PreparedStatement statement = conn.prepareStatement("select t2.name, t2.email from " + Database.WORKSPACE_USER_PERMISSIONS + 
					" as t1 left join " + Database.USERS + " as t2 on t1.user_id = t2.id where t1.workspace_id = ?;"); 
			statement.setInt(1, workID);
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if(r != null){
					users.add(new User(res.getString(1), null, res.getString(2)));
				}
			}
			return users;
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}
	
	
	
	
	/**
	 * Adds permission for the given group to the given workspace.
	 * @param workspace The name of an existing workspace owned by the current user.
	 * @param group The name of an existing group.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 			"User is no longer logged in." 
	 * 			"Group with name " + group + " not found in database."
	 *	 		"Workspace with given name not found with user as owner."
	 *			"Group permission to workspace could not be added."
	 *			"Error connecting to database."
	 */
	public void permitGroup(String workspace, String group) throws DataServiceException {
		if(workspace == null || group == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		try{
			int workspaceID = getOwnedWorkspaceId(conn, workspace, u);
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? ");
			statement.setString(1, group);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Group with name " + group + " not found in database.");
			}
			int groupID = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.WORKSPACE_GROUP_PERMISSIONS + 
					"(workspace_id, group_id) values (?, ?)");
			statement.setInt(1, workspaceID);
			statement.setInt(2, groupID);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Group permission to workspace could not be added.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}
	
	
	
	/**
	 * Adds permission for the given user to the given workspace.
	 * @param workspace The name of an existing workspace owned by the current user.
	 * @param user The name of an existing user.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 			"User is no longer logged in." 
	 * 			"User with name " + user + " not found in database."
	 *	 		"Workspace with given name not found with user as owner."
	 *			"User permission to workspace could not be added."
	 *			"Error connecting to database."
	 */
	public void permitUser(String workspace, String user) throws DataServiceException {
		if(workspace == null || user == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		try{
			int workspaceID = getOwnedWorkspaceId(conn, workspace, u);
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS +
					" where name = ? ");
			statement.setString(1, user);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User with name " + user + " not found in database.");
			}
			int userID = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.WORKSPACE_USER_PERMISSIONS + 
					"(workspace_id, user_id) values (?, ?)");
			statement.setInt(1, workspaceID);
			statement.setInt(2, userID);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Group permission to workspace could not be added.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}

	/**
	 * Revokes permission for the given group to the given workspace.
	 * @param workspace The name of an existing workspace owned by the current user.
	 * @param group The name of an existing group with permission to the workspace.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 			"User is no longer logged in." 
	 * 			"Group with name " + group + " not found in database."
	 *	 		"Workspace with given name not found with user as owner."
	 *			"Group already does not have permission to access workspace."
	 *			"Error removing group permission."
	 *			"Error connecting to database."
	 */
	public void disallowGroup(String workspace, String group)
			throws DataServiceException {
		if(workspace == null || group == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			int workspaceID = getOwnedWorkspaceId(conn, workspace, u);
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? ");
			statement.setString(1, group);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Group with name " + group + " not found in database.");
			}
			int groupID = res.getInt(1);
			
			statement = conn.prepareStatement("select * from " + Database.WORKSPACE_GROUP_PERMISSIONS + 
					" where workspace_id = ? and group_id = ?");
			statement.setInt(1, workspaceID);
			statement.setInt(2, groupID);
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Group already does not have permission to access workspace.");
			}
			
			statement = conn.prepareStatement("delete from " + Database.WORKSPACE_GROUP_PERMISSIONS +
					" where workspace_id = ? and group_id = ?");
			statement.setInt(1, workspaceID);
			statement.setInt(2, groupID);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Error removing group permission.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
		
	}

	/**
	 * Revokes permission for the given user to the given workspace.
	 * @param workspace The name of an existing workspace owned by the current user.
	 * @param user The name of an existing user with permission to the workspace.
	 * @throws DataServiceException "Null value passed to WorkspaceService"
	 * 			"User is no longer logged in." 
	 * 			"User with name " + user + " not found in database."
	 *	 		"Workspace with given name not found with user as owner."
	 *			"User already does not have permission to access workspace."
	 *			"Error removing user permission."
	 *			"Error connecting to database."
	 */
	public void disallowUser(String workspace, String user)
			throws DataServiceException {
		if(workspace == null || user == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			int workspaceID = getOwnedWorkspaceId(conn, workspace, u);
			
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS +
					" where name = ? ");
			statement.setString(1, user);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User with name " + user + " not found in database.");
			}
			int userID = res.getInt(1);
			
			statement = conn.prepareStatement("select * from " + Database.WORKSPACE_USER_PERMISSIONS + 
					" where workspace_id = ? and user_id = ?");
			statement.setInt(1, workspaceID);
			statement.setInt(2, userID);
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User already does not have permission to access workspace.");
			}
			
			statement = conn.prepareStatement("delete from " + Database.WORKSPACE_USER_PERMISSIONS +
					" where workspace_id = ? and user_id = ?");
			statement.setInt(1, workspaceID);
			statement.setInt(2, userID);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Error removing user permission.");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}
}
