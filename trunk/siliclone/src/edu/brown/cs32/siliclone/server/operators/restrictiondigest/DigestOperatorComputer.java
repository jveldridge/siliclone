package edu.brown.cs32.siliclone.server.operators.restrictiondigest;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class DigestOperatorComputer implements OperatorComputer {

	
	private int progress;

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		System.out.println("hi debug3");
		System.out.println("Were here! "+properties.get("enzymes").toString());
		return input[0];
	}

	public int getProgress() {
		return progress;
	}


	public void cancel() {
		System.out.println("cancel ignored");
		
	}

}
