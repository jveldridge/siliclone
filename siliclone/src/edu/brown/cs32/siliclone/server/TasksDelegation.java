package edu.brown.cs32.siliclone.server;

import edu.brown.cs32.siliclone.database.server.IndexNucleotideSequenceTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;

public class TasksDelegation {
	
	public static void delegate(IndexNucleotideSequenceTask t, TaskClient tc){
		
		t.compute();
		
	}

}