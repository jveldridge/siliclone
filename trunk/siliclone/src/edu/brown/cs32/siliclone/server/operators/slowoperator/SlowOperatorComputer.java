package edu.brown.cs32.siliclone.server.operators.slowoperator;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class SlowOperatorComputer implements OperatorComputer {

	
	private int progress;

	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map<String, String> properties) {
		progress = 0;
		for(int i = 0;i<=100;i++){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			progress = i;
			System.out.println(i+"%");
			
		}
		return input[0];
	}

	public int getProgress() {
		return progress;
	}


	public void cancel() {
		System.out.println("cancel ignored");
		
	}

}
