

package edu.brown.cs32.siliclone.tasks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The TaskClientListener is run in a seperate Thread in the TaskServer to listen for incoming TaskClient connections
 * @author tderond
 */
public class TaskClientListener implements Runnable{
	
	private int _port;
	private TaskScheduler _scheduler;
	private static final boolean DEBUG = true;
	
	public TaskClientListener(int port, TaskScheduler scheduler){
		_port=port;
		_scheduler = scheduler;
	}

	/**
	 * listens for incoming TaskClients and runs a TaskClientHandler for each incoming connection
	 */
	public void run() {
		try {
			ServerSocket listeningSocket = new ServerSocket(_port);
			
			while(true){
				TaskClientHandler handler = new TaskClientHandler(listeningSocket.accept(),_scheduler);
				new Thread(handler,"TaskClientHandler").start();
				if(DEBUG){
					System.out.println("A new task client connected");
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("Encountered a problem while running IncomingTaskListener on port "+_port);
			System.err.println("Exiting...");
			System.exit(2);
		}

		
		
		
		
	}

}
