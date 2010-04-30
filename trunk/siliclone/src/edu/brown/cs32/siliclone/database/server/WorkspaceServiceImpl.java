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
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public class WorkspaceServiceImpl extends RemoteServiceServlet implements
		WorkspaceService {

	private User getLoggedIn() throws DataServiceException{
		User u = (User) this.getThreadLocalRequest().getSession().getAttribute("user");
		if(u == null){
			throw new DataServiceException("User is no longer logged in.");
		}
		return u;
	}
	
	
	//TODO compression?
	public void saveWorkspace(Workspace w, String name) throws DataServiceException {
		if(w == null || name == null){
			throw new DataServiceException("null value given to DataService.saveWorkspace");
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
			
			statement = conn.prepareStatement("insert into "+ Database.WORKSPACES + "(name, data) values (?,?)");
			statement.setString(1, name);
			statement.setObject(2, w);
			statement.executeUpdate();
			
			statement = conn.prepareStatement("select id from " + Database.WORKSPACES +
					" where name = ?");
			statement.setString(1, name);
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Workspace with name " + name + " could not be written.");
			}
			int workspaceId = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.WORKSPACE_USER_PERMISSIONS + 
					"(workspace_id, user_id) values (?, ?)");
			statement.setInt(1, workspaceId);
			statement.setInt(2, u.getId());
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Could not update permissions to new workspace.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}
	
	public Workspace findWorkspace(String name) throws DataServiceException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select t2.data from " +
					Database.WORKSPACE_USER_PERMISSIONS + " as t1 left join " + 
					Database.WORKSPACES + " as t2 on t1.workspace_id = t2.id where t2.name = ? " +
							"and t1.user_id = ?;");
			statement.setString(1, name);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return (Workspace) res.getObject(1);
			}
			
			statement = conn.prepareStatement("select t2.data from " + 
					Database.WORKSPACE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.WORKSPACES + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t2) on (t1.group_id = t2.group_id and " +
					"t1.workspace_id = t2.id) where t3.member_id = ? and t2.name = ?");
			statement.setInt(1, u.getId());
			statement.setString(2, name);
			res = statement.executeQuery();
			if(res.next()){
				return (Workspace) res.getObject(1);
			}else{
				throw new DataServiceException("No workspace with name " + name + 
						" was found with permissions granted to user " + u.getName());
			}
			
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	public List<String> getAvailableWorkspaces() throws DataServiceException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		ArrayList<String> available = new ArrayList<String>();
		try{
			// add those with explicit user access
			PreparedStatement statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_USER_PERMISSIONS + " as t1 left join " + 
					Database.WORKSPACES + " as t2 on t1.workspace_id = t2.id where t1.user_id = ?;");
			statement.setInt(1, u.getId());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				available.add(res.getString(1));
			}
			
			//add those with group member access (not group owner)
			statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.WORKSPACES + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t3) on (t1.group_id = t3.group_id and " +
					"t1.workspace_id = t2.id) where t3.member_id = ?;");
			statement.setInt(1, u.getId());
			res = statement.executeQuery();
			while(res.next()){
				available.add(res.getString(1));
			}
			return available;
		}
		catch(SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}

}