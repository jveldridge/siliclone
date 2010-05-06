package edu.brown.cs32.siliclone.server.operators.pcr2;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.TasksDelegation;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;
import edu.brown.cs32.siliclone.tasks.PCRTask;

public class PCR2OperatorComputer implements OperatorComputer {

	private int progress;
	
	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map<String, String> properties) {
		PCRTask task = new PCRTask(input, properties);
		task.compute();
		return task.getOutput();
	}

	@Override
	public int getProgress() {
		// TODO Auto-generated method stub
		return progress;
	}

}
