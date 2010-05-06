package edu.brown.cs32.siliclone.database.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.WorkspaceService;

@SuppressWarnings("serial")
public class WorkspaceServiceImpl extends RemoteServiceServlet implements
		WorkspaceService {

	private User getLoggedIn() throws DataServiceException{
		return UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
	}
	
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
	
	
	//TODO compression?
	/**
	 * saves workspace with given data, name does not overwrite
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
			
			int workspaceId = -1;
			try{
				workspaceId = getWorkspaceId(conn, name);
			}catch(DataServiceException e){
				throw new DataServiceException("Workspace data could not be written.");
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
	 * returns workspace with given name if user has permission
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
	 * saves workspace over existing data - or makes new entry if no workspace has given name
	 */
	public void overwriteWorkspace(Workspace w, String name)
			throws DataServiceException {
		if(w == null || name == null){
			throw new DataServiceException("null value given to WorkspaceService.saveWorkspace");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id, owner from " + Database.WORKSPACES +
					" where name = ?");
			statement.setString(1, name);
			//statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				saveWorkspace(w, name);
			}
			int id = res.getInt(1);
			
			if(res.getInt(2) != u.getId()){
				throw new DataServiceException("User does not own workspace, so cannot overwrite it.");
			}
			
			statement = conn.prepareStatement("update " + Database.WORKSPACES + " set data = ? where id = ?");
			statement.setInt(2, id);
			Database.saveCompressedObject(statement, 1, w);
			if( 0 >= statement.executeUpdate()){
				throw new DataServiceException("Workspace could not be updated.");
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

	public List<String> getPermittedGroups(String workspace) throws DataServiceException {
		if(workspace == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		ArrayList<String> groups = new ArrayList<String>();
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ?");
			statement.setString(1, workspace);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace + " not found in database.");
			}
			int workID = res.getInt(1);
			
			statement = conn.prepareStatement("select t2.group_name from " + Database.WORKSPACE_GROUP_PERMISSIONS + 
					" as t1 left join " + Database.GROUPS + " as t2 on t1.group_id = t2.id where t1.workspace_id = ?;"); 
			statement.setInt(1, workID);
			res = statement.executeQuery();
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
	
	
	

	public List<User> getPermittedUsers(String workspace) throws DataServiceException {
		if(workspace == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ?");
			statement.setString(1, workspace);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace + " not found in database.");
			}
			int workID = res.getInt(1);
			
			statement = conn.prepareStatement("select t2.name, t2.email from " + Database.WORKSPACE_USER_PERMISSIONS + 
					" as t1 left join " + Database.USERS + " as t2 on t1.user_id = t2.id where t1.workspace_id = ?;"); 
			statement.setInt(1, workID);
			res = statement.executeQuery();
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
	
	
	
	

	public void permitGroup(String workspace, String group) throws DataServiceException {
		if(workspace == null || group == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ? and owner = ?");
			statement.setString(1, workspace);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace +
						" not found with current user as owner.");
			}
			int workspaceID = res.getInt(1);
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? ");
			statement.setString(1, group);
			res = statement.executeQuery();
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
	
	
	

	public void permitUser(String workspace, String user) throws DataServiceException {
		if(workspace == null || user == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ? and owner = ?");
			statement.setString(1, workspace);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace +
						" not found with current user as owner.");
			}
			int workspaceID = res.getInt(1);
			
			statement = conn.prepareStatement("select id from " + Database.USERS +
					" where name = ? ");
			statement.setString(1, user);
			res = statement.executeQuery();
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

	public void disallowGroup(String workspace, String group)
			throws DataServiceException {
		if(workspace == null || group == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ? and owner = ?");
			statement.setString(1, workspace);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace +
						" not found with current user as owner.");
			}
			int workspaceID = res.getInt(1);

			
			statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? ");
			statement.setString(1, group);
			res = statement.executeQuery();
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

	public void disallowUser(String workspace, String user)
			throws DataServiceException {
		if(workspace == null || user == null){
			throw new DataServiceException("Null value passed to WorkspaceService");
		}
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + 
					Database.WORKSPACES + " where name = ? and owner = ?");
			statement.setString(1, workspace);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + workspace +
						" not found with current user as owner.");
			}
			int workspaceID = res.getInt(1);

			
			statement = conn.prepareStatement("select id from " + Database.USERS +
					" where name = ? ");
			statement.setString(1, user);
			res = statement.executeQuery();
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
