package edu.brown.cs32.siliclone.database.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * note - source http://www.java2s.com/Code/Java/Database-SQL-JDBC/HowtoserializedeserializeaJavaobjecttotheMySQLdatabase.htm
 * maybe later incorporate tomcat connectors.
 */
public class Database {
	/**
	 * Prepared statements use generic strings:
	 * http://java.sun.com/j2se/1.4.2/docs/api/java/sql/PreparedStatement.html
	 */
	  
	private static final Connection singleConnection = getConnection();
	public static Connection getSingleConnection(){
		return singleConnection;
	}
	  
	public static Connection getConnection(){
	    String driver = "org.gjt.mm.mysql.Driver"; //must be in class path
	    String url = "jdbc:mysql://everett-77.resnet.brown.edu/siliclone"; //must be running from dmhall's room
	    String username = "javauser";
	    String password = "siliclone";
	    try {
	    	Class.forName(driver);
	    }catch (ClassNotFoundException e){
	    	System.out.println("could not load mysql connection driver");
	    	return null;
	    }
	    try {
	    	Connection conn = DriverManager.getConnection(url, username, password);
	    	if(conn == null){
	    		System.out.println("Could not connect to database at " + username);
	    		return null;
	    	}
	    	if(!validDB(conn)){
	    		return null;
	    	}
		    return conn;
	    } catch (SQLException e){
	    	System.out.println("Could not connect to database at " + username);
	    	return null;
	    }
	  }
	  
	  
	  
	  
	  private static boolean validDB(Connection conn){
		  try{
			  Statement statement = conn.createStatement();
			  
			  statement.executeUpdate("create table if not exists users" +
				  		"(name varchar(60) not null unique primary key," +
				  		"email varchar(60) not null," +
				  		"password varchar(60) not null);");
			
			  statement.executeUpdate("create table if not exists groups" +
				  		"(user varchar(60) not null," +
				  		"groupname varchar(60) not null," +
				  		"owner bool not null);");
			  
			  statement.close();

			  
			  return true;  
		  }catch (SQLException e){
			  System.out.println("Could not create table in database from " + conn);
			  return false;
		  }
	  }
	  
	  
	  
	  public static void main(String args[])throws Exception {
	    Connection conn = null;
	    try {
	      conn = getConnection();
	      if(conn != null){
	    	  System.out.println("conn=" + conn);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    	if(conn != null){
	    		conn.close();
	    	}
	    }
	  }
	
}
