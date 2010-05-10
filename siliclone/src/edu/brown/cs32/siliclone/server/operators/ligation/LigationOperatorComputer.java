package edu.brown.cs32.siliclone.server.operators.ligation;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.TasksDelegation;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class LigationOperatorComputer implements OperatorComputer {

	private int progress;
	
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		System.out.println("In ligationoperationcomputer");
		LigationTask task = new LigationTask(input, properties);
		progress = 25;
		task= (LigationTask) TasksDelegation.delegate(task);
		progress = 100;
		System.out.println("number of Ligation output:"+task.getOutput().size());
		return task.getOutput();
	}
	
	public int getProgress() {
		// TODO Auto-generated method stub
		return progress;
	}

}
