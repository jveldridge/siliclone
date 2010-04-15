/*
 * IncomingTaskListener
 */

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
 *
 * @author tderond
 */
public class WorkerNodeHandler implements Runnable{
	
	private Socket _socket;
	private TaskScheduler _scheduler;
	private static final boolean DEBUG = true;
	
	public WorkerNodeHandler(Socket socket, TaskScheduler scheduler){
		_socket=socket;
		_scheduler=scheduler;
	}
	

	@Override
	public void run() {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(_socket.getOutputStream());
			Request request = _scheduler.getNextRequest();
			if(request==null){
				if(DEBUG){
					System.out.println("No work available for this worker node");
				}
				oos.writeObject(null);
				oos.flush();
			}else{
				if(DEBUG){
					System.out.println("The worker node was assigned a task");
				}
				oos.writeObject(request.getTask());
				oos.flush();
				
				ObjectInputStream ois = new ObjectInputStream(_socket
						.getInputStream());

				Object incomingObject;
				try {
					incomingObject = ois.readObject();
					if (!(incomingObject instanceof Task)) {
						throw new ClassNotFoundException();
					}
					Task incomingTask = (Task) incomingObject;
				request.setTask(incomingTask);
				
				_scheduler.returnCompletedRequest(request);
				

			} catch (ClassNotFoundException e) {
				System.err.println("Ignored non-Task data from "
						+ _socket.getInetAddress());
			}
			} // end if null
		} catch (IOException e) {
			System.err.println("Error when communicating with "
					+ _socket.getInetAddress());
		}
		
		
		
		
	}

}
