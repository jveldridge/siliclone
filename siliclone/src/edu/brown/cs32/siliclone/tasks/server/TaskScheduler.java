package edu.brown.cs32.siliclone.tasks.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.brown.cs32.siliclone.tasks.Request;

/**
 * Keeps a queue of Tasks that need to be sent to WorkerNodes
 */
public class TaskScheduler{
	
	ConcurrentLinkedQueue<Request> _requestQueue;
	ConcurrentHashMap<Request, TaskClientHandler> _completedRequestsDestinations;
	WorkerDispatcherListener _workerDispatcherListener;
	
	/**
	 * Makes a new Taskscheduler
	 * @param workerDispatcherListener there Will only be one workerDispatcherListener, but
	 * to be good Object-Oriented programmers, we pass a reference to it just to be safe.
	 */
	public TaskScheduler(WorkerDispatcherListener workerDispatcherListener){
		_requestQueue = new ConcurrentLinkedQueue<Request>();
		_completedRequestsDestinations = new ConcurrentHashMap<Request, TaskClientHandler>();
		_workerDispatcherListener = workerDispatcherListener;
	}
	
	
	/**
	 * Adds a new request to the queue
	 * @param request The request the needs to be added
	 * @param tch a reference to the TaskClient that made this request,
	 * such that we know where to return the request to.
	 */
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
	
	/**
	 * returns the information from this completed request to the correct TaskClientHandler
	 * and then forgets all about this request
	 * @param request the request that needs to be returned
	 */
	public void returnCompletedRequest(Request request){
		_completedRequestsDestinations.remove(request).returnCompletedRequest(request);	
	}
	
	/**
	 * returns the next Task that needs to be performed  and removes it from the queue.
	 * @return null if there are no more jobs on the queue
	 */
	public Request getNextRequest(){
		return _requestQueue.poll();
	}

}
