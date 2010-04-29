package edu.brown.cs32.siliclone.database.server;



import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.FailedConnectionException;
import edu.brown.cs32.siliclone.database.client.UserService;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	
	//source http://www.rgagnon.com/javadetails/java-0400.html
	private String encrypt(String word, int maxLength){
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("SHA-1");
			d.reset();
			d.update(word.getBytes());
			byte[] bytes = d.digest();
			String encrypted = "";
			for(int i = 0; i < bytes.length && i < maxLength; i++){
				encrypted += (char) (bytes[i] & 0xff);
			}
			return encrypted;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//null if no user  
	private User getLoggedIn() throws IOException{
		User u = (User) this.getThreadLocalRequest().getSession().getAttribute("user");
		if(u == null){
			throw new IOException("User is no longer logged in.");
		}
		return u;
	}
	
	private void setLoggedIn(User u){
		this.getThreadLocalRequest().getSession().setAttribute("user", u);
	}
	
	

	public User login(User u) throws IOException{
		if(u == null || u.getName() == null || u.getPassword() == null){
			return null;
		}
		String password = encrypt(u.getPassword(), 50);
		if(password == null){
			return null;
		}
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id, email from " + Database.USERS + " where " +
					"name = ? and password = ? ;");
			statement.setString(1, u.getName());
			statement.setString(2, password);
			
			ResultSet res = statement.executeQuery();
			if(res.first()){ //reached 1st entry - row for the specified user with password
				u.setId(res.getInt(1));
				u.setEmail(res.getString(2));
				u.setPassword(null);
				setLoggedIn(u);
			}else {
				u = null;
			}
			conn.close();
			return u;
		}catch (Exception e){ 
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}
	
	
	
	public User changePassword(String newPassword) throws IOException {
		User u = getLoggedIn();
		if(newPassword == null){
			return null;
		}
		
		Connection conn = Database.getConnection();
		
		String password = encrypt(newPassword, 50);
		if(password == null){ return null; }
		
		try{
			PreparedStatement statement = conn.prepareStatement("update " + Database.USERS + 
						" set password = ? where name = ? ;");
			statement.setString(1, password);
			statement.setString(2, u.getName());
			
			if(0 < statement.executeUpdate()){
				conn.close();
			}else{
				u = null;
			}
			return u;
		}catch (SQLException e){ 
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}

		

	//todo javax.mail 
	public User register(User u) throws IOException {
		if(u == null || u.getEmail() == null || u.getName() == null || u.getPassword() == null){
			return null;
		}

		String password = encrypt(u.getPassword(), 50);
		if(password == null){ return null; }
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + 
					" where name = ? ;");
			statement.setString(1, u.getName());
			
			ResultSet res = statement.executeQuery();
			
			if(res.next()){ //reached 1st entry - row for the specified user
				conn.close();
				return null;
			}
			
			statement = conn.prepareStatement("insert into "+ Database.USERS + 
					"(name, email, password) values (?,?,?);");
			statement.setString(1, u.getName());
			statement.setString(2, u.getEmail());
			statement.setString(3, password);
			
			if(0 < statement.executeUpdate()){
				statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;"); //all
				statement.setString(1, u.getName());  // of this 
				res= statement.executeQuery(); // will be 
				u.setId(res.getInt(1));// deleted
				conn.close();
				u.setPassword(null);
				setLoggedIn(u);//todo send email instead
				return u;
			}else{
				conn.close();
				throw new FailedConnectionException();
			}
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}

	
	//todo email notification?
	public User remove(User u)  throws IOException {
		if(u== null || u.getName() == null){
			return null;
		}

		Connection conn = Database.getConnection();

		try{ //delete group memberships, then delete user, leave groups/data that others might need.
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, u.getName());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return null;
			}
			int id = res.getInt(1);
			
			if(0 < statement.executeUpdate("delete from " + Database.USERS + " where id = " + id + ";")){
				return null;
			}
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where member_id = " + id);
			statement.executeUpdate("delete from " + Database.WORKSPACE_USER_PERMISSIONS + " where user_id = " + id);
			statement.executeUpdate("delete from " + Database.SEQUENCE_USER_PERMISSIONS + " where user_id = " + id);			
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try{
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
		return u;
	}


	
	
	public String addToGroup(String group, String userToAdd) throws IOException {
		User u = getLoggedIn();
		if(group == null || userToAdd == null){
			throw new IOException("A null value was passed to UserService.addToGroup.");
		}

		Connection conn = Database.getConnection();
		
		try{
			int ownerId = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, userToAdd);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "could not find user "+userToAdd+ " to add in database.";
			}
			int newId = res.getInt(1);

			statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? and owner_id = ?;");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "could not find group " + group + " with requesting owner " + u.getName();
			}
			int groupId = res.getInt(1);

			statement = conn.prepareStatement("select id from " + Database.GROUP_PERMISSIONS +
					" where group_id = ? and member_id = ?;");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "User " + userToAdd + " is already a member of group " + group;
			}
			
			statement = conn.prepareStatement("insert into " + Database.GROUP_PERMISSIONS
					+ " (group_id, member_id) values (?, ?);");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			if(0 < statement.executeUpdate()){
				conn.close();
				return "User " + userToAdd + " successfully added to group " + group;
			}else {
				conn.close();
				return "Could not add user " + userToAdd + " to group " + group;
			}
		}catch (SQLException e){ 
			try{
				conn.close();
			}catch(SQLException e1){e1.printStackTrace();}
			return "Error calling database to add user to group.";
		}
	}


		

	public String createGroup(String group) throws IOException {
		User u = getLoggedIn();
		if(group == null){
			return "Null value passed to UserService.createGroup";
		}
		
		Connection conn = Database.getConnection();
		
		try{
			int id = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ?");
			statement.setString(1, group);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				conn.close();
				return "Group with name " + group + " already exists";
			}
			
			statement = conn.prepareStatement("insert into " + Database.GROUPS + " (group_name, owner_id) values (?,?);");
			statement.setString(1, group);
			statement.setInt(2, id);
			if(0 < statement.executeUpdate()){
				conn.close();
				return "Group " + group + " successfully created with owner " + u.getName();
			}else {
				conn.close();
				return "Group " + group + " could not be added";
			}
		}catch (SQLException e){
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			throw new FailedConnectionException();
		}
	}


	

	public List<String> getAvailableGroups() throws IOException {
		User u = getLoggedIn();
		ArrayList<String> available = (ArrayList<String>) getOwnedGroups();
		
		Connection conn = Database.getConnection();
		
		try{
			int id = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("Select " + Database.GROUPS + ".group_name from " + 
					Database.GROUP_PERMISSIONS + " left join " + Database.GROUPS + " on " +
					Database.GROUP_PERMISSIONS + ".group_id = " + Database.GROUPS + ".id where " + 
					Database.GROUP_PERMISSIONS + ".member_id = ?");
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			while(res.next()){
				available.add(res.getString(1));
			}
			conn.close();
			return available;
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}



	public List<String> getOwnedGroups() throws IOException {
		User u = getLoggedIn();
		
		ArrayList<String> owned = new ArrayList<String>();
		
		Connection conn = Database.getConnection();
		
		try{
			int id = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("Select group_name from " + Database.GROUPS + " where owner_id = ?");
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			while(res.next()){
				owned.add(res.getString(1));
			}
			conn.close();
			return owned;
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}



	public List<User> getUsersWithAccessToGroup(String group) throws IOException {
		ArrayList<User> users = new ArrayList<User>();
		
		if(group == null){
			return users;
		}
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select " + Database.GROUPS + ".id, " + 
					Database.USERS + ".name, " + Database.USERS + ".email from "+ Database.GROUPS +
					" left join " + Database.USERS + " on " + Database.GROUPS + ".owner_id = " + 
					Database.USERS + ".id where " + Database.GROUPS + ".group_name = ?");
			statement.setString(1, group);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return users;
			}
			int groupId = res.getInt(1);
			User owner = new User();
			owner.setName(res.getString(2));
			owner.setEmail(res.getString(3));
			users.add(owner);
			
			
			statement = conn.prepareStatement("select " + Database.USERS + ".name, " + Database.USERS +
					".email from " + Database.GROUP_PERMISSIONS + " left join " + Database.USERS + 
					" on " + Database.GROUP_PERMISSIONS + ".member_id = " +
					Database.USERS + ".id where " + Database.GROUP_PERMISSIONS + ".group_id = ?");
			statement.setInt(1, groupId);
			res = statement.executeQuery();
			while(res.next()){
				User u = new User();
				u.setName(res.getString(1));
				u.setEmail(res.getString(2));
				users.add(u);
			}
			conn.close();
			return users;
		}catch (SQLException e){
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
			throw new FailedConnectionException();
		}
	}




	public String removeFromGroup(String group, String userToRemove) throws IOException {
		User u = getLoggedIn();
		if(u == null){
			throw new IOException("User is no longer logged in.");
		}
		
		if(userToRemove == null || group == null){
			throw new IOException("Null value given to UserService.removeFromGroup");
		}

		if(u.getName() == userToRemove){
			return "Cannot remove owner without deleting group";
		}
		
		Connection conn = Database.getConnection();

		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?");
			statement.setString(1, userToRemove);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "User to remove not found in database.";
			}
			int toRemoveId = res.getInt(1);
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS + " where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, u.getId());
			res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			int groupId = res.getInt(1);
			
			statement = conn.prepareStatement("delete from " + Database.GROUP_PERMISSIONS + " where member_id = ? and group_id = ?");
			statement.setInt(1, toRemoveId);
			statement.setInt(2, groupId);
			if(0 < statement.executeUpdate()){
				return "User " + userToRemove + " removed from group " + group;
			}else{
				return "User " + userToRemove + " was not a member of group " + group;
			}
		}catch (SQLException e){
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			throw new FailedConnectionException();
		}	
	}
	
	
	public String removeGroup(String group) throws IOException{
		User u = getLoggedIn();
		if(u == null){
			throw new IOException("User is no longer logged in.");
		}
		if(group == null){
			throw new IOException("Null value given to UserService.removeGroup.");
		}
		
		Connection conn = Database.getConnection();
		
		try{
			int ownerId = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				conn.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			int groupId = res.getInt(1);
			
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.WORKSPACE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.SEQUENCE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.GROUPS + " where id = " + groupId);
			
			conn.close();
			return "Group " + group + " removed.";
			
		}catch (SQLException e){
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			throw new FailedConnectionException();
		}	
	}
	
	
	public static void main(String[] args){
		/**
		UserServiceImpl i = new UserServiceImpl();
		User u = new User();
		u.setName("user2");
		u.setEmail("email@site.com");
		u.setPassword("password");
		u = i.register(u);
		if(u.isValid()){
			System.out.println("successfully registered new user!");
		}
		
		u.setValid(false);
		u = i.login(u);
		if(u.isValid()){
			System.out.println("successfully logged in!");
		}
		
		u = i.changePassword(u, "password");
		if(u.getPassword().equals("password")){
			System.out.println("And change the password!");
		}
		**/
	}

}
