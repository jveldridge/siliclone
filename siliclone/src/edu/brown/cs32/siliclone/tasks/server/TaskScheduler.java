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
	
	private int _worker_timeout;
	private int _dispatch_timeout;
	private int _max_retry_dispatches;
	
/**
 * Makes a new Taskscheduler
	 * @param workerDispatcherListener there Will only be one workerDispatcherListener, but
	 * to be good Object-Oriented programmers, we pass a reference to it just to be safe.
 * @param dispatch_timeout when a new dispatch request is made, the amount of time given until the request is re-made
 * @param worker_timeout after a task has been delivered to a WorkerNode, the amount of time until we give up on waiting for it to return
 */
	public TaskScheduler(WorkerDispatcherListener workerDispatcherListener, int dispatch_timeout, int worker_timeout,int max_retry_dispatches){
		_requestQueue = new ConcurrentLinkedQueue<Request>();
		_completedRequestsDestinations = new ConcurrentHashMap<Request, TaskClientHandler>();
		_workerDispatcherListener = workerDispatcherListener;
		_worker_timeout=worker_timeout;
		_dispatch_timeout=dispatch_timeout;
		_max_retry_dispatches=max_retry_dispatches;
	}
	
	
	/**
	 * Adds a new request to the queue
	 * @param request The request the needs to be added
	 * @param tch a reference to the TaskClient that made this request,
	 * such that we know where to return the request to.
	 */
	public void enqueueRequest(Request request, TaskClientHandler tch){
		if(invokeWorker()){
		if(!_requestQueue.add(request)){
			System.err.println("A valid request failed to be added to the scheduler. This will result in a missed task!! (but will be ignored for now)");
			Thread.dumpStack();
		}
		if(_completedRequestsDestinations.put(request, tch) != null){
			System.err.println("A valid request was added to the scheduler, that was already in the scheduler. This is very strange but will be ignored");
		}
		
		new Thread(new DispatchTimeOutRunner(request),"dispatchertimeoutrunner").start();
		}else{
			System.out.println("Setting this task timed out");
			request.setTimedOut(true);
			tch.returnCompletedRequest(request);
		}
		
	}
	
/**
 * 
 * @return  if there was WorkerNode to request a dispatch from.
 */
	public boolean invokeWorker(){
		return _workerDispatcherListener.dispatchWorker();
		
	}
	
	/**
	 * returns the information from this completed request to the correct TaskClientHandler
	 * and then forgets all about this request
	 * @param request the request that needs to be returned
	 */
	public void returnCompletedRequest(Request request){
		synchronized (request) {
		TaskClientHandler destination = _completedRequestsDestinations.remove(request);
		if(destination==null){
			System.out.println("Received back a task that had timed out already. Now it's too late :("+
					"\nThe task was: '"+request.getTask()+"'("+request.hashCode()+")");

		}else{
			System.out.println("Sending a completed task back ("+request.hashCode()+")");
			destination.returnCompletedRequest(request);
		}
		}
		
			
	}
	
	/**
	 * returns the next Task that needs to be performed  and removes it from the queue.
	 * @return null if there are no more jobs on the queue
	 */
	public Request getNextRequest(){
		Request r =_requestQueue.poll();
		if(r!=null){
		new Thread(new WorkerTimeOutRunner(r),"workertimeoutrunner"+r.getTask().hashCode()).start();
		}
		return r;
	}
	
	
	private class WorkerTimeOutRunner implements Runnable{
private Request _request;
		
		public WorkerTimeOutRunner(Request request) {
		 	_request = request;
		}
		
		public void run() {
			try {
				Thread.sleep(_worker_timeout*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (_request) {
				if(_completedRequestsDestinations.containsKey(_request)){
				System.out.println("A WorkerNode timed out"+
						"\nThe task was: '"+_request.getTask()+"'("+_request.hashCode()+")");
				_request.setTimedOut(true);
				returnCompletedRequest(_request);
				}
				
			}

			
		}
		
		
	}
	
	
	private class DispatchTimeOutRunner implements Runnable{
				
private Request _request;
		
		public DispatchTimeOutRunner(Request request) {
		 	_request = request;
		}
		
				
				public void run() {
					for(int i=0;true;i++){
					try {
						Thread.sleep(_dispatch_timeout*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!_requestQueue.contains(_request)){
						break;
					}
					System.out.println("Re-dispatch request sent out");
					if(i==_max_retry_dispatches||!invokeWorker()){
						_request.setTimedOut(true);
						returnCompletedRequest(_request);
						break;
					}
					}

					
				}
				
				
			}
	
	

}
