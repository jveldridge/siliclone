package edu.brown.cs32.siliclone.server.operators.ligation;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.TasksDelegation;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;
import edu.brown.cs32.siliclone.tasks.LigationTask;
import edu.brown.cs32.siliclone.tasks.PCRTask;

public class LigationOperatorComputer implements OperatorComputer {

	private int progress;
	
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		LigationTask task = new LigationTask(input, properties);
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
