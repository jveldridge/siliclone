package edu.brown.cs32.siliclone.server.operators.pcr;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.TasksDelegation;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;
import edu.brown.cs32.siliclone.server.operators.pcr.PCRTask;

public class PCROperatorComputer implements OperatorComputer {

	private int progress;
	
	public void cancel() {
		
	}

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		PCRTask task = new PCRTask(input, properties);
		progress = 25;
		task = (PCRTask) TasksDelegation.delegate(task);
		progress = 100;
		System.out.println("number of PCR output:"+task.getOutput().size());
		return task.getOutput();
	}
	
	public int getProgress() {
		return progress;
	}

}
