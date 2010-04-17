
package edu.brown.cs32.siliclone.tasks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The WorkerNodeListener is run in a seperate Thread in the TaskServer to listen for incoming WorkerNode connections
 * @author tderond
 */
public class WorkerNodeListener implements Runnable{
	
	private int _port;
	private TaskScheduler _scheduler;
	private static final boolean DEBUG = true;
	
	public WorkerNodeListener(int port, TaskScheduler scheduler){
		_port=port;
		_scheduler = scheduler;
	}

	/**
	 * listen for incoming WorkerNode connections.
	 * Upon arrival of a new WorkerNode connection, let a WorkerNodeHandler
	 * determine what work the Node needs to do 
	 */
	public void run() {
		try {
			ServerSocket listeningSocket = new ServerSocket(_port);
			
			while(true){
				WorkerNodeHandler handler = new WorkerNodeHandler(listeningSocket.accept(),_scheduler);
				new Thread(handler, "WorkerNodeHandler").start();
				if(DEBUG){
					System.out.println("A new worker node connected");
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("Encountered a problem while running WorkerNodeListener on port "+_port);
			System.err.println("Exiting...");
			System.exit(2);
		}

		
		
		
		
	}

}
