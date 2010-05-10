package edu.brown.cs32.siliclone.database.server;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

//import sun.misc.Cache;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.server.TasksDelegation;

/**
 * SequenceService is responsible for communicating with the database about sequence data. 
 */
@SuppressWarnings("serial")
public class SequenceServiceImpl extends RemoteServiceServlet implements SequenceService{
	
	/**
	 * Adds a feature to the sequence data indicated by the sequence hook,
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param Feature The feature to add. (not null)
	 * @throws DataServiceException "Null value passed to SequenceService.addFeature"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence features could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	@SuppressWarnings("unchecked")
	public void addFeature(SequenceHook seq, Feature toAdd)
			throws DataServiceException {
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		if(seq == null || toAdd == null){
			throw new DataServiceException("Null value passed to SequenceService.addFeature");
		}
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select features from " + 
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Map<String, Collection<Feature>> features = 
				(Map<String, Collection<Feature>>) Database.loadCompressedObject(res.getBlob(1));
			if(features.containsKey(toAdd.getType())){
				features.get(toAdd.getType()).add(toAdd);
			}else{
				ArrayList<Feature> featureSet = new ArrayList<Feature>();
				featureSet.add(toAdd);
				features.put(toAdd.getType(), featureSet);
			}
			statement = conn.prepareStatement("update " + Database.SEQUENCE_DATA + 
					" set data = ? where id = ?");
			statement.setInt(2, seq.getDataID());
			Database.saveCompressedObject(statement, 1, features);
			if (0 >= statement.executeUpdate()){
				throw new DataServiceException("Sequence features could not be saved.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}catch (IOException e){
			throw new DataServiceException("Error reading data.");
		}catch (ClassNotFoundException e){
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Adds a property to the sequence data indicated by the hook, 
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param key The key to which the given property is mapped - the property name. (not null)
	 * @param value The value of the property. (not null)
	 * @param session The current session, from which the current user is read.
	 * @throws DataServiceException "Null value passed to SequenceService.addProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence already contains property with given key."
	 * 			"Sequence properties could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	@SuppressWarnings("unchecked")
	public static void addProperty(SequenceHook seq, String key, Object value, HttpSession session)
			throws DataServiceException {
		if(seq == null || key == null || value == null){
			throw new DataServiceException("Null value passed to SequenceService.addProperty");
		}
		UserServiceImpl.verifyAccess(session, seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " + 
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Map<String, Object> properties = (Map<String, Object>)
							Database.loadCompressedObject(res.getBlob(1));
			if(properties.containsKey(key)){
				throw new DataServiceException("Sequence already contains property with given key.");
			}

			properties.put(key, value);

			statement = conn.prepareStatement("update " + Database.SEQUENCE_DATA + 
					" set properties = ? where id = ?");
			statement.setInt(2, seq.getDataID());
			Database.saveCompressedObject(statement, 1, properties);
			if (0 >= statement.executeUpdate()){
				throw new DataServiceException("Sequence properties could not be saved.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}catch (IOException e){
			throw new DataServiceException("Error reading data.");
		}catch (ClassNotFoundException e){
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	/**
	 * Gives a collection of features of a given sequence's data, of a given type - 
	 * if the current user has permission to view this data.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param featureType The type description to which the features are mapped to. 
	 * 			(from Feature.getType;  not null)
	 * @throws DataServiceException "Null value passed to SequenceService.getFeaturesOfType"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	@SuppressWarnings("unchecked")
	public Collection<Feature> getFeaturesOfType(SequenceHook seq,
			String featureType) throws DataServiceException {
		if(seq == null || featureType == null){
			throw new DataServiceException("Null value passed to SequenceService.getFeaturesOfType");
		}
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select features from " +
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			
			return ((Map<String, Collection<Feature>>) 
					Database.loadCompressedObject(res.getBlob(1))).get(featureType);
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	

	/**
	 * Gives a map of all properties saved to the specified sequence data, 
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param session The session from which the current user is found.
	 * @return A map of property names to the property values.
	 * @throws DataServiceException"Null value passed to SequenceService.getProperties"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public Map<String,Object> getProperties(SequenceHook seq,  HttpSession session) throws DataServiceException {
		UserServiceImpl.verifyAccess(session, seq);
		return getProperties(seq, this.getThreadLocalRequest().getSession(true));
	}
		

	/**
	 * Gives a map of all properties saved to the specified sequence data, 
	 * Does not check user permissions.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @return A map of property names to the property values.
	 * @throws DataServiceException"Null value passed to SequenceService.getProperties"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getProperties(SequenceHook seq) throws DataServiceException {
		if(seq == null){
			throw new DataServiceException("Null value passed to SequenceService.getProperties");
		}
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " +
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			return ((Map<String, Object>) Database.loadCompressedObject(res.getBlob(1)));
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	/**
	 * Gives a single property value mapped to the given key for the specified sequence,
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param key The property name (not null)
	 * @param session The session from which the current user is found. 
	 * @return A map of property names to the property values.
	 * @throws DataServiceException "Null value passed to SequenceService.getProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence property not found with key " + key
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public static Object getProperty(SequenceHook seq, String key, HttpSession session)
			throws DataServiceException {
		if(seq == null || key == null){
			throw new DataServiceException("Null value passed to SequenceService.getProperty");
		}
		IsSerializable property = (IsSerializable) getProperties(seq).get(key);
		if(property == null){
			throw new DataServiceException("Sequence property not found with key " + key);
		}
		return property;
		
	}

	//public Cache nucleotideStringCache; //TODO make cache

	/**
	 * Gives the sequence nucleotide string pointed to by the sequence hook.
	 * Does not check any permissions.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @return The nucleotide string specified by the hook.
	 * @throws DataServiceException "Null value passed to SequenceService.getSequence"
	 * 		"Sequence could not be found in the database."
	 * 		"Error reading data."
	 * 		"Error connecting to database."
	 */
	public static NucleotideString getSequence(SequenceHook seq) throws DataServiceException {
		if(seq == null){
			throw new DataServiceException("Null value passed to SequenceService.getSequence");
		}
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select data from " +
					Database.SEQUENCES + " where id = ?");
			statement.setInt(1, seq.getSeqID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
			//	System.out.println("were here now "+statement.toString()+" e "+ res.toString());
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Blob b = res.getBlob(1);
			NucleotideString ns =(NucleotideString) Database.loadCompressedObject(b);
			System.out.println("sequence:"+ns);
			return ns;
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	/**
	 * Gives the sequence nucleotide string pointed to by the sequence hook.
	 * Checks if the current user has permission to access the given nucleotide sequence.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param session The session from which the current user is found.
	 * @return The nucleotide string specified by the hook.
	 * @throws DataServiceException "Null value passed to SequenceService.getSequence"
	 * 		"Sequence could not be found in the database."
	 * 		"Error reading data."
	 * 		"Error connecting to database."
	 */
	public static NucleotideString getSequence(SequenceHook seq, HttpSession session)
			throws DataServiceException {
		if(seq == null){
			throw new DataServiceException("Null value passed to SequenceService.getSequence");
		}
		UserServiceImpl.verifyAccess(session, seq);
		
		return SequenceServiceImpl.getSequence(seq);
	}
	
	/**
	 * Gives the sequence nucleotide string pointed to by the sequence hook.
	 * Checks if the current user has permission to access the given nucleotide sequence.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @return The nucleotide string's string value specified by the hook.
	 * @throws DataServiceException "Null value passed to SequenceService.getSequence"
	 * 		"Sequence could not be found in the database."
	 * 		"Error reading data."
	 * 		"Error connecting to database."
	 */
	public String getNucleotides(SequenceHook seq) throws DataServiceException {
		NucleotideString nuc = SequenceServiceImpl.getSequence(seq, this.getThreadLocalRequest().getSession());
		return nuc.getDisplayString();
	}


	/**
	 * Gives the sequence nucleotide string's length, pointed to by the sequence hook.
	 * Checks if the current user has permission to access the given nucleotide sequence.
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @return The nucleotide string's length specified by the hook.
	 * @throws DataServiceException "Null value passed to SequenceService.getSequence"
	 * 		"Sequence could not be found in the database."
	 * 		"Error reading data."
	 * 		"Error connecting to database."
	 */
	public int length(SequenceHook seq) throws DataServiceException {
		return SequenceServiceImpl.getSequence(seq, this.getThreadLocalRequest().getSession()).getLength();
	}
	
	/**
	 * Saves the given NucleotideString, first making sure that it has not been saved before
	 * (no other NucleotideString that is equal to the given one). If it already saved, simply
	 * returns the index of the existing data.
	 * @param nucleotides The NucleotideString to save (not null)
	 * @param conn The connection to be used to communicate with the database.
	 * @return The index of the sequence saved in the database.
	 * @throws DataServiceException "Error reading NucleotideString."
	 * 			"Error writing sequence to database."
	 * 			"Error connecting to database."
	 */
	private static int saveNucleotideString(NucleotideString nucleotides, Connection conn)
				throws DataServiceException{
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCES + " where hash = ?", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, nucleotides.hashCode());
			System.out.println("hashcode: "+nucleotides.hashCode());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				Blob b2 = res.getBlob(2);
				NucleotideString ns = (NucleotideString) Database.loadCompressedObject(b2);
				
				if(nucleotides.equals(ns)){
					return res.getInt(1);
				}
			}
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCES + 
					" (data,indexDepth,hash) values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
			Database.saveCompressedObject(statement, 1, nucleotides);
			statement.setInt(2,nucleotides.getIndexDepth());
			statement.setInt(3,nucleotides.hashCode());
			statement.executeUpdate();
			
