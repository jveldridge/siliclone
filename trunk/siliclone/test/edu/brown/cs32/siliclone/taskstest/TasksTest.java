package edu.brown.cs32.siliclone.taskstest;

import java.io.IOException;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.Test;

import edu.brown.cs32.siliclone.tasks.SquareTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;


public class TasksTest {

	
	@Test
	public void testLocalisRemote(){
		SquareTask localTask = new SquareTask();
		SquareTask remoteTaskIn = new SquareTask();
		localTask.numberToBeSquared = 4;
		remoteTaskIn.numberToBeSquared = 4;
		localTask.compute();
		SquareTask remoteTaskOut = null;
		try {
			
			TaskClient taskClient = new TaskClient("128.148.38.81", 2010);
			
			remoteTaskOut = (SquareTask) taskClient.computeTask(remoteTaskIn);
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Assert.assertEquals(remoteTaskOut.squaredNumber, localTask.squaredNumber);
		
		
		
	}
	
}
