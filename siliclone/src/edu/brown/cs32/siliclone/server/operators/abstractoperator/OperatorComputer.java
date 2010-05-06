package edu.brown.cs32.siliclone.server.operators.abstractoperator;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface OperatorComputer {

	public Collection<SequenceHook> computeOutput(Collection<SequenceHook>[] input, Map properties);
	
	public int getProgress();
	
	public void cancel();
	
}
