package edu.brown.cs32.siliclone.database.server;



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
import edu.brown.cs32.siliclone.database.client.UserService;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	
	//TODO better security?
	
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
	
	
	public User changePassword(User u, String newPassword) {
		if(!u.isValid() || newPassword == null || u.getName() == null){
			return u;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("could not connect to database");
			return u;
		}
		
		String password = encrypt(newPassword, 50);
		if(password == null){
			return u;
		}
		
		
		PreparedStatement statement = null;
		
		try{
			statement = conn.prepareStatement("update " + Database.USERS + 
						" set password = ? where name = ? ;");
			statement.setString(1, password);
			statement.setString(2, u.getName());
			
			if(0 < statement.executeUpdate()){
				u.setPassword(newPassword);
			}
			
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try {
				if(statement != null){ statement.close();}
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
		return u;
	}

	
	
	public User login(User u) {
		
		if(u.getName() == null || u.getPassword() == null){
			return u;
		}

		String password = encrypt(u.getPassword(), 50);
		if(password == null){
			return u;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("could not connect to database");
			return u;
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("select * from " + Database.USERS + " where " +
					"name = ? and password = ? ;");
			statement.setString(1, u.getName());
			statement.setString(2, password);
			
			res = statement.executeQuery();
			if(res.first()){ //reached 1st entry - row for the specified user with password
				u.setValid(true);
			}
		}catch (Exception e){ e.printStackTrace();
		}finally{
			try {
				if(res != null){ res.close();} 
				if(statement != null){ statement.close(); }
				conn.close();
			}catch (SQLException e) { e.printStackTrace(); }
		}
		return u;
	}

	
	
	
	public User register(User u) {
		if(u.getEmail() == null || u.getEmail() == null || u.getPassword() == null){
			return u;
		}

		String password = encrypt(u.getPassword(), 50);
		if(password == null){
			return u;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("could not connect to database");
			return u;
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		
		try{
			statement = conn.prepareStatement("select password from " + Database.USERS + 
					" where name = ? ;");
			statement.setString(1, u.getName());
			
			res = statement.executeQuery();
			
			if(res.next()){ //reached 1st entry - row for the specified user
				res.close();
				statement.close();
				conn.close();
				return u;
			}
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("insert into "+ Database.USERS + 
					"(name, email, password) values (?,?,?);");
			statement.setString(1, u.getName());
			statement.setString(2, u.getEmail());
			statement.setString(3, password);
			
			if(0 < statement.executeUpdate()){
				u.setValid(true);
			}
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try {
				if(res != null) {res.close();}
				if(statement != null){statement.close();
				conn.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return u;
	}

	

	public User remove(User u) {
		if(u.getName() == null || !u.isValid()){
			return u;
		}

		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("could not connect to database");
			return u;
		}

		PreparedStatement statement = null;
		ResultSet res = null;
		try{ //delete group memberships, then delete user, leave groups/data that others might need.
			statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return u;
			}
			int id = res.getInt(1);
			res.close();
			
			if(0 < statement.executeUpdate("delete from " + Database.USERS + " where id = " + id + ";")){
				u.setValid(false);
			}
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where member_id = " + id);
			statement.executeUpdate("delete from " + Database.WORKSPACE_USER_PERMISSIONS + " where user_id = " + id);
			statement.executeUpdate("delete from " + Database.SEQUENCE_USER_PERMISSIONS + " where user_id = " + id);			
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
		return u;
	}


	
	
	public String addToGroup(User u, String group, String userToAdd) {
		if(u.getName() == null || group == null || userToAdd == null){
			return "Null value given to addToGroup";
		}

		Connection conn = Database.getConnection();
		if(conn == null){
			return "Connection error";
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "could not find requesting user in database.";
			}
			int ownerId = res.getInt(1);
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, userToAdd);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "could not find user to add in database.";
			}
			int newId = res.getInt(1);
			res.close();
			statement.close();

			statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? and owner_id = ?;");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "could not group " + group + " with requesting owner.";
			}
			int groupId = res.getInt(1);
			res.close();
			statement.close();
			

			statement = conn.prepareStatement("select id from " + Database.GROUP_PERMISSIONS +
					" where group_id = ? and member_id = ?;");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "User " + userToAdd + " is already a member of group " + group;
			}
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("insert into " + Database.GROUP_PERMISSIONS
					+ " (group_id, member_id) values (?, ?);");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			if(0 < statement.executeUpdate()){
				statement.close();
				conn.close();
				return "User " + userToAdd + " successfully added to group " + group;
			}else {
				statement.close();
				conn.close();
				return "Could not add user " + userToAdd + " to group " + group;
			}
		}catch (SQLException e){ 
			try{
				if(res != null){res.close();}
				if(statement != null){statement.close();}
				conn.close();
			}catch(SQLException e1){e1.printStackTrace();}
			return "Error calling database to add user to group.";
		}
	}


		

	public String createGroup(User u, String group) {
		if(!u.isValid() || u.getName() == null || group == null){
			return "Null value or invalid user passed to createGroup";
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			return "Connection error";
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?");
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "error - user could not be found in database.";
			}
			int id = res.getInt(1);
			res.close();
			statement.close();
			
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ?");
			statement.setString(1, group);
			res = statement.executeQuery();
			if(res.next()){
				res.close();
				statement.close();
				conn.close();
				return "Group with name " + group + " already exists";
			}
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("insert into " + Database.GROUPS + " (group_name, owner_id) values (?,?);");
			statement.setString(1, group);
			statement.setInt(2, id);
			if(0 < statement.executeUpdate()){
				statement.close();
				conn.close();
				return "Group " + group + " successfully created with owner " + u.getName();
			}else {
				statement.close();
				conn.close();
				return "Group " + group + " could not be added";
			}
		}catch (SQLException e){
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			return "There was an error when making the group.";
		}
	}


	

	public List<String> getAvailableGroups(User u) {
		ArrayList<String> available = (ArrayList<String>) getOwnedGroups(u);
		if(u.getName() == null){
			return available;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("error connecting to database");
			return available;
		}
		
		PreparedStatement statement = null;
		ResultSet res1 = null;
		ResultSet res2 = null;
		try{
			statement = conn.prepareStatement("Select id from " + Database.USERS + " where name = ?");
			statement.setString(1, u.getName());
			res1 = statement.executeQuery();
			if(!res1.next()){
				res1.close();
				statement.close();
				conn.close();
				System.err.println("Querying user could not be found " + u.getName());
				return available;
			}
			int id = res1.getInt(1);
			res1.close();
			statement.close();
			
			statement = conn.prepareStatement("Select group_id from " + Database.GROUP_PERMISSIONS + " where member_id = ?");
			statement.setInt(1, id);
			res1 = statement.executeQuery();
			while(res1.next()){
				res2 = statement.executeQuery("Select group_name from " + Database.GROUPS + " where id = " + res1.getInt(1));
				available.add(res2.getString(1));
				res2.close();
			}
			res1.close();
			statement.close();
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res1 != null){ res1.close();}
				if(res2 != null){ res2.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
		return available;
	}



	public List<String> getOwnedGroups(User u) {
		ArrayList<String> owned = new ArrayList<String>();
		if(u.getName() == null){
			return owned;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("error connecting to database");
			return owned;
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("Select id from " + Database.USERS + " where name = ?");
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				System.err.println("Querying user could not be found " + u.getName());
				return owned;
			}
			int id = res.getInt(1);
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("Select group_name from " + Database.GROUPS + " where owner_id = ?");
			statement.setInt(1, id);
			res = statement.executeQuery();
			while(res.next()){
				owned.add(res.getString(1));
			}
			res.close();
			statement.close();
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
		return owned;
	}



	public List<String> getUsersWithAccessToGroup(String group) {
		ArrayList<String> users = new ArrayList<String>();
		if(group == null){
			return users;
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			System.err.println("error connecting to database");
			return users;
		}
		
		PreparedStatement statement = null;
		ResultSet res1 = null;
		ResultSet res2 = null;
		try{
			statement = conn.prepareStatement("select * from " + Database.GROUPS + " where group_name = ?");
			statement.setString(1, group);
			res1 = statement.executeQuery();
			if(!res1.next()){
				res1.close();
				statement.close();
				conn.close();
				return users;
			}
			int groupId = res1.getInt(1);
			res2 = statement.executeQuery("select name from " + Database.USERS + " where id = " + res1.getInt(3));
			users.add(res2.getString(1));
			res2.close();
			res1.close();
			statement.close();
			
			statement = conn.prepareStatement("select member_id from " + Database.GROUP_PERMISSIONS + 
					" where group_id = ?");
			statement.setInt(1, groupId);
			res1 = statement.executeQuery();
			while(res1.next()){
				res2 = statement.executeQuery("select name from " + Database.USERS + " where id = " + res1.getInt(1));
				users.add(res2.getString(1));
				res2.close();
			}
			res1.close();
			statement.close();
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res1 != null){ res1.close();}
				if(res2 != null){ res2.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
		return users;
	}




	public String removeFromGroup(User u, String group, String userToRemove) {
		if(u.getName() == null || userToRemove == null || group == null){
			return "Null value given";
		}

		if(u.getName() == userToRemove){
			return "Cannot remove owner without deleting group";
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			return "error connecting to database";
		}

		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("select id from " + Database.USERS + "where name = ?");
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "Error- requesting user not found in database.";
			}
			int requestingId = res.getInt(1);
			res.close();
			
			statement.setString(1, userToRemove);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "User to remove not found in database.";
			}
			int toRemoveId = res.getInt(1);
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS + " where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, requestingId);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			int groupId = res.getInt(1);
			res.close();
			statement.close();
			
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
				if(res != null){res.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			return "Error connecting to database to remove user from group.";
		}	
	}
	
	
	public String removeGroup(User u, String group){
		if(u.getName() == null || group == null){
			return "Null value given.";
		}
		
		Connection conn = Database.getConnection();
		if(conn == null){
			return "error connecting to database";
		}
		
		PreparedStatement statement = null;
		ResultSet res = null;
		try{
			statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?");
			statement.setString(1, u.getName());
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "error - could not find requesting user in database.";
			}
			int ownerId = res.getInt(1);
			res.close();
			statement.close();
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			res = statement.executeQuery();
			if(!res.next()){
				res.close();
				statement.close();
				conn.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			int groupId = res.getInt(1);
			res.close();
			
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.WORKSPACE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.SEQUENCE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.GROUPS + " where id = " + groupId);
			statement.close();
			return "Group " + group + " removed.";
			
		}catch (SQLException e){
			try{
				if(res != null){res.close();}
				if(statement != null){ statement.close();}
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
			return "Error connecting to database to remove group.";
		}	
	}
	
	/**
	public static void main(String[] args){
		UserServiceImpl i = new UserServiceImpl();
		User u = new User();
		u.setName("user2");
		u.setValid(false);
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
		
		try {
			Database.getSingleConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
}
