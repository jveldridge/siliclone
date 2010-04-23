

package edu.brown.cs32.siliclone.tasks.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import edu.brown.cs32.siliclone.tasks.Request;
import edu.brown.cs32.siliclone.tasks.Task;

/**
 * Takes care of retrieving tasks from a newly connected TaskClient,
 * and communicating with it.
 */
public class TaskClientHandler implements Runnable{
	
	private Socket _socket;
	private TaskScheduler _scheduler;
	private ObjectOutputStream _oos;
	private HashSet<Request> _requestsWaiting;
	private static final boolean DEBUG = true;
	
	/**
	 * Makes a new TaskClientHandler
	 * @param socket the socket that the incoming TaskClient is connected to
	 * @param scheduler there Will only be one workerDispatcherListener, but
	 * to be good Object-Oriented programmers, we pass a reference to it just to be safe.
	 */
	public TaskClientHandler(Socket socket, TaskScheduler scheduler){
		_socket=socket;
		_scheduler=scheduler;
		try {
			_oos = new ObjectOutputStream(_socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Connection with TaskClient at"
					+ _socket.getInetAddress()+" has been broken");
		}
		if(DEBUG){
			_requestsWaiting = new HashSet<Request>();
		}
	}
	
	
	/**
	 * Returns this request to the TaskClient. If the debug mode is turned off,
	 * At this point it is trusted that this is the right request to return to this client
	 * @param request the request that needs to be sent to the TaskClient
	 */
	public void returnCompletedRequest(Request request){
		if(DEBUG){
			synchronized (_requestsWaiting) {
				assert(_requestsWaiting.contains(request));
				_requestsWaiting.remove(request);
			}
		}
		
		synchronized (_oos) {
			try {
				_oos.writeObject(request);
				_oos.flush();
			} catch (IOException e) {
				System.err.println("Failed to return completed request to TaskClient at "+_socket.getInetAddress());
			}
			
			
		}
	}

	
	/**
	 * Takes care of retrieving tasks from a newly connected TaskClient
	 */
	public void run() {

		try {
			ObjectInputStream ois = new ObjectInputStream(_socket
					.getInputStream());
			while(true){
			Object incomingObject = null;
			try {
				incomingObject = ois.readObject();
				if (!(incomingObject instanceof Request)) {
					throw new ClassNotFoundException();
				}
				Request incomingRequest = (Request) incomingObject;
				
				if(DEBUG){
					System.out.println("Received a request, queueing now");
					synchronized (_requestsWaiting) {
						assert(_requestsWaiting.contains(incomingRequest));
						_requestsWaiting.add(incomingRequest);
					}
				}
				
				_scheduler.enqueueRequest(incomingRequest,this);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.err.println("Ignored non-Request data from "
						+ _socket.getInetAddress());
				System.err.println(incomingObject.getClass().getName());
			}
			}
		} catch (IOException e) {
			System.err.println("Error when communicating with "
					+ _socket.getInetAddress());
		}
		
		
		
		
	}

}
