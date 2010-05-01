package edu.brown.cs32.siliclone.database.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

public interface SequenceServiceAsync {

	void addFeature(SequenceHook seq, Feature toAdd,
			AsyncCallback<Void> callback) throws DataServiceException;

	void addProperty(SequenceHook seq, String key, Serializable value,
			AsyncCallback<Void> callback) throws DataServiceException;

	void getFeaturesOfType(SequenceHook seq, String featureType,
			AsyncCallback<Collection<Feature>> callback) throws DataServiceException;

	void getProperty(SequenceHook seq, String key,
			AsyncCallback<Serializable> callback) throws DataServiceException;

	void getSequence(SequenceHook seq, AsyncCallback<NucleotideString> callback) throws DataServiceException;

	void length(SequenceHook seq, AsyncCallback<Integer> callback) throws DataServiceException;

	void saveSequence(NucleotideString nucleotides,
			Map<String, Collection<Feature>> features,
			String seqName,
			Map<String, Serializable> properties,
			AsyncCallback<SequenceHook> callback) throws DataServiceException;

}
