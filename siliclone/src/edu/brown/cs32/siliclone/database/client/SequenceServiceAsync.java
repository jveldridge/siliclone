package edu.brown.cs32.siliclone.database.client;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

public interface SequenceServiceAsync {

	void addFeature(SequenceHook seq, Feature toAdd, String type,
			AsyncCallback<Void> callback);

	void addProperty(SequenceHook seq, String key, Object value,
			AsyncCallback<Void> callback);

	void getFeaturesOfType(SequenceHook seq, String featureType,
			AsyncCallback<Collection<Feature>> callback);

	void getProperty(SequenceHook seq, String key,
			AsyncCallback<Object> callback);

	void getSequence(SequenceHook seq, AsyncCallback<NucleotideString> callback);

	void length(SequenceHook seq, AsyncCallback<Integer> callback);

	void saveSequence(NucleotideString nucleotides,
			Collection<Feature> features,
			Collection<Entry<String, Object>> properties,
			AsyncCallback<SequenceHook> callback);

}
