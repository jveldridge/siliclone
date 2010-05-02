package edu.brown.cs32.siliclone.database.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.tasks.Task;

public class IndexNucleotideSequenceTask implements Task {

	
	int id;
	
	public IndexNucleotideSequenceTask(int id) {
		this.id = id;	}
	
	public void compute()  {
		Connection conn;
		try {
			conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCES + " where id = ?",ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Blob b = res.getBlob(2);
			NucleotideString ns =  (NucleotideString) Database.loadCompressedObject(b);
			ns.makeIndex(6);
			res.updateObject(2, ns);
			res.updateInt(3, ns.getIndexDepth());
			res.updateRow();
			
			
		}catch (SQLException e){
			e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
		} catch (DataServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("finished compute for indexing");

	}

}
