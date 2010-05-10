package edu.brown.cs32.siliclone.database.client;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.dna.NucleotideString;


public class SequenceServiceImplTest {

	@Test
	public void saveNucleotideTest(){	
		NucleotideString ns = new NucleotideString("cgattccgccggttacgatcgatcgactcatagatatcagcacatatcagacgatcgtcagttctacgtgacgacgagctacgtgtcagtacgatcgatcgctgatcagtacgcatga");
		SequenceHook hook = null;
		try {
			hook = SequenceServiceImpl.saveSequence(ns, new HashMap<String, Collection<Feature>>(), "someinterestingname"+Math.random(), new HashMap<String, Object>(), true);
		} catch (DataServiceException e) {
			e.printStackTrace();
			fail("could not save sequence");
		};
		
		try {
			ns = SequenceServiceImpl.getSequence(hook);
		} catch (DataServiceException e) {
			e.printStackTrace();
			fail("could not retrieve sequence");
		}
		
		assertEquals(ns.getLength(), 118);
	}
}
