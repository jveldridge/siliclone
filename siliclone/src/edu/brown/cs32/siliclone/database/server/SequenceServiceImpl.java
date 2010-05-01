package edu.brown.cs32.siliclone.database.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

@SuppressWarnings("serial")
public class SequenceServiceImpl extends RemoteServiceServlet implements SequenceService{

	private User getLoggedIn() throws DataServiceException{
		User u = (User) this.getThreadLocalRequest().getSession().getAttribute("user");
		if(u == null){
			throw new DataServiceException("User is no longer logged in.");
		}
		return u;
	}

	private void verifyAccess(SequenceHook seq) throws DataServiceException{
		User u = getLoggedIn();
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
		}
		
	}
	
	public void addFeature(SequenceHook seq, Feature toAdd)
			throws DataServiceException {
		//verifyAccess(seq);
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
			Map<String, Collection<Feature>> features = (Map<String, Collection<Feature>>) res.getObject(1);
			if(features.containsKey(toAdd.getType())){
				features.get(toAdd.getType()).add(toAdd);
			}else{
				ArrayList<Feature> featureSet = new ArrayList<Feature>();
				featureSet.add(toAdd);
				features.put(toAdd.getType(), featureSet);
			}
			statement = conn.prepareStatement("update " + Database.SEQUENCE_DATA + 
					" set data = ? where id = ?");
			statement.setObject(1, features);
			statement.setInt(2, seq.getDataID());
			if (0 >= statement.executeUpdate()){
				throw new DataServiceException("Sequence features could not be saved.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	public void addProperty(SequenceHook seq, String key, Serializable value)
			throws DataServiceException {
		if(seq == null || key == null || value == null){
			throw new DataServiceException("Null value passed to SequenceService.addProperty");
		}
		//verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " + 
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Map<String, Serializable> properties = (Map<String, Serializable>) res.getObject(1);
			if(properties.containsKey(key)){
				throw new DataServiceException("Sequence already contains property with given key.");
			}

			properties.put(key, value);

			statement = conn.prepareStatement("update " + Database.SEQUENCE_DATA + 
					" set properties = ? where id = ?");
			statement.setObject(1, properties);
			statement.setInt(2, seq.getDataID());
			if (0 >= statement.executeUpdate()){
				throw new DataServiceException("Sequence properties could not be saved.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Feature> getFeaturesOfType(SequenceHook seq,
			String featureType) throws DataServiceException {
		if(seq == null || featureType == null){
			throw new DataServiceException("Null value passed to SequenceService.getFeaturesOfType");
		}
		//verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select features from " +
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			
			Blob b = res.getBlob(1);
			ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes(1, (int) b.length()));
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ((Map<String, Collection<Feature>>) ois.readObject()).get(featureType);
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	@SuppressWarnings("unchecked")
	public Serializable getProperty(SequenceHook seq, String key)
			throws DataServiceException {
		if(seq == null || key == null){
			throw new DataServiceException("Null value passed to SequenceService.getProperty");
		}
		//verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " +
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			
			Blob b = res.getBlob(1);
			ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes(1, (int) b.length()));
			ObjectInputStream ois = new ObjectInputStream(bis);
			Serializable property = ((Map<String, Serializable>) ois.readObject()).get(key);
			if(property == null){
				throw new DataServiceException("Sequence property not found with key " + key);
			}
			return property;
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	public NucleotideString getSequence(SequenceHook seq)
			throws DataServiceException {
		if(seq == null){
			throw new DataServiceException("Null value passed to SequenceService.getSequence");
		}
		//verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select data from " +
					Database.SEQUENCES + " where id = ?");
			statement.setInt(1, seq.getSeqID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Blob b = res.getBlob(1);
			ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes(1, (int) b.length()));
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (NucleotideString) ois.readObject();
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataServiceException("Error reading data.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

	public int length(SequenceHook seq) throws DataServiceException {
		return getSequence(seq).getLength();
	}

	public SequenceHook saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, Serializable> properties) throws DataServiceException {
		if(nucleotides == null || features == null || seqName == null || properties == null){
			throw new DataServiceException("Null value passed to SequenceService.saveSequence");
		}
		//verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCE_DATA + " where name = ?");
			statement.setString(1, seqName);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				throw new DataServiceException("Sequence data with name " + seqName + " already exists");
			}
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCES + 
					" (data) values (?);");
			statement.setObject(1, nucleotides);
			statement.executeUpdate();
			
			res = statement.getGeneratedKeys();
			if(!res.next()){
				throw new DataServiceException("Error saving sequence to database.");
			}
			int seqID = res.getInt(1);
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCE_DATA + 
					" (name, seq_id, features, properties) values (?,?,?,?)");
			statement.setString(1, seqName);
			statement.setInt(2, seqID);
			statement.setObject(3, features);
			statement.setObject(4, properties);
			statement.executeUpdate();
			
			res = statement.getGeneratedKeys();
			if(!res.next()){
				throw new DataServiceException("Error saving sequence data to database.");
			}
			int dataID = res.getInt(1);
			
			return new SequenceHook(dataID, seqID, seqName);
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
	}

}