			res = statement.getGeneratedKeys();
	
			if(!res.next()){
				throw new DataServiceException("Error writing sequence to database.");
			}
			
			return res.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading NucleotideString.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataServiceException("Error reading NucleotideString.");
		} 
	}
	
	
	
	/**
	 * Saves the given data, returning a sequence hook to refer to its location in the database.
	 * Does not grant permission to any users.
	 * @param nucleotides The underlying sequence data to use. not null
	 * @param features The map of feature types to collections of sequence features to save. not null
	 * @param seqName The name of the sequence data, not null
	 * @param properties The map of sequence property names to property values.
	 * @return The hook referring to the data's location in the database.
	 * @throws DataServiceException "Null value passed to SequenceService.saveSequence"
	 * 		"Sequence data with name " + seqName + " already exists"
	 * 		"Error saving sequence data to database."
	 * 		"Error connecting to database."
	 */
	public static SequenceHook saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, Object> properties) throws DataServiceException {
		if(nucleotides == null || features == null || seqName == null || properties == null){
			throw new DataServiceException("Null value passed to SequenceService.saveSequence");
		}
			
		Connection conn = Database.getConnection();
			
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCE_DATA + " where name = ?", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, seqName);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				throw new DataServiceException("Sequence data with name " + seqName + " already exists");
			}
			
			int seqID = saveNucleotideString(nucleotides, conn);
			
				//notify task that it can start working now
			TasksDelegation.delegate(new IndexNucleotideSequenceTask(seqID), null); 
			
			
			//saving the sequence data
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCE_DATA + 
				" (name, seq_id, features, properties) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, seqName);
			statement.setInt(2, seqID);
			Database.saveCompressedObject(statement, 3, features);
			Database.saveCompressedObject(statement, 4, properties);
			statement.executeUpdate();
			
			res = statement.getGeneratedKeys();
			if(!res.next()){
				throw new DataServiceException("Error saving sequence data to database.");
			}
			int dataID = res.getInt(1);
			
			System.out.println("returning" + dataID);
			
			
			return new SequenceHook(dataID, seqID, seqName);
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error saving sequence data to database.");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	   
	
	
	
	/**
	 * Saves the given data, returning a sequence hook to refer to its location in the database.
	 * Granting permission to read to the current user.
	 * @param nucleotides The underlying sequence data to use. not null
	 * @param features The map of feature types to collections of sequence features to save. not null
	 * @param seqName The name of the sequence data, not null
	 * @param properties The map of sequence property names to property values.
	 * @param session The https session from which the current user is read.
	 * @return The hook referring to the data's location in the database.
	 * @throws DataServiceException "Null value passed to SequenceService.saveSequence"
	 * 		"Sequence data with name " + seqName + " already exists"
	 * 		"Error saving sequence data to database."
	 * 		"Error granting user permission to saved sequence"
	 * 		"Error connecting to database."
	 */
	public static SequenceHook saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, Object> properties, HttpSession session) throws DataServiceException {
		if(nucleotides == null || features == null || seqName == null || properties == null){
			throw new DataServiceException("Null value passed to SequenceService.saveSequence");
		}
		
		SequenceHook seq = saveSequence(nucleotides, features, seqName, properties);
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("insert into " + Database.SEQUENCE_USER_PERMISSIONS + " (data_id, user_id) values (?, ?)");
			statement.setInt(1, seq.getDataID());
			statement.setInt(2, UserServiceImpl.getLoggedIn(session).getId());
			if (0 >= statement.executeUpdate()) {
				throw new DataServiceException("Error granting user permission to saved sequence");
			}
			
			return seq;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	

	/**
	 * Saves the given data, returning a sequence hook to refer to its location in the database.
	 * Granting permission to read to the current user.
	 * @param nucleotides The underlying sequence data to use as a string. not null
	 * @param features The map of feature types to collections of sequence features to save. not null
	 * @param seqName The name of the sequence data, not null
	 * @param properties The map of sequence property names to property values.
	 * @param session The https session from which the current user is read.
	 * @return The hook referring to the data's location in the database.
	 * @throws DataServiceException "Null value passed to SequenceService.saveSequence"
	 * 		"Sequence data with name " + seqName + " already exists"
	 * 		"Error saving sequence data to database."
	 * 		"Error granting user permission to saved sequence"
	 * 		"Error connecting to database."
	 */
	
	public static SequenceHook saveSequence(String nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, Object> properties, HttpSession session) throws DataServiceException {
		NucleotideString seq = new NucleotideString(nucleotides);
		return SequenceServiceImpl.saveSequence(seq, features, seqName, properties, session);
	}
	
	/**
	 * Saves the given data, returning a sequence hook to refer to its location in the database.
	 * Granting permission to read to the current user.
	 * @param nucleotides The underlying sequence data to use. not null
	 * @param seqName The name of the sequence data, not null
	 * @return The hook referring to the data's location in the database.
	 * @throws DataServiceException "Null value passed to SequenceService.saveSequence"
	 * 		"Sequence data with name " + seqName + " already exists"
	 * 		"Error saving sequence data to database."
	 * 		"Error granting user permission to saved sequence"
	 * 		"Error connecting to database."
	 */
	public SequenceHook saveSequence(String nucleotides, String seqName)
			throws DataServiceException {
		//System.out.println("Save sequence");
		return SequenceServiceImpl.saveSequence(nucleotides, seqName, this.getThreadLocalRequest().getSession());
	}
	
	/**
	 * Saves the given data, returning a sequence hook to refer to its location in the database.
	 * Granting permission to read to the current user.
	 * @param nucleotides The underlying sequence data to use. not null
	 * @param seqName The name of the sequence data, not null
	 * @param session The https session from which the current user is read.
	 * @return The hook referring to the data's location in the database.
	 * @throws DataServiceException "Null value passed to SequenceService.saveSequence"
	 * 		"Sequence data with name " + seqName + " already exists"
	 * 		"Error saving sequence data to database."
	 * 		"Error granting user permission to saved sequence"
	 * 		"Error connecting to database."
	 */
	public static SequenceHook saveSequence(String nucleotides, String seqName, HttpSession session)
	throws DataServiceException {
		System.out.println("now in method 2");
		return SequenceServiceImpl.saveSequence(nucleotides, new HashMap<String, Collection<Feature>>(), 
		seqName, new HashMap<String, Object>(), session);
	}
	
	
	
	/**
	 * Gives the hook referring to the sequence data of the given name,
	 * if the current user has permission to view this data.
	 * @param name The name of the exisiting sequence that the user has access to.
	 * @throws DataServiceException  "null value was passed to SequenceService.findSequence"
	 * 			"Sequence data with name " + name + " not found."
	 * 			"Sequence could not be found in the database."
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Error communicating with database."
	 */
	public SequenceHook findSequence(String name) throws DataServiceException {
		SequenceHook seq = findDNASequence(name);
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		return seq;
	}
		
	
	
	
	/**
	 * Gives the hook referring to the sequence data of the given name,
	 * Does not check user permissions
	 * @param name The name of the exisiting sequence that the user has access to.
	 * @throws DataServiceException  "null value was passed to SequenceService.findSequence"
	 * 			"Sequence data with name " + name + " not found."
	 * 			"Sequence could not be found in the database."
	 * 			"Error communicating with database."
	 */
	public static SequenceHook findDNASequence(String name) throws DataServiceException {
		if(name == null){
			throw new DataServiceException("null value was passed to SequenceService.findSequence");
		}
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select id, seq_id from " + Database.SEQUENCE_DATA + 
					" where name = ?");
			statement.setString(1, name);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence data with name " + name + " not found.");
			}
			int dataID = res.getInt(1);
			int seqID = res.getInt(2);
			SequenceHook seq = new SequenceHook(dataID, seqID, name);
			conn.close();
			return seq;
		}catch(SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error communicating with database.");
		}
	}
	
	
	
	/**
	 * Gives a list of sequence data names for all sequences that the current user has 
	 * permission to access.
	 * @return The list of available sequence data, empty if none are found.
	 * @throws DataServiceException "Error connecting to database."
	 */
	public List<String> listAvailableSequences() throws DataServiceException {
		User u = UserServiceImpl.getLoggedIn(this.getThreadLocalRequest().getSession());
		Connection conn = Database.getConnection();
		ArrayList<String> available = new ArrayList<String>();
		try{
			PreparedStatement statement = conn.prepareStatement("select t2.name from " +
					Database.SEQUENCE_USER_PERMISSIONS + " as t1 left join " + 
					Database.SEQUENCE_DATA + " as t2 on t1.data_id = t2.id where t1.user_id = ?;");
			statement.setInt(1, u.getId());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if (r != null) {
					available.add(r);
				}
			}
			
			statement = conn.prepareStatement("select t2.name from " +
					Database.SEQUENCE_GROUP_PERMISSIONS + " as t1 inner join (" + Database.SEQUENCE_DATA + 
					" as t2, " + Database.GROUP_PERMISSIONS + " as t3) on (t1.group_id = t3.group_id and " +
					"t1.data_id = t2.id) where t3.member_id = ?;");
			statement.setInt(1, u.getId());
			res= statement.executeQuery();
			while(res.next()){
				String r = res.getString(1);
				if(r != null){
					available.add(r);
				}
			}
			return available;
		}catch(SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try{
				conn.close();
			}catch (SQLException e1){e1.printStackTrace();}
		}
	}
	
	

	
	/**
	 * Adds a property to the sequence data indicated by the hook, 
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param key The key to which the given property is mapped - the property name. (not null)
	 * @param value The value of the property. (not null)
	 * @throws DataServiceException "Null value passed to SequenceService.addProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence already contains property with given key."
	 * 			"Sequence properties could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public void addProperty(SequenceHook seq, String key, IsSerializable value)
			throws DataServiceException {
		SequenceServiceImpl.addProperty(seq, key, value, this.getThreadLocalRequest().getSession());
	}


	/**
	 * Adds a property to the sequence data indicated by the hook, 
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param key The key to which the given property is mapped - the property name. (not null)
	 * @param value The value of the property. (not null)
	 * @throws DataServiceException "Null value passed to SequenceService.addProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence already contains property with given key."
	 * 			"Sequence properties could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public void addProperty(SequenceHook seq, String key, String value)
			throws DataServiceException {
		SequenceServiceImpl.addProperty(seq, key, value, this.getThreadLocalRequest().getSession());
	}


	/**
	 * Adds a property to the sequence data indicated by the hook, 
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param key The key to which the given property is mapped - the property name. (not null)
	 * @param value The value of the property. (not null)
	 * @throws DataServiceException "Null value passed to SequenceService.addProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence already contains property with given key."
	 * 			"Sequence properties could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public void addProperty(SequenceHook seq, String key, Boolean value)
			throws DataServiceException {
		SequenceServiceImpl.addProperty(seq, key, value, this.getThreadLocalRequest().getSession());
	}


	/**
	 * Adds a property to the sequence data indicated by the hook, 
	 * if the current user has permission to access the given sequence.
	 * @param seq The hook referencing the existing sequence data to modify. (not null)
	 * @param key The key to which the given property is mapped - the property name. (not null)
	 * @param value The value of the property. (not null)
	 * @throws DataServiceException "Null value passed to SequenceService.addProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence already contains property with given key."
	 * 			"Sequence properties could not be saved."
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public void addProperty(SequenceHook seq, String key, Integer value)
			throws DataServiceException {
		SequenceServiceImpl.addProperty(seq, key, value, this.getThreadLocalRequest().getSession());
	}


	/**
	 * Gives a single property value mapped to the given key for the specified sequence,
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param key The property name (not null)
	 * @return A map of property names to the property values.
	 * @throws DataServiceException "Null value passed to SequenceService.getProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence property not found with key " + key
	 * 			"Property '" + key + "' is not of type boolean"
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public boolean getBooleanProperty(SequenceHook seq, String key)
			throws DataServiceException {
		boolean result;
		try {
			result = (Boolean) SequenceServiceImpl.getProperty(seq, key, this.getThreadLocalRequest().getSession());
		}
		catch (ClassCastException e) {
			throw new DataServiceException("Property '" + key + "' is not of type boolean");
		}
		
		return result;
	}


	/**
	 * Gives a single property value mapped to the given key for the specified sequence,
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param key The property name (not null)
	 * @return A map of property names to the property values.
	 * @throws DataServiceException "Null value passed to SequenceService.getProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence property not found with key " + key
	 * 			"Property '" + key + "' is not of type integer"
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public int getIntegerProperty(SequenceHook seq, String key)
			throws DataServiceException {
		int result;
		try {
			result = (Integer) SequenceServiceImpl.getProperty(seq, key, this.getThreadLocalRequest().getSession());
		}
		catch (ClassCastException e) {
			throw new DataServiceException("Property '" + key + "' is not of type integer");
		}
		
		return result;
	}

	/**
	 * Gives a single property value mapped to the given key for the specified sequence,
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param key The property name (not null)
	 * @return A map of property names to the property values.
	 * @throws DataServiceException "Null value passed to SequenceService.getProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence property not found with key " + key
	 * 			"Property '" + key + "' is not of type IsSerializable"
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public IsSerializable getIsSerializableProperty(SequenceHook seq, String key)
			throws DataServiceException {
		IsSerializable result;
		try {
			result = (IsSerializable) SequenceServiceImpl.getProperty(seq, key, this.getThreadLocalRequest().getSession());
		}
		catch (ClassCastException e) {
			throw new DataServiceException("Property '" + key + "' is not of type IsSerializable");
		}
		
		return result;
	}

	/**
	 * Gives a single property value mapped to the given key for the specified sequence,
	 * if the current user has permission to view this data. 
	 * @param seq The hook referencing the existing sequence data to access. (not null)
	 * @param key The property name (not null)
	 * @return A map of property names to the property values.
	 * @throws DataServiceException "Null value passed to SequenceService.getProperty"
	 * 			"User is no longer logged in."
	 * 			"User does not have permission to use requested sequence."
	 * 			"Sequence could not be found in the database."
	 * 			"Sequence property not found with key " + key
	 * 			"Property '" + key + "' is not of type String"
	 * 			"Error reading data."
	 * 			"Error connecting to database."
	 */
	public String getStringProperty(SequenceHook seq, String key)
			throws DataServiceException {
		String result;
		try {
			result = (String) SequenceServiceImpl.getProperty(seq, key, this.getThreadLocalRequest().getSession());
		}
		catch (ClassCastException e) {
			throw new DataServiceException("Property '" + key + "' is not of type String");
		}
		
		return result;
	}

}
