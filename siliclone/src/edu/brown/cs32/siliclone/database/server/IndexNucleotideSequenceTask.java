package edu.brown.cs32.siliclone.database.server;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.tasks.Task;

/**
 * The Index Nucleotide Sequence Task is responsible for 
 * updating a nucleotide string in the sequences table of the database,
 * so that the nucleotide string has a suffix tree initialized.
 */
@SuppressWarnings("serial")
public class IndexNucleotideSequenceTask implements Task {
	int id;
	
	
	private static final int SUFFIX_INDEX = 6;
	
	/**
	 * Constructs a task to index the nuclotide string
	 * at the given index in the sequences table.
	 * @param id The index of the existing sequence.
	 */
	public IndexNucleotideSequenceTask(int id) {
		this.id = id;	
	}
	
	/**
	 * Executes the task - indexing the nucleotide sequence.
	 */
	public void compute()  {
		Connection conn;
		try {
			conn = Database.getConnection();
		try{
			PreparedStatement statement = conn.prepareStatement("select * from " +
					Database.SEQUENCES + " where id = ?");//,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			if(!res.next()){
				throw new DataServiceException("Sequence could not be found in the database.");
			}
			Blob b = res.getBlob(2);
			NucleotideString ns =  (NucleotideString) Database.loadCompressedObject(b);
			
			
			ns.makeIndex(SUFFIX_INDEX);
			PreparedStatement statement2 = conn.prepareStatement("update "+Database.SEQUENCES+
				" set data=?, indexDepth=? where id = ?");
			
			
			Database.saveCompressedObject(statement2, 1, ns);
			statement2.setInt(2, ns.getIndexDepth());
			statement2.setInt(3, id);
			
			statement2.executeUpdate();
			
			
			statement = conn.prepareStatement("update " + Database.SEQUENCES + " set data = ?, indexDepth = ?  where id = ?");
			statement.setInt(3, id);
			statement.setInt(2, ns.getIndexDepth());
			Database.saveCompressedObject(statement, 1, ns);
			
			statement.executeUpdate();
			
		}catch (SQLException e){
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (DataServiceException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) { e.printStackTrace(); }
		}
		} catch (DataServiceException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("finished compute for indexing");
	}

}
