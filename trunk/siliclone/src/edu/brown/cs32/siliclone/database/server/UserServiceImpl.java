package edu.brown.cs32.siliclone.database.server;





import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		
		try{
			PreparedStatement statement = Database.getSingleConnection().prepareStatement(
					"update users set password = '" + newPassword.hashCode() + 
					"' where name = '" + u.getName() + "';");
			if(0 < statement.executeUpdate()){
				u.setPassword(newPassword);
			}
			
		}catch (SQLException e){
			return u;
		}
		
		return u;
	}

	
	
	
	
	
	public User login(User u) {
		// TODO Auto-generated method stub
		if(u.getName() == null || u.getPassword() == null){
			return u;
		}
		
		try{
			PreparedStatement statement = Database.getSingleConnection().prepareStatement(
					"select password from users where name = '" + u.getName() + "';");
			ResultSet res = statement.executeQuery();
			if(res.first()){ //reached 1st entry - row for the specified user
				String hashedPassword = res.getString(1); //starts indexing at 1 
				if(hashedPassword.equals("" + u.getPassword().hashCode())){
					u.setValid(true);
				}
			}
			
			res.close();
			statement.close();
		}catch (Exception e){
			return u;
		}		
		return u;
	}

	
	
	
	
	
	public User register(User u) {
		if(u.getEmail() == null || u.getEmail() == null || u.getPassword() == null){
			return u;
		}
		Connection conn = Database.getSingleConnection();
		try{
			PreparedStatement statement = conn.prepareStatement(
					"select password from users where name = '" + u.getName() + "';");
			ResultSet res = statement.executeQuery();
			if(res.next()){ //reached 1st entry - row for the specified user
				res.close();
				statement.close();
				return u;
			}
			
			statement = conn.prepareStatement("insert into users (name, email, password)" +
					" values ('" + u.getName() + "', '" + u.getEmail() +
					"', '" + u.getPassword().hashCode() + "');");
			if(0 < statement.executeUpdate()){ //success
				u.setValid(true);
			}
			statement.close();
		}catch (SQLException e){
			return u;
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
	}
}
