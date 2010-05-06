package edu.brown.cs32.siliclone.server.operators.pcr2;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.TasksDelegation;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;
import edu.brown.cs32.siliclone.tasks.PCRTask;

public class PCR2OperatorComputer implements OperatorComputer {

	private int progress;
	
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		PCRTask task = new PCRTask(input, properties);
		progress = 25;
		task.compute();
		progress = 100;
		return task.getOutput();
	}
	
	public int getProgress() {
		// TODO Auto-generated method stub
		return progress;
	}

}