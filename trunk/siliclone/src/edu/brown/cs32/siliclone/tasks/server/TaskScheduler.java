package edu.brown.cs32.siliclone.tasks.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.brown.cs32.siliclone.tasks.Request;

public class TaskScheduler{
	
	ConcurrentLinkedQueue<Request> _requestQueue;
	ConcurrentHashMap<Request, TaskClientHandler> _completedRequestsDestinations;
	WorkerDispatcherListener _workerDispatcherListener;
	
	public TaskScheduler(WorkerDispatcherListener workerDispatcherListener){
		_requestQueue = new ConcurrentLinkedQueue<Request>();
		_completedRequestsDestinations = new ConcurrentHashMap<Request, TaskClientHandler>();
		_workerDispatcherListener = workerDispatcherListener;
	}
	
	public void enqueueRequest(Request request, TaskClientHandler tch){
		if(!_requestQueue.add(request)){
			System.err.println("A valid request failed to be added to the scheduler. This will result in a missed task!! (but will be ignored for now)");
			Thread.dumpStack();
		}
		if(_completedRequestsDestinations.put(request, tch) != null){
			System.err.println("A valid request was added to the scheduler, that was already in the scheduler. This is very strange but will be ignored");
		}
		invokeWorker();
		
	}
	
	public void invokeWorker(){
		_workerDispatcherListener.dispatchWorker();
		
	}
	
	public void returnCompletedRequest(Request request){
		_completedRequestsDestinations.remove(request).returnCompletedRequest(request);	
	}
	
	/**
	 * 
	 * @return null if there are no more jobs on the queue
	 */
	public Request getNextRequest(){
		return _requestQueue.poll();
	}

}
