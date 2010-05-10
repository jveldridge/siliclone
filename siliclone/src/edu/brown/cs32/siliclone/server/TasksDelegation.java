package edu.brown.cs32.siliclone.server;

import edu.brown.cs32.siliclone.database.server.IndexNucleotideSequenceTask;
import edu.brown.cs32.siliclone.tasks.Task;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;

public class TasksDelegation {
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public static Task delegate(Task t){
		t.compute();
		return t;
	}

}