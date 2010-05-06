package edu.brown.cs32.siliclone.tasks;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public class PCRTask implements Task {

	
	private Collection<SequenceHook> output;
	private Collection<SequenceHook>[] input;
	private Map<String, String> properties;
	
	public PCRTask(Collection<SequenceHook>[] input, Map<String, String> properties) {
		this.input = input;
		this.properties = properties;
	}

	public void compute() {
		Integer matchLength = Integer.parseInt(properties.get("match"));
		
		
	}
	
	public Collection<SequenceHook> getOutput() {
		return null;
		
	}

}
