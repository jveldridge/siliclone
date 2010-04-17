/*
 * IncomingTaskListener
 */

package edu.brown.cs32.siliclone.tasks.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author tderond
 */
public class WorkerDispatcherListener implements Runnable{
	
	private int _port;
	private LinkedBlockingDeque<ObjectOutputStream> _dispatchers;
	private static final boolean DEBUG =true;
	
	public WorkerDispatcherListener(int port){
		_port=port;
		_dispatchers = new LinkedBlockingDeque<ObjectOutputStream>();
	}

	public void run() {
		try {
			ServerSocket listeningSocket = new ServerSocket(_port);
			
			while(true){
				Socket socket = listeningSocket.accept();
				synchronized (_dispatchers) {
					if(!_dispatchers.offerFirst(new ObjectOutputStream(socket.getOutputStream()))){
						System.err.println("A Worker dispatcher connected to the server but could not be registered");
					}
					if(DEBUG){
					System.out.println("Number of WorkerDispatchers: "+_dispatchers.size());
					}
					}
			}
			
			
		} catch (IOException e) {
			System.err.println("Encountered a problem while running WorkerDispatcherListener on port "+_port);
			System.err.println("Exiting...");
			System.exit(2);
		}

		
		
		
		
	}
	
	public void dispatchWorker(){
		synchronized (_dispatchers) {
			ObjectOutputStream oos = _dispatchers.peek();
			if(oos!=null){
				synchronized (oos) {
					try {
						oos.writeObject(null);
						oos.flush();
					} catch (IOException e) {
						System.err.println("Error communicating with WorkerDispatcher, deleting it from the list of dispatchers, and trying again");
						_dispatchers.removeFirst();
						dispatchWorker(); //this is OK in java, since this thread already has the lock on _dispatchers
					}
					
				}
			}else{
				System.err.println("There are no registered WorkerDispatchers. Tasks can currently not be completed");
			}
		}
	}

}
