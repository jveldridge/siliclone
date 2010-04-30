package edu.brown.cs32.siliclone.database.server;

//TODO move everything to finallys, throw proper types of exceptions, and comment code

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
import edu.brown.cs32.siliclone.database.client.DataServiceException;
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
	
	//throws error if user not logged in.
	private User getLoggedIn() throws DataServiceException{
		User u = (User) this.getThreadLocalRequest().getSession().getAttribute("user");
		if(u == null){
			throw new DataServiceException("User is no longer logged in.");
		}
		return u;
	}
	
	private void setLoggedIn(User u){
		this.getThreadLocalRequest().getSession().setAttribute("user", u);
	}

	/**
	 * Logs in the user passed as a parameter.  If the login was successful,
	 * the same user object will be returned.  If the login was unsuccessful,
	 * this method returns null.
	 * 
	 * @param u User to be logged in
	 * @return an object representing the User now logged in
	 * @throws FailedConnectionException if connecting to the 
	 * 		   database was unsuccessful
	 */
	public User login(User u) throws DataServiceException {
		if(u == null || u.getName() == null || u.getPassword() == null){
			throw new DataServiceException("Null value passed to UserService.login");
		}
		
		String password = encrypt(u.getPassword(), 50);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id, email from " + Database.USERS + 
					" where name = ? and password = ? ;");
			statement.setString(1, u.getName());
			statement.setString(2, password);
			
			ResultSet res = statement.executeQuery();
			if(res.first()){ //reached 1st entry - row for the specified user with password
				u.setId(res.getInt(1));
				u.setEmail(res.getString(2));
				u.setPassword(null);
				setLoggedIn(u);
				return u;
			}else {
				throw new DataServiceException("User not found with given password and name " + u.getName());
			}
		}catch (SQLException e){ 
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	
	
	public User changePassword(String newPassword) throws DataServiceException {
		User u = getLoggedIn();
		if(newPassword == null){
			throw new DataServiceException("null value passed to UserService.changePassword.");
		}
		
		Connection conn = Database.getConnection();
		
		String password = encrypt(newPassword, 50);
		
		try{
			PreparedStatement statement = conn.prepareStatement("update " + Database.USERS + 
						" set password = ? where name = ? ;");
			statement.setString(1, password);
			statement.setString(2, u.getName());
			
			if(0 < statement.executeUpdate()){
				return u;
			}else{
				throw new DataServiceException("Error changing password for user " + u.getName());
			}
		}catch (SQLException e){ 
			e.printStackTrace();
			throw new DataServiceException("Error connecting to the database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

		

	//TODO javax.mail 
	public User register(User u) throws DataServiceException {
		if(u == null || u.getEmail() == null || u.getName() == null || u.getPassword() == null){
			throw new DataServiceException("Null value passed to UserService.register");
		}

		String password = encrypt(u.getPassword(), 50);
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + 
					" where name = ? ;");
			statement.setString(1, u.getName());
			
			ResultSet res = statement.executeQuery();
			
			if(res.next()){ //reached 1st entry - row for the specified user
				throw new DataServiceException("User with name " + u.getName() + " already exists.");
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
				u.setPassword(null);
				setLoggedIn(u);//todo send email instead
				return u;
			}else{
				throw new DataServiceException("Could not add new user to database.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}

	
	//TODO email notification?
	public void remove(User u)  throws DataServiceException {
		if(u== null || u.getName() == null){
			throw new DataServiceException("Null value was passed to UserService.remove");
		}

		Connection conn = Database.getConnection();

		try{ //delete group memberships, then delete user, leave groups/data that others might need.
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, u.getName());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User not found with name " + u.getName());
			}
			int id = res.getInt(1);
			
			if(0 < statement.executeUpdate("delete from " + Database.USERS + " where id = " + id + ";")){
				throw new DataServiceException("User " + u.getName() + " could not be removed from database.");
			}
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where member_id = " + id);
			statement.executeUpdate("delete from " + Database.WORKSPACE_USER_PERMISSIONS + " where user_id = " + id);
			statement.executeUpdate("delete from " + Database.SEQUENCE_USER_PERMISSIONS + " where user_id = " + id);			
		}catch (SQLException e){ 
			e.printStackTrace();
			throw new DataServiceException("Error connecting  to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e){e.printStackTrace();}
		}
	}


	
	
	public void addToGroup(String group, String userToAdd) throws DataServiceException {
		User u = getLoggedIn();
		if(group == null || userToAdd == null){
			throw new DataServiceException("A null value was passed to UserService.addToGroup.");
		}

		Connection conn = Database.getConnection();
		
		try{
			int ownerId = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;" );
			statement.setString(1, userToAdd);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("could not find user "+userToAdd+ " to add in database.");
			}
			int newId = res.getInt(1);

			statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? and owner_id = ?;");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("could not find group " + group + " with requesting owner " + u.getName());
			}
			int groupId = res.getInt(1);

			statement = conn.prepareStatement("select id from " + Database.GROUP_PERMISSIONS +
					" where group_id = ? and member_id = ?;");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User " + userToAdd + " is already a member of group " + group);
			}
			
			statement = conn.prepareStatement("insert into " + Database.GROUP_PERMISSIONS
					+ " (group_id, member_id) values (?, ?);");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Could not add user " + userToAdd + " to group " + group);
			}
		}catch (SQLException e){ 
			throw new DataServiceException("Error calling database to add user to group.");
		}finally{
			try{
				conn.close();
			}catch(SQLException e1){e1.printStackTrace();}
		}
	}


		

	public void createGroup(String group) throws DataServiceException {
		User u = getLoggedIn();
		if(group == null){
			throw new DataServiceException("Null value passed to UserService.createGroup");
		}
		
		Connection conn = Database.getConnection();
		
		try{
			int id = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ?");
			statement.setString(1, group);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				throw new DataServiceException("Group with name " + group + " already exists");
			}
			
			statement = conn.prepareStatement("insert into " + Database.GROUPS + " (group_name, owner_id) values (?,?);");
			statement.setString(1, group);
			statement.setInt(2, id);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Group " + group + " could not be added");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}


	

	public List<String> getAvailableGroups() throws DataServiceException {
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
			return available;
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}



	public List<String> getOwnedGroups() throws DataServiceException{
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
			return owned;
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}



	public List<User> getUsersWithAccessToGroup(String group) throws DataServiceException {
		ArrayList<User> users = new ArrayList<User>();
		
		if(group == null){
			throw new DataServiceException("Null value passed to UserService.getUsersWithAccessToGroup.");
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
			
			
			statement = conn.prepareStatement("select t2.name, t2.email from " + Database.GROUP_PERMISSIONS + 
					" as t1 left join " + Database.USERS + 
					" as t1 on t1.member_id = t2.id where t1.group_id = ?");
			statement.setInt(1, groupId);
			res = statement.executeQuery();
			while(res.next()){
				User u = new User();
				u.setName(res.getString(1));
				u.setEmail(res.getString(2));
				users.add(u);
			}
			return users;
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}




	public void removeFromGroup(String group, String userToRemove) throws DataServiceException {
		User u = getLoggedIn();
		
		if(userToRemove == null || group == null){
			throw new DataServiceException("Null value given to UserService.removeFromGroup");
		}

		if(u.getName() == userToRemove){
			throw new DataServiceException("Cannot remove owner without deleting group");
		}
		
		Connection conn = Database.getConnection();

		try{
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?");
			statement.setString(1, userToRemove);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("User to remove " + userToRemove + " not found in database.");
			}
			int toRemoveId = res.getInt(1);
			
			statement = conn.prepareStatement("select id from " + Database.GROUPS + " where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, u.getId());
			res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Group " + group + " not found with owner " + u.getName());
			}
			int groupId = res.getInt(1);
			
			statement = conn.prepareStatement("delete from " + Database.GROUP_PERMISSIONS + " where member_id = ? and group_id = ?");
			statement.setInt(1, toRemoveId);
			statement.setInt(2, groupId);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("User " + userToRemove + " was not a member of group " + group);
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}
	
	
	public void removeGroup(String group) throws DataServiceException{
		User u = getLoggedIn();
		if(group == null){
			throw new DataServiceException("Null value given to UserService.removeGroup.");
		}
		
		Connection conn = Database.getConnection();
		
		try{
			int ownerId = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + "where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Group " + group + " not found with owner " + u.getName());
			}
			int groupId = res.getInt(1);
			
			statement.executeUpdate("delete from " + Database.GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.WORKSPACE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.SEQUENCE_GROUP_PERMISSIONS + " where group_id = " + groupId);
			statement.executeUpdate("delete from " + Database.GROUPS + " where id = " + groupId);
			
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
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