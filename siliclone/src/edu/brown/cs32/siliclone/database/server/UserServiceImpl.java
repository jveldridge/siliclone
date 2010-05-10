package edu.brown.cs32.siliclone.database.server;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;

/**
 * UserService is responsible for communicating with the database about user accounts and groups.
 * Errors are passed through the messages saved in DataServiceExceptions
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	
	/**
	 * Creates a string that is the beginning of a SHA-1 encryption
	 * @param word The word to encrypt (not null)
	 * @param maxLength The length of the desired encrypted string (the result may be shorter).
	 * @return The encrypted string, or null if something goes horribly wrong and SHA-1 encryption is not found.
	 */
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
	
	/**
	 * Returns the user object currently saved in the servlet's http session.
	 * This user object is saved with a call to setLoggedIn()
	 * @return The user object saved using setLoggedIn()
	 * @throws DataServiceException If no user was saved - gives message "User is no longer logged in." 
	 */
	//throws error if user not logged in.
	public User getLoggedIn() throws DataServiceException{
		return getLoggedIn(this.getThreadLocalRequest().getSession(true));
	}
	
	/**
	 * Sets the http session's recording of the user to null,
	 * so getLoggedIn will throw an exception until setLoggedIn is called again.
	 */
	public void logout(){
		this.getThreadLocalRequest().getSession().removeAttribute("user");
	}
	
	/**
	 * Sets the http session's current user to the given User object.
	 * @param u The current user, with username and email initialized. (not null)
	 */
	//updates session to save current user.
	private void setLoggedIn(User u){
		this.getThreadLocalRequest().getSession().setAttribute("user", u);
	}
	
	/**
	 * Returns the database integer index for the user with the given name.
	 * @param conn The database connection to use for contacting the database. (not null)
	 * @param name The name of the user to find  (not null)
	 * @return The index of the user in the database.
	 * @throws DataServiceException If the user could not be found: either
	 * 		 "User with name " + name + " not found in database." or	
	 * 			"Error communicating with database."
	 */
	private int findUserId(Connection conn, String name) throws DataServiceException{
		try {
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.USERS + 
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt(1);
			}else{
				throw new DataServiceException("User with name " + name + " not found in database.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataServiceException("Error communicating with database.");
		}
		
	}

	/**
	 * Logs in the user passed as a parameter. 
	 * @param u The User - must have initialized name and password. not null.
	 * @return user with password set to null, email initialized
	 * @throws DataServiceException if login unsuccessful :
	 * 		"Null value passed to UserService.login"
	 * 		"User not found with given password and name " + u.getName()
	 * 		"Error connecting to database."
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
	
	
	/**
	 * Changes the current user's password to the given string.
	 * @param newPassword The new password to set for the current user. not null
	 * @return the current user with name and email initialized. 
	 * @throws DataServiceException:"null value passed to UserService.changePassword."
	 * 			 "User is no longer logged in."
	 * 			"Error changing password for user " + u.getName()
	 * 			or "Error connecting to the database."
	 */
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

		

	//TODO javax.mail (or maybe not?)
	/**
	 * Creates a new user entry in the database, and sets that user to be logged in.
	 * @param u The user to be logged in with name, email, and password initialized. not null
	 * @return The user with password set to null.
	 * @throws DataServiceException: "Null value passed to UserService.register"
	 * 			"User with name " + u.getName() + " already exists."
	 * 			"Failed to register user"
	 * 			"Error connecting to database."
	 */
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
				statement = conn.prepareStatement("select id from " + Database.USERS + " where name = ?;"); //double check
				statement.setString(1, u.getName());  
				res= statement.executeQuery();  
				if(!res.next()){
					throw new DataServiceException("Failed to register user");
				}
				u.setId(res.getInt(1)); 
				u.setPassword(null);
				setLoggedIn(u); 
				return u;
			}else{
				throw new DataServiceException("Failed to register user");
			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e1) { e1.printStackTrace(); }
		}
	}

	
	/**
	 * Deletes a user account from the database, leaving saved workspaces,
	 * 	 sequences, and owned groups intact.
	 * The user must be currently logged in, and is logged out after being removed.
	 * @param u The user to be removed, with username initialized and equal to that of the current user.
	 * @throws DataServiceException: "Null value was passed to UserService.remove"
	 * 		"User is no longer logged in."
	 * 		"Error - the account to delete is different from the current user's account."
	 * 		"User " + u.getName() + " could not be removed from database."
	 * 		"Error connecting  to database." 
	 */
	public void remove(User u)  throws DataServiceException {
		if(u== null || u.getName() == null){
			throw new DataServiceException("Null value was passed to UserService.remove");
		}

		if(!getLoggedIn().getName().equals(u.getName())){
			throw new DataServiceException("Error - the account to delete is different from the current user's account.");
		}
		Connection conn = Database.getConnection();

		try{ //delete group memberships, then delete user, leave groups/data that others might need.
			int id = findUserId(conn, u.getName());
			Statement statement = conn.createStatement();
			if(0 < statement.executeUpdate("delete from " + Database.USERS + " where id = " + id + ";")){
				throw new DataServiceException("User " + u.getName() + " could not be removed from database.");
			}
			logout();
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


	
	/**
	 *  Adds the user with the given name as a member to the group with the given name.
	 *  @param group The name of an existing group, which is owned by the current user.
	 *  @param userToAdd The name of an existing user who is not currently a member.
	 *  @throws DataServiceException: "A null value was passed to UserService.addToGroup."
	 *  	"User is no longer logged in."
	 * 		"User with name " + userToAdd + " not found in database."
	 *		 "could not find group " + group + " with requesting owner "
	 *		"User " + userToAdd + " is already a member of group " + group
	 *		"Could not add user " + userToAdd + " to group " + group
	 * 		"Error connecting  to database."
	 */
	public void addToGroup(String group, String userToAdd) throws DataServiceException {
		if(group == null || userToAdd == null){
			throw new DataServiceException("A null value was passed to UserService.addToGroup.");
		}
		User u = getLoggedIn();

		Connection conn = Database.getConnection();
		
		try{
			int ownerId = u.getId();
			
			int newId = findUserId(conn, userToAdd);

			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS +
					" where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, ownerId);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("could not find group " + group + " with requesting owner " + u.getName());
			}
			int groupId = res.getInt(1);

			statement = conn.prepareStatement("select * from " + Database.GROUP_PERMISSIONS +
					" where group_id = ? and member_id = ?");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			res = statement.executeQuery();
			if(res.next()){
				throw new DataServiceException("User " + userToAdd + " is already a member of group " + group);
			}
			
			statement = conn.prepareStatement("insert into " + Database.GROUP_PERMISSIONS
					+ "(group_id, member_id) values (?, ?)");
			statement.setInt(1, groupId);
			statement.setInt(2, newId);
			if(0 >= statement.executeUpdate()){
				throw new DataServiceException("Could not add user " + userToAdd + " to group " + group);
			}
		}catch (SQLException e){ 
			e.printStackTrace();
			throw new DataServiceException("Error connecting  to database.");
		}finally{
			try{
				conn.close();
			}catch(SQLException e1){e1.printStackTrace();}
		}
	}


		
	/**
	 * Creates a new group with the given name and the currently logged in user as the owner.
	 * @param group The name of the new group to create (must be a unique name).
	 * @throws DataServiceException "Null value passed to UserService.createGroup"
	 *  	"User is no longer logged in."
	 *  	"Group with name " + group + " already exists"
	 *  	"Group " + group + " could not be added"
	 * 		"Error connecting to database."
	 */
	public void createGroup(String group) throws DataServiceException {
		if(group == null){
			throw new DataServiceException("Null value passed to UserService.createGroup");
		}
		User u = getLoggedIn();
		
		Connection conn = Database.getConnection();
		
		try{
			int id = u.getId();
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + " where group_name = ?");
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
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}


	
	/**
	 * Lists all groups that the current user either owns or is a member of.
	 * @return A list of group names for the available groups, which is empty if there are none.
	 * @throws DataServiceException "User is no longer logged in."
	 *  	or "Error connecting to database."
	 */
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


	/**
	 * Lists all groups that the current user owns.
	 * @return A list of group names for the owned groups, which is empty if there are none.
	 * @throws DataServiceException "User is no longer logged in."
	 *  	or "Error connecting to database."
	 */
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


	/**
	 * Lists all users that have access to the given group, including the owner.
	 * @param group The name of an existing group in the database.
	 * @return A list of all users who have access, which will at least have one entry for the owner.
	 * 			Or an empty list if the group is not found.
	 * @throws DataServiceException: "Null value passed to UserService.getUsersWithAccessToGroup."
	 * 			or "Error connecting to database."
	 */
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
					" as t2 on t1.member_id = t2.id where t1.group_id = ?");
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
	
	
	/**
	 * Lists all users in the database.
	 * @return A list of all users in the database, with name and email initialized.
	 * @throws DataServiceException : "Error connecting to the database."
	 */
	public List<User> getAllUsers() throws DataServiceException{
		ArrayList<User> users = new ArrayList<User>();
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " + 
					Database.USERS + ";");
			ResultSet res = statement.executeQuery();
			while(res.next()){
				users.add(new User(res.getString("name"), null, res.getString("email")));
			}
			return users;
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to the database.");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	


	/**
	 * Removes a member from a group, where that member is someone with permission but not the owner.
	 * @param group An existing group's name where the group is owned by the current user.
	 * @param userToRemove The name of an existing user who is a member of the group.
	 * @throws DataServiceException : "Null value given to UserService.removeFromGroup"
	 *  	"User is no longer logged in."
	 * 		"Cannot remove owner without deleting group"
	 * 		"Group " + group + " not found with owner " + u.getName()
	 * 		"User " + userToRemove + " was not a member of group " + group
	 * 		or "Error connecting to database."
	 */
	public void removeFromGroup(String group, String userToRemove) throws DataServiceException {
		if(userToRemove == null || group == null){
			throw new DataServiceException("Null value given to UserService.removeFromGroup");
		}
		
		User u = getLoggedIn();
		
		
		if(u.getName().equals(userToRemove)){
			throw new DataServiceException("Cannot remove owner without deleting group");
		}
		
		Connection conn = Database.getConnection();

		try{
			int toRemoveId = findUserId(conn, userToRemove);
			
			PreparedStatement statement = conn.prepareStatement("select id from " + Database.GROUPS + " where group_name = ? and owner_id = ?");
			statement.setString(1, group);
			statement.setInt(2, u.getId());
			ResultSet res = statement.executeQuery();
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
	
	/**
	 * Deletes a group and all associated permissions from the databases.
	 * @param group The name of a valid group which is owned by the curren user.
	 * @throws DataServiceException "Null value given to UserService.removeGroup."
	 * 		"User is no longer logged in."
	 * 		"Group " + group + " not found with owner " + u.getName()
	 * 		"Error connecting to database."
	 */
	public void removeGroup(String group) throws DataServiceException{
		if(group == null){
			throw new DataServiceException("Null value given to UserService.removeGroup.");
		}
		User u = getLoggedIn();
		
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
	
	
	
	
	
	
	/**
	 * Checks if the user saved in the http session has access to the sequence data given by the hook
	 * @param session The current session, created by the httpservlet called via rpc. not null
	 * @param seq The sequencehook pointing to the data in question.
	 * @throws DataServiceException: "User is no longer logged in."
	 * 		"User does not have permission to use requested sequence."
	 * 		"Error connecting to database."
	 */
	public static void verifyAccess(HttpSession session, SequenceHook seq) throws DataServiceException {
		/*User u = UserServiceImpl.getLoggedIn(session);
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCE_GROUP_PERMISSIONS + " as t1 left join " + 
					Database.GROUP_PERMISSIONS + " as t2 on t1.group_id = t2.group_id " +
					"where t2.member_id = ? and t1.data_id = ?;");
			statement.setInt(1, u.getId());
			statement.setInt(2, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				statement = conn.prepareStatement("select * from " + 
						Database.SEQUENCE_USER_PERMISSIONS + " where data_id = ? and user_id = ? ");
				statement.setInt(1, seq.getDataID());
				statement.setInt(2, u.getId());
				res = statement.executeQuery();
				if(!res.next()){
					throw new DataServiceException("User does not have permission to use requested sequence.");
				}
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}*/
	}
	
	
	/**
	 * Returns the user object currently saved in the given http session.
	 * This user object is saved with a call to setLoggedIn()
	 * @param session The current session, created by the httpservlet called via rpc. not null
	 * @return The user object saved using setLoggedIn()
	 * @throws DataServiceException If no user was saved - gives message "User is no longer logged in." 
	 */
	public static User getLoggedIn(HttpSession session) throws DataServiceException{
		User u = (User) session.getAttribute("user");
		if(u == null){
			throw new DataServiceException("User is no longer logged in.");
		}
		return u;
	}

}
