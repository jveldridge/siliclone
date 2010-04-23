package edu.brown.cs32.siliclone.database.server;





import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.UserService;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	public User changePassword(User u, String newPassword) {
		// TODO Auto-generated method stub
		if(!u.getValid() || newPassword == null || u.getName() == null){
			return u;
		}
		Statement statement = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			if(0 < statement.executeUpdate("update users set password = '"
					+ newPassword.hashCode() + "' where name = '" + u.getName() + "';")){
				u.setPassword(newPassword);
			}
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try {
				if(statement != null){ statement.close();}
			} catch (SQLException e) { e.printStackTrace(); }
		}
		return u;
	}

	
	
	
	
	public User login(User u) {
		// TODO Auto-generated method stub
		if(u.getName() == null || u.getPassword() == null){
			return u;
		}
		
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			res = statement.executeQuery(
					"select password from users where name = '" + u.getName() + "';");
			if(res.first()){ //reached 1st entry - row for the specified user
				String hashedPassword = res.getString(1); //starts indexing at 1 
				if(hashedPassword.equals("" + u.getPassword().hashCode())){
					u.setValid(true);
				}
			}
			
		}catch (Exception e){ e.printStackTrace();
		}finally{
			try {
				if(res != null){ res.close();} 
				if(statement != null){ statement.close(); }
			}catch (SQLException e) { e.printStackTrace(); }
		}
		return u;
	}

	
	
	
	
	
	public User register(User u) {
		if(u.getEmail() == null || u.getEmail() == null || u.getPassword() == null){
			return u;
		}

		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			res = statement.executeQuery(
					"select password from users where name = '" + u.getName() + "';");
			if(res.next()){ //reached 1st entry - row for the specified user
				res.close();
				statement.close();
				return u;
			}
			res.close();
			
			if(0 < statement.executeUpdate(
					"insert into users (name, email, password)" +
					" values ('" + u.getName() + "', '" + u.getEmail() +
					"', '" + u.getPassword().hashCode() + "');")){ //success
				u.setValid(true);
			}
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try {
				if(res != null) {res.close();}
				if(statement != null){statement.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return u;
	}

	
	public static void main(String[] args){
		UserServiceImpl i = new UserServiceImpl();
		User u = new User();
		u.setName("user2");
		u.setValid(false);
		u.setEmail("email@site.com");
		u.setPassword("siliclone");
		u = i.register(u);
		if(u.getValid()){
			System.out.println("successfully registered new user!");
		}
		
		u.setValid(false);
		u = i.login(u);
		if(u.getValid()){
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


	
	
	public User remove(User u) {
		if(u.getName() == null || !u.getValid()){
			return u;
		}

		Statement statement = null;
		try{ //delete group memberships, then delete user, leave groups/data that others might need.
			statement = Database.getSingleConnection().createStatement();
			statement.executeUpdate("delete from groups where user = '" + u.getName() + "';");
			
			if(0 < statement.executeUpdate(
					"delete from users where name = '" + u.getName() + "';")){
				u.setValid(false);
			}
		}catch (SQLException e){ e.printStackTrace();
		}finally{
			try{
				if(statement != null){ statement.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return u;
	}





	
	
	
	
	
	
	public String addToGroup(User u, String group, String userToAdd) {
		
		if(u.getName() == null || group == null || userToAdd == null){
			return "Null value given to addToGroup";
		}
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			res = statement.executeQuery(
					"select from groups where user = '" + u.getName() + 
					"' and group = '" + group + 
					"' and owner = true;");
			if(!res.next()){ //group was not found
				res.close();
				statement.close();
				return "No group '" + group + "' was found with owner " + u.getName();
			}
			res.close();
			
			
			res = statement.executeQuery("select from users where name = '" + userToAdd + "';");
			if(!res.next()){ //no user found
				res.close();
				statement.close();
				return "No user could be found with user name " + userToAdd;
			}
			res.close();
			
			
			res = statement.executeQuery(
					"select from groups where user = '" + userToAdd +
					"' and group = " + group + "';");
			if(res.next()){ //already member
				res.close();
				statement.close();
				return "User with name " + u.getName() + "is already a member of this group.";
			}
			res.close();
			
			if(0 < statement.executeUpdate(
					"inset into groups (user, group, owner) values ('" +
					userToAdd + "', '" + group + "', false);")){
				res.close();
				statement.close();
				return "User " + userToAdd + " successfully added to group " + group;
			}else {
				res.close();
				statement.close();
				return "Could not add user " + userToAdd + " to group " + group;
			}
		}catch (SQLException e){ 
			try{
				if(res != null){res.close();}
				if(statement != null){statement.close();}
			}catch(SQLException e1){e1.printStackTrace();}
			return "Error calling database to add user to group.";
		}
	}



	public String createGroup(User u, String group) {
		// TODO Auto-generated method stub
		if(!u.getValid() || u.getName() == null || group == null){
			return "Null value or invalid user passed to createGroup";
		}
		
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			res = statement.executeQuery(
					"select from groups where group = '" + group + "';");
			if(res.next()){
				res.close();
				statement.close();
				return "Group already exists with name + " + group;
			}
			res.close();
			
			if(0 < statement.executeUpdate("insert into groups (user, group, owner) " +
					"values ('" + u.getName() + "', '" + group +"', true);")){
				statement.close();
				return "Group " + group + " added with owner " + u.getName();
			}else {
				statement.close();
				return "Group " + group + " could not be added.";
			}
		}catch (SQLException e){
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e1){e1.printStackTrace();}
			return "There was an error when making the group.";
		}
	}






	public List<String> getAvailableGroups(User u) {
		ArrayList<String> available = new ArrayList<String>();
		if(u.getName() == null){
			return available;
		}
		
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			res = statement.executeQuery("Select group from groups where user = '" + 
					u.getName() + "';");
			while(res.next()){
				available.add(res.getString(1));
			}
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return available;
	}



	public List<String> getOwnedGroups(User u) {
		ArrayList<String> owned = new ArrayList<String>();
		if(u.getName() == null){
			return owned;
		}
		
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			res = statement.executeQuery("Select group from groups where user = '" + 
					u.getName() + "' and owner = true;");
			while(res.next()){
				owned.add(res.getString(1));
			}
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return owned;
	}






	public List<String> getUsersWithAccessToGroup(String group) {
		ArrayList<String> users = new ArrayList<String>();
		if(group == null){
			return users;
		}
		
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			res = statement.executeQuery("Select user from groups where group = '" + 
					group + "';");
			while(res.next()){
				users.add(res.getString(1));
			}
		}catch (SQLException e){e.printStackTrace();
		}finally {
			try{
				if(res != null){ res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e){e.printStackTrace();}
		}
		return users;
	}




	public String removeFromGroup(User u, String group, String userToRemove) {
		if(u.getName() == null || userToRemove == null || group == null){
			return "Null value given";
		}

		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();
			
			res = statement.executeQuery("select from groups where user = '" + 
					u.getName() + "' and owner = true and group = '" + group + "';");
			if(!res.next()){
				res.close();
				statement.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			res.close();
			
			statement.executeUpdate("delete from groups where user = '" + userToRemove + "'" +
					"and group = '" + group + "';");
			statement.close();
			return "User " + userToRemove + " removed from group " + group;
		}catch (SQLException e){
			try{
				if(res != null){res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e1){e1.printStackTrace();}
			return "Error connecting to database to remove user from group.";
		}	
	}
	
	
	public String removeGroup(User u, String group){
		if(u.getName() == null || group == null){
			return "Null value given.";
		}
		Statement statement = null;
		ResultSet res = null;
		try{
			statement = Database.getSingleConnection().createStatement();

			res = statement.executeQuery("select from groups where user = '" + 
					u.getName() + "' and owner = true and group = '" + group + "';");
			if(!res.next()){
				res.close();
				statement.close();
				return "Group " + group + " not found with owner " + u.getName();
			}
			res.close();
			
			statement.executeUpdate("delete from groups where group = '" + group + "';");
			statement.close();
			return "Group " + group + " removed.";
		}catch (SQLException e){
			try{
				if(res != null){res.close();}
				if(statement != null){ statement.close();}
			}catch (SQLException e1){e1.printStackTrace();}
			return "Error connecting to database to remove group.";
		}	
	}
}
