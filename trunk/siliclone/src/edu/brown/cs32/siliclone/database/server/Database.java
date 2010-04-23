package edu.brown.cs32.siliclone.database.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


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
			  
			  PreparedStatement statement = conn.prepareStatement("create table if not exists users" +
			  		"(name varchar(60) not null unique primary key," +
			  		"email varchar(60) not null," +
			  		"password varchar(60) not null);");
			  statement.executeUpdate();
			  statement.close();

			  
			  return true;  
		  }catch (SQLException e){
			  System.out.println("Could not create table in database from " + conn);
			  return false;
		  }
	  }
	  
	  
	  
	  
	  
/**
	  public static long writeJavaObject(Connection conn, Object object) throws Exception {
	    String className = object.getClass().getName();
	    PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL);

	    // set input parameters
	    pstmt.setString(1, className);
	    pstmt.setObject(2, object);
	    pstmt.executeUpdate();

	    // get the generated key for the id
	    ResultSet rs = pstmt.getGeneratedKeys();
	    int id = -1;
	    if (rs.next()) {
	      id = rs.getInt(1);
	    }

	    rs.close();
	    pstmt.close();
	    System.out.println("writeJavaObject: done serializing: " + className);
	    return id;
	  }

	  public static Object readJavaObject(Connection conn, long id) throws Exception {
	    PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
	    pstmt.setLong(1, id);
	    ResultSet rs = pstmt.executeQuery();
	    rs.next();
	    Object object = rs.getObject(1);
	    String className = object.getClass().getName();

	    rs.close();
	    pstmt.close();
	    System.out.println("readJavaObject: done de-serializing: " + className);
	    return object;
	  }
	  
	  **/
	  
	  
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
