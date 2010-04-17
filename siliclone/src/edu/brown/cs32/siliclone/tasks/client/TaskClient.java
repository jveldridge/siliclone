package edu.brown.cs32.siliclone.tasks.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs32.siliclone.tasks.Request;
import edu.brown.cs32.siliclone.tasks.Task;

public class TaskClient implements Runnable{
	
	private Socket _socket;
	private ObjectOutputStream _oos;
	private ConcurrentHashMap<Request,Task> _returnedRequests;
	private HashSet<Request> _requestsWaiting;
	private static final boolean DEBUG = true;
	
	public TaskClient(String host, int port) throws UnknownHostException, IOException{
	
		_socket = new Socket(host,port);
		_oos = new ObjectOutputStream(_socket.getOutputStream());
		_returnedRequests = new ConcurrentHashMap<Request,Task>();
		if(DEBUG){
		_requestsWaiting = new HashSet<Request>();
		new Thread(this,"TaskClient").start();
		}
		
	}
	
	public Task computeTask(Task task) throws IOException{
		
		Request request = new Request(task);
		
		synchronized (_oos) {
			_oos.writeObject(request);			
		}
		
		if(DEBUG){
		synchronized (_requestsWaiting) {
			assert(!_requestsWaiting.contains(request));
			_requestsWaiting.add(request);
		}
		}
		
		synchronized (_returnedRequests) {
			while(!_returnedRequests.containsKey(request)){
				try {
					_returnedRequests.wait();
				} catch (InterruptedException e) {
					System.err.println("InterruptedException while waiting for task to be computed");
					e.printStackTrace();
				}
			}
			if(DEBUG){
			synchronized (_requestsWaiting) {
				assert(_requestsWaiting.contains(request));
				_requestsWaiting.remove(request);
			}
			}
			
			return _returnedRequests.remove(request);
			
			
		}
		
		
	}

	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(_socket
					.getInputStream());
			while (true) {
				Object incomingObject = ois.readObject();
				if(DEBUG){
					System.out.println("Received a task back!");
				}
				if (!(incomingObject instanceof Request)) {
					throw new ClassNotFoundException();
				}
				Request incomingRequest = (Request) incomingObject;
				synchronized(_returnedRequests){
					_returnedRequests.putIfAbsent(incomingRequest, incomingRequest.getTask());
					_returnedRequests.notify();
				}
			
			}
		} catch (IOException e) {
			System.err.println("Error when communicating with "
					+ _socket.getInetAddress());
		} catch (ClassNotFoundException e) {
			System.err.println("Received non-Request data from the server at "
					+ _socket.getInetAddress());
		}
	}

}
