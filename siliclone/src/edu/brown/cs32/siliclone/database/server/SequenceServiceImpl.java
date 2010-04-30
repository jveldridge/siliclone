package edu.brown.cs32.siliclone.database.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

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
		verifyAccess(seq);
		
		Connection conn = Database.getConnection();
		
		try{
			PreparedStatement statement = conn.prepareStatement("select data from " + 
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
				throw new DataServiceException("Sequence data could not be saved.");
			}
		}catch (SQLException e){
			throw new DataServiceException("Error connecting to database.");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
		
	}

	public void addProperty(SequenceHook seq, String key, Object value)
			throws DataServiceException {
		
		
	}

	public Collection<Feature> getFeaturesOfType(SequenceHook seq,
			String featureType) throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(SequenceHook seq, String key)
			throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public NucleotideString getSequence(SequenceHook seq)
			throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public int length(SequenceHook seq) throws DataServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

	public SequenceHook saveSequence(NucleotideString nucleotides,
			Collection<Feature> features, String seqName,
			Map<String, Object> properties) throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
