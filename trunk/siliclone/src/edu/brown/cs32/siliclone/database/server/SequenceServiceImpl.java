package edu.brown.cs32.siliclone.database.server;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.database.client.NoSuchSequenceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

public class SequenceServiceImpl extends RemoteServiceServlet implements SequenceService{

	public void addFeature(SequenceHook seq, Feature toAdd, String type)
			throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		
	}

	public void addProperty(SequenceHook seq, String key, Object value)
			throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		
	}

	public Collection<Feature> getFeaturesOfType(SequenceHook seq,
			String featureType) throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(SequenceHook seq, String key)
			throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public NucleotideString getSequence(SequenceHook seq)
			throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public int length(SequenceHook seq) throws NoSuchSequenceException {
		// TODO Auto-generated method stub
		return 0;
	}

	public SequenceHook saveSequence(NucleotideString nucleotides,
			Collection<Feature> features,
			Collection<Entry<String, Object>> properties) {
		// TODO Auto-generated method stub
		return null;
	}

}
