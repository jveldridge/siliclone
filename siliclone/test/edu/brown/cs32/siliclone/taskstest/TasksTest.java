package edu.brown.cs32.siliclone.taskstest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs32.siliclone.tasks.SquareTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;
import edu.brown.cs32.siliclone.tasks.client.TaskTimedOutException;


public class TasksTest {
	
	private static TaskClient _tc;

	@BeforeClass
	public static void initialize(){
		try {
			_tc = new TaskClient("doug", 2010);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testLocalisRemote(){
		SquareTask localTask = new SquareTask();
		SquareTask remoteTaskIn = new SquareTask();
		long start = System.nanoTime();
		localTask.numberToBeSquared = 2000;
		remoteTaskIn.numberToBeSquared = localTask.numberToBeSquared;
		//localTask.compute();
		System.out.println("time elapsed for local: "+(System.nanoTime()-start)/1000000);
		start = System.nanoTime();
		SquareTask remoteTaskOut = null;
		try {
			
			remoteTaskOut = (SquareTask) _tc.computeTask(remoteTaskIn);
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TaskTimedOutException e){
			System.err.println(e);
		}
		System.out.println("time elapsed for remote: "+(System.nanoTime()-start)/1000000);
		
		Assert.assertEquals(remoteTaskOut.squaredNumber, remoteTaskIn.numberToBeSquared*remoteTaskIn.numberToBeSquared);
		

		
	}
	
	
	@Test
	public void testManyRequests(){
		
		final int numberOfRequests=10;
		
		System.out.println("starting "+numberOfRequests+" task requests");
			TestRequester[] requesters = new TestRequester[numberOfRequests];
			Random r = new Random();
			for(int i = 0 ; i<numberOfRequests;i++){
				//System.out.println("thread spawned "+i);
				
				requesters[i]=new TestRequester(_tc, r.nextInt(1000),"testrequesterthread "+i);
				requesters[i].start();
				
			}
			for(int i = 0 ; i<numberOfRequests;i++){
				
				try {
					requesters[i].join();
					System.out.println("Thread "+i+" (number="+requesters[i]._number+"):"+requesters[i].worked);
					Assert.assertTrue(requesters[i].worked);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		
	}
	
	
	
	private class TestRequester extends Thread{

		TaskClient _tc;
		int _number;
		boolean worked=false;
		
		public TestRequester(TaskClient tc, int number,String name) {
			super(name);
			_tc = tc;
			_number=number;
			
		}
		
		public void run() {
			SquareTask remoteTaskIn = new SquareTask();
			remoteTaskIn.numberToBeSquared = _number;
			long start = System.nanoTime();
			SquareTask remoteTaskOut = null;
			try {
				
				remoteTaskOut = (SquareTask) _tc.computeTask(remoteTaskIn);
				
				worked=remoteTaskOut.squaredNumber==remoteTaskIn.numberToBeSquared*remoteTaskIn.numberToBeSquared;
				
			}catch (TaskTimedOutException e){
				System.err.println(e);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println("time elapsed for remote with number "+_number+": "+(System.nanoTime()-start)/1000000);
			
			

			
		}
		
		
		
	}
	
}
