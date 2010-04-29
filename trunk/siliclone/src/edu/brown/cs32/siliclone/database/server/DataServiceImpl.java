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
import edu.brown.cs32.siliclone.database.client.DataService;
import edu.brown.cs32.siliclone.database.client.FailedConnectionException;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	//null if no user  
	private User getLoggedIn() throws IOException{
		User u = (User) this.getThreadLocalRequest().getSession().getAttribute("user");
		if(u == null){
			throw new IOException("User is no longer logged in.");
		}
		return u;
	}
	
	
	//todo compression?
	public boolean saveWorkspace(Workspace w, String name) throws IOException {
		if(w == null || name == null){
			throw new IOException("null value given to DataService.saveWorkspace");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.WORKSPACES +
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				conn.close();
				throw new IOException("Workspace with name " + name + " already exists.");
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
				conn.close();
				throw new IOException("Workspace with name " + name + " could not be written.");
			}
			int workspaceId = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.WORKSPACE_USER_PERMISSIONS + 
					"(workspace_id, user_id) values (?, ?)");
			statement.setInt(1, workspaceId);
			statement.setInt(2, u.getId());
			if(0 < statement.executeUpdate()){
				conn.close();
				return true;
			}else {
				conn.close();
				throw new IOException("Could not update permissions to new workspace.");
			}
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}
	
	public Workspace findWorkspace(String name) throws IOException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
//		try{
//			PreparedStatement statement = conn.prepareStatement("select  from " + 
//					Database
//		}
		
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAvailableWorkspaces() throws IOException {
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		ArrayList<String> available = new ArrayList<String>();
		try{
			// add those with explicit user access
			PreparedStatement statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_USER_PERMISSIONS + " as t1 left join " + 
					Database.WORKSPACES + " as t2 on t1.workspace_id == t2.id where t1.user_id = ?;");
			statement.setInt(1, u.getId());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				available.add(res.getString(1));
			}
			
			//add those with group member access (not group owner)
			statement = conn.prepareStatement("select t2.name from " +
					Database.WORKSPACE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.WORKSPACES + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t3) pm (t1.group_id = t3.group_id and " +
					"t1.workspace_id = t2.id) where t3.member_id = ?;");
			statement.setInt(1, u.getId());
			res = statement.executeQuery();
			while(res.next()){
				available.add(res.getString(1));
			}
			conn.close();
			return available;
		}
		catch(SQLException e){
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			throw new FailedConnectionException();
		}
	}

	
	

	
	public boolean saveSequence(DNASequence s, String name) throws IOException {
		if(s == null || name == null){
			throw new IOException("null value given to DataService.saveSequence");
		}
		User u = getLoggedIn();
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.SEQUENCES +
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				conn.close();
				throw new IOException("Sequence with name " + name + " already exists.");
			}
			
			statement = conn.prepareStatement("insert into "+ Database.SEQUENCES + "(name, data) values (?,?)");
			statement.setString(1, name);
			statement.setObject(2, s);
			
			statement.executeUpdate();
			
			statement = conn.prepareStatement("select id from " + Database.SEQUENCES +
					" where name = ?");
			statement.setString(1, name);
			res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				throw new IOException("Sequence with name " + name + " could not be written.");
			}
			int sequenceId = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCE_USER_PERMISSIONS + 
					"(sequence_id, user_id) values (?, ?)");
			statement.setInt(1, sequenceId);
			statement.setInt(2, u.getId());
			if(0 < statement.executeUpdate()){
				conn.close();
				return true;
			}else {
				conn.close();
				throw new IOException("Could not update permissions to new sequence.");
			}
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}

	public List<SequenceHook> getAvailableSequences() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}



}
