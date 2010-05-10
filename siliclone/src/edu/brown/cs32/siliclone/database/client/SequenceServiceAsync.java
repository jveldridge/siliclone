package edu.brown.cs32.siliclone.database.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;

public interface SequenceServiceAsync {

	void addFeature(SequenceHook seq, Feature toAdd,
			AsyncCallback<Void> callback) throws DataServiceException;

	void getFeaturesOfType(SequenceHook seq, String featureType,
			AsyncCallback<Collection<Feature>> callback) throws DataServiceException;

	//void getSequence(SequenceHook seq, AsyncCallback<NucleotideString> callback) throws DataServiceException;

	void length(SequenceHook seq, AsyncCallback<Integer> callback) throws DataServiceException;

	//removed b/c should just be called serverside
//	void saveSequence(NucleotideString nucleotides,
//			Map<String, Collection<Feature>> features,
//			String seqName,
//			Map<String, IsSerializable> properties,
//			AsyncCallback<SequenceHook> callback) throws DataServiceException;

	void getNucleotides(SequenceHook seq, AsyncCallback<String> callback);

	void saveSequence(String nucleotides, String seqName,
			AsyncCallback<SequenceHook> callback);

	void findSequence(String name, AsyncCallback<SequenceHook> callback);

	void listAvailableSequences(AsyncCallback<List<String>> callback);

	void addProperty(SequenceHook seq, String key, IsSerializable value,
			AsyncCallback<Void> callback) throws DataServiceException;
	
	void addProperty(SequenceHook seq, String key, String value,
			AsyncCallback<Void> callback);

	void addProperty(SequenceHook seq, String key, Boolean value,
			AsyncCallback<Void> callback);

	void addProperty(SequenceHook seq, String key, Integer value,
			AsyncCallback<Void> callback);

	void getIsSerializableProperty(SequenceHook seq, String key,
			AsyncCallback<IsSerializable> callback);

	void getStringProperty(SequenceHook seq, String key,
			AsyncCallback<String> callback);

	void getBooleanProperty(SequenceHook seq, String key,
			AsyncCallback<Boolean> callback);

	void getIntegerProperty(SequenceHook seq, String key,
			AsyncCallback<Integer> callback);


	void getAllProperties(SequenceHook seq, AsyncCallback<Map> callback);

}
