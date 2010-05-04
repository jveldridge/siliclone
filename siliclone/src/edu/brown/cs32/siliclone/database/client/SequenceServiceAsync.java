package edu.brown.cs32.siliclone.database.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;

public interface SequenceServiceAsync {

	void addFeature(SequenceHook seq, Feature toAdd,
			AsyncCallback<Void> callback) throws DataServiceException;

	void addProperty(SequenceHook seq, String key, IsSerializable value,
			AsyncCallback<Void> callback) throws DataServiceException;

	void getFeaturesOfType(SequenceHook seq, String featureType,
			AsyncCallback<Collection<Feature>> callback) throws DataServiceException;

	void getProperty(SequenceHook seq, String key,
			AsyncCallback<IsSerializable> callback) throws DataServiceException;

	//void getSequence(SequenceHook seq, AsyncCallback<NucleotideString> callback) throws DataServiceException;

	void length(SequenceHook seq, AsyncCallback<Integer> callback) throws DataServiceException;

	//removed b/c should just be called serverside
//	void saveSequence(NucleotideString nucleotides,
//			Map<String, Collection<Feature>> features,
//			String seqName,
//			Map<String, IsSerializable> properties,
//			AsyncCallback<SequenceHook> callback) throws DataServiceException;

	void saveSequence(String nucleotides,
			Map<String, Collection<Feature>> features, String seqName,
			Map<String, IsSerializable> properties,
			AsyncCallback<SequenceHook> callback);

	void getNucleotides(SequenceHook seq, AsyncCallback<String> callback);

}
