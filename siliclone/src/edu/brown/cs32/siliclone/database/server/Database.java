package edu.brown.cs32.siliclone.database.server;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.brown.cs32.siliclone.database.client.DataServiceException;


/**
 * The Database class is responsible for defining how the application server connects to the database server.
 *  It produces a connection which is handled using java.sql statements for mysql.
 *  It also defines constants for the table names, and static methods for reading/writing objects to the database.
 */
public class Database {
	
	// TODO - load these strings from a .config? using java.util.properties?
	private static final String DRIVER = "org.gjt.mm.mysql.Driver"; //must be in class path
	private static final String URL = "jdbc:mysql://mysql.vxcv.com/cs032002";
	private static final String USERNAME = "cs032002";
	private static final String PASSWORD = "indy123";
	
	static{
		try {
			Class.forName(DRIVER); 
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load mysql driver.");
			e.printStackTrace();
		}
	}
	
	
	//table names
	public static final String USERS = "users";
	public static final String GROUPS = "groups";
	public static final String GROUP_PERMISSIONS = "group_permissions";
	public static final String WORKSPACES = "workspaces";
	public static final String WORKSPACE_GROUP_PERMISSIONS = "workspace_group_permissions";
	public static final String WORKSPACE_USER_PERMISSIONS = "workspace_user_permissions";
	public static final String SEQUENCES = "sequences";
	public static final String SEQUENCE_DATA = "sequence_data";
	public static final String SEQUENCE_GROUP_PERMISSIONS = "sequence_group_permissions";
	public static final String SEQUENCE_USER_PERMISSIONS = "sequence_user_permissions";
	
	
	/**
	 * Creates a connection to the server's mysql database. This 
	 * connection should be closed when it is not in use. 
	 * The database used and its location are specified in the Database class' class fields.
	 * 
	 * @return The server's database connection 
	 * @throws DataServiceException If there was an error when connecting.(prints errors to stdio)
	 * 
	 */
	public static Connection getConnection() throws DataServiceException{
		try {
	    	Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	return conn;
	    } catch (SQLException e){
	    	System.err.println("Could not connect to database at " + URL);
	    	e.printStackTrace();
	    	throw new DataServiceException("Error connecting to the database.");
	    }
	}
	  
	
	
	
	
	
	/**
	 * Tries to create the tables used by this program in the given connection's database.
	 * This is meant for development convenience - not a product feature.
	 * @param conn Valid connection to db, retrieved from getConnection().
	 * @return True if connected and had permissions, false otherwise. Does not
	 * 		 guarantee success (if tables exists with different number of columns
	 */
	public static boolean initializeDB(Connection conn){
		//TODO not up to date?
		try{
			Statement statement = conn.createStatement();
			  
			statement.executeUpdate("create table if not exists " +
					USERS +
					"(id mediumint not null primary key auto_increment, " +
					"name varchar(60) not null, " +
					"email varchar(60) not null, " +
					"password varchar(60) not null);");
			
			statement.executeUpdate("create table if not exists " +
					GROUPS +
					"(id mediumint not null primary key auto_increment, " +
					"group_name varchar(60) not null, " +
					"owner_id mediumint not null);");
			statement.executeUpdate("create table if not exists " +
					GROUP_PERMISSIONS +
					"(group_id mediumint not null, " +
					"member_id mediumint not null);");
			
			statement.executeUpdate("create table if not exists " +
					WORKSPACES +
					"(id mediumint not null primary key auto_increment, " +
					"name varchar(60) not null, " +
					"data longblob not null," +
					"owner mediumint not null);");
			statement.executeUpdate("create table if not exists " +
					WORKSPACE_GROUP_PERMISSIONS + 
					"(workspace_id mediumint not null, " +
					"group_id mediumint not null);");
			statement.executeUpdate("create table if not exists " +
					WORKSPACE_USER_PERMISSIONS + 
					"(workspace_id mediumint not null, " +
					"user_id mediumint not null);");
			
			statement.executeUpdate("create table if not exists " +
					SEQUENCES + 
					"(id mediumint not null primary key auto_increment, " +
					"data longblob not null);");
			statement.executeUpdate("create table if not exists " + 
					SEQUENCE_DATA + 
					"(id mediumint not null primary key auto_increment, " +
					"name varchar(60) not null, " + 
					"seq_id mediumint not null, " +
					"features longblob not null, " +
					"properties longblob not null);");
			statement.executeUpdate("create table if not exists " +
					SEQUENCE_GROUP_PERMISSIONS +
					"(data_id mediumint not null, " +
					"group_id mediumint not null);");
			statement.executeUpdate("create table if not exists " +
					SEQUENCE_USER_PERMISSIONS + 
					"(data_id mediumint not null, " +
					"user_id mediumint not null); ");
			  
			return true;  
		}catch (SQLException e){
			e.printStackTrace();
			System.out.println("Could not create table in database from " + conn);
			return false;
		} finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Convenience method to run initializeDB on getConnection 
	 * @param argv
	 */
	public static void main(String[] argv) {
		try{
			Database.initializeDB(getConnection());
		}catch(DataServiceException e){
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Wraps the given output stream in a compression stream.
	 * @param os An output stream to modify by the given compression method. (not null)
	 * @return The output stream, which now compresses before writing.
	 * @throws IOException If there was an error in constructing the compression stream. 
	 * @see GZIPOutputStream
	 */
	private static OutputStream getCompressedOutputStream(OutputStream os) throws IOException{
		//return new GZIPOutputStream(os);
		return os;
	}
	
	/**
	 * Wraps input stream in a decompression stream
	 * @param is An input stream to modify. (not null)
	 * @return The same input stream wrapped in a decompression stream.
	 * @throws IOException If there was an error constructing the stream.
	 * @see GZIPInputStream
	 */
	private static InputStream getCompressedInputStream(InputStream is) throws IOException{
		//return new GZIPInputStream(is);
		return is;
	}
	
	/**
	 * Given prepared statement for insertion, sets object data to compressed value at given index.
	 * @param statement A PreparedStatement that specifies where the data update or insertion will be in the database. (not null)
	 * @param columnindex The index of the data in the prepared statement (which '?'), starting at index 1.
	 * @param objectToWrite The serializable object (not null) that is to be compressed and written.
	 * @throws IOException If there is an error during compression.
	 * @throws SQLException If there is an error in the statement syntax or database connection.
	 */
	public static void saveCompressedObject(PreparedStatement statement, int columnindex, Object objectToWrite) throws IOException, SQLException{
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(getCompressedOutputStream(baout));
		oos.writeObject(objectToWrite);
		oos.close();
		statement.setBinaryStream(columnindex,new ByteArrayInputStream(baout.toByteArray()));	
		
	}
	
	/**
	 * The same as saveCompressed object with a PreparedStatement, except this updates the stream of the resultset created from that statement.
	 * @param resultset The resultset generated from a valid insertion or update statement. (not null)
	 * @param columnindex The index of the data in the statement. (>= 1)
	 * @param objectToWrite The serializable object (not null) that is to be compressed and written. (not null)
	 * @throws IOException If there is an error during compression.
	 * @throws SQLException If there is an error in the statement syntax or database connection.
	 */
	public static void saveCompressedObject(ResultSet resultset, int columnindex, Object objectToWrite) throws IOException, SQLException{
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(getCompressedOutputStream(baout));
		oos.writeObject(objectToWrite);
		oos.close();
		resultset.updateBinaryStream(columnindex,new ByteArrayInputStream(baout.toByteArray()));	
		
	}
	
	/**
	 * Reads compressed data from a blob object retrieved from the database.
	 * @param b The blob object retrieved from ResultSet.getBlob (not null)
	 * @return The object, decompressed and deserialized.
	 * @throws IOException If there was an error during decompression.
	 * @throws SQLException If there was an error deserializing from the blob.
	 * @throws ClassNotFoundException If there was an error deserializing from the blob.
	 */
	public static Object loadCompressedObject(Blob b) throws IOException, SQLException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes(1, (int) b.length()));
		ObjectInputStream ois = new ObjectInputStream(getCompressedInputStream(bis));
		return ois.readObject();
	}
	
}
