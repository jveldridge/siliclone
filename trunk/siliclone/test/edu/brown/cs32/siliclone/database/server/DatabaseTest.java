package edu.brown.cs32.siliclone.database.server;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.features.Feature;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseTest {

	@Test
	public void testGetConnection() {
//		Connection conn = Database.getConnection();
//		assertNotNull(conn); //connection created
//		
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	
	@Test
	public void saveNucleotideTest(){
		NucleotideString ns = new NucleotideString("aattccgccggttaa");
		try {
			new SequenceServiceImpl().saveSequence(ns, new HashMap<String, Collection<Feature>>(), "someinterestingname", new HashMap<String, IsSerializable>());
		} catch (DataServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	
}
