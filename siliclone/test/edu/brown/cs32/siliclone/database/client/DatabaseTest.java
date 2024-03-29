package edu.brown.cs32.siliclone.database.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.server.Database;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.dna.NucleotideString;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseTest {

	@Test
	public void testGetConnection() {
		
		
		Connection conn = null;
		try {
			conn = Database.getConnection();
		} catch (DataServiceException e1) {
			fail("Could not create Database connection.");
		}
		assertNotNull(conn); //connection created
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
