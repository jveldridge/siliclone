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
import java.util.Map;

import javax.servlet.http.HttpSession;

import sun.misc.Cache;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.server.TasksDelegation;

@SuppressWarnings("serial")
public class SequenceServiceImpl extends RemoteServiceServlet implements SequenceService{
	
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
	
	
	@SuppressWarnings("unchecked")
	public void addProperty(SequenceHook seq, String key, IsSerializable value)
			throws DataServiceException {
		if(seq == null || key == null || value == null){
			throw new DataServiceException("Null value passed to SequenceService.addProperty");
		}
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " + 
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Map<String, IsSerializable> properties = (Map<String, IsSerializable>)
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


	@SuppressWarnings("unchecked")
	public IsSerializable getProperty(SequenceHook seq, String key)
			throws DataServiceException {
		if(seq == null || key == null){
			throw new DataServiceException("Null value passed to SequenceService.getProperty");
		}
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select properties from " +
					Database.SEQUENCE_DATA + " where id = ?");
			statement.setInt(1, seq.getDataID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			IsSerializable property = 
				((Map<String, IsSerializable>) Database.loadCompressedObject(res.getBlob(1))).get(key);
			if(property == null){
				throw new DataServiceException("Sequence property not found with key " + key);
			}
			return property;
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

	public Cache nucleotideStringCache; //TODO make cache
	
	public NucleotideString getSequence(SequenceHook seq)
			throws DataServiceException {
		if(seq == null){
			throw new DataServiceException("Null value passed to SequenceService.getSequence");
		}
		UserServiceImpl.verifyAccess(this.getThreadLocalRequest().getSession(), seq);
		
		Connection conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select data from " +
					Database.SEQUENCES + " where id = ?");
			statement.setInt(1, seq.getSeqID());
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				System.out.println("were here now "+statement.toString()+" e "+ res.toString());
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
	
	public String getNucleotides(SequenceHook seq) throws DataServiceException {
		NucleotideString nuc = this.getSequence(seq);
		return nuc.toString();
	}


	public int length(SequenceHook seq) throws DataServiceException {
		return getSequence(seq).getLength();
	}
	
	public SequenceHook saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, IsSerializable> properties) throws DataServiceException {
		return saveSequence(nucleotides, features, seqName, properties, this.getThreadLocalRequest().getSession());
	}

	public static SequenceHook saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, IsSerializable> properties, HttpSession session) throws DataServiceException {
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
			
			int seqID=-1;
			
			
			//first check if a sequence with the same hash exists already. if so, check if its the same
			
			PreparedStatement statement2 = conn.prepareStatement("select * from " +
					Database.SEQUENCES + " where hash = ?", Statement.RETURN_GENERATED_KEYS);
			statement2.setInt(1, nucleotides.hashCode());
			System.out.println("hashcode: "+nucleotides.hashCode());
			ResultSet res2 = statement2.executeQuery();
			
			
			while(res2.next()){
				Blob b2 = res2.getBlob(2);
				NucleotideString ns2= (NucleotideString) Database.loadCompressedObject(b2);
				System.out.println("someindex:"+ns2);
				if(nucleotides.equals(ns2)){
					
					seqID = res2.getInt(1);
					System.out.println("nucleotide string is already in database, making shallow hequencehook to id "+seqID);
					
					//TODO MAKE SURE THAT YOU BLOCK UNTIL THE SEQUENCE IS ACTUALLY INDEXED!!!
				}
				
				
			}
			
			if(seqID==-1){
			
			statement = conn.prepareStatement("insert into " + Database.SEQUENCES + 
					" (data,indexDepth,hash) values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
	
			Database.saveCompressedObject(statement,1, nucleotides);
			statement.setInt(2,nucleotides.getIndexDepth());
			statement.setInt(3,nucleotides.hashCode());
			statement.executeUpdate();
			
			res = statement.getGeneratedKeys();
			if(!res.next()){
				throw new DataServiceException("Error saving sequence to database.");
			}
			seqID = res.getInt(1);
			TasksDelegation.delegate(new IndexNucleotideSequenceTask(seqID), null);
			
		}
			
			
			
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
			
			
			
			//now index before the hook is returned.
			
			
			//now give current user permissions
			statement = conn.prepareStatement("insert into " + Database.SEQUENCE_USER_PERMISSIONS +
					"(data_id, user_id) values (?, ?)");
			statement.setInt(1, dataID);
			statement.setInt(2, UserServiceImpl.getLoggedIn(session).getId());
			if (0 >= statement.executeUpdate()){
				throw new DataServiceException("Error granting user permission to saved sequence.");
			}
			
			return new SequenceHook(dataID, seqID, seqName);
			
		}catch (SQLException e){
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataServiceException("Error connecting to database 2.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataServiceException("Non-nucleotidestring data found in database ???");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
		

		
	}
	
	public SequenceHook saveSequence(String nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, IsSerializable> properties) throws DataServiceException {
		return SequenceServiceImpl.saveSequence(nucleotides, features, seqName, properties, this.getThreadLocalRequest().getSession());
	}

	public static SequenceHook saveSequence(String nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, IsSerializable> properties, HttpSession session) throws DataServiceException {
		NucleotideString seq = new NucleotideString(nucleotides);
		return SequenceServiceImpl.saveSequence(seq, features, seqName, properties, session);
	}

	public SequenceHook saveSequence(String nucleotides, String seqName)
			throws DataServiceException {
		return SequenceServiceImpl.saveSequence(nucleotides, seqName, this.getThreadLocalRequest().getSession());
	}
	
	public static SequenceHook saveSequence(String nucleotides, String seqName, HttpSession session)
	throws DataServiceException {
		return SequenceServiceImpl.saveSequence(nucleotides, new HashMap<String, Collection<Feature>>(), 
		seqName, new HashMap<String, IsSerializable>(), session);
}

}
