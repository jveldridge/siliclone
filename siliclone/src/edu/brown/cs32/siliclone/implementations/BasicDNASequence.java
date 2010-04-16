package edu.brown.cs32.siliclone.implementations;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.interfaces.Feature;


public class BasicDNASequence implements edu.brown.cs32.siliclone.interfaces.DNASequence {
	
	private final String _sequence;
	private Map<String, Collection<Feature>> _features;
	private Map<String, Object> _properties;

	BasicDNASequence(String sequence) {
		_sequence = sequence;
	}
	
	BasicDNASequence(String sequence, Map<String,Collection<Feature>> features, 
				     Map<String,Object> properties) {
		_sequence = sequence;
		_features = features;
		_properties = properties;
	}
	
	@Override
	public String getSequence() {
		return _sequence;
	}

	@Override
	public int length() {
		return _sequence.length();
	}
	
	@Override
	public Collection<Feature> getFeaturesOfType(String featureType) {
		return _features.get(featureType);
	}
	
	@Override
	public void addFeature(Feature toAdd, String type) {
		if (type != null) {
			if (_features.get(type) != null) {
				_features.get(type).add(toAdd);
			}
			else {
				Collection<Feature> collecToAdd = new ArrayList<Feature>();
				collecToAdd.add(toAdd);
				_features.put(type, collecToAdd);
			}
		}
	}
	
	@Override
	public void addProperty(String key, Object value) {
		_properties.put(key, value);
	}

	@Override
	public Object getProperty(String key) {
		return _properties.get(key);
	}
}
