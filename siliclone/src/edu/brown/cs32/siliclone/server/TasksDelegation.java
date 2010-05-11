package edu.brown.cs32.siliclone.server;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.brown.cs32.siliclone.database.server.IndexNucleotideSequenceTask;
import edu.brown.cs32.siliclone.tasks.Task;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;
import edu.brown.cs32.siliclone.tasks.client.TaskTimedOutException;

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
	
	
	/**
	 * uncomment this if you want to run things on the cluster
	 */
	/*
	public static Task delegate(Task t){
		
		
		try {
			TaskClient tc = new TaskClient("caladan.cs.brown.edu", 2010);
			t=tc.computeTask(t);
			tc.close();
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (TaskTimedOutException e) {
			// TODO Auto-generated catch block
			System.err.println("TASK TIMED OUT!");
		}
		return t;
		
		
		
	}
	
	*/

}