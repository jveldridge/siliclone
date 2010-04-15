package edu.brown.cs32.siliclone.tasks.workernode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.brown.cs32.siliclone.tasks.Task;

public class WorkerNode {

	private static final boolean DEBUG=true;
	/**
	 * WorkerNode <host> <port>
	 */
	public static void main(String[] args) {
		if(args.length!=2){
		
			System.err.println("Usage: WorkerNode <host> <port>");
			System.exit(1);
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		try {
			Socket socket = new Socket(host, port);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Object incomingObject;
			try {
				incomingObject = ois.readObject();
				if(incomingObject==null){
					if(DEBUG){
						System.out.println("No work to be done. Exiting...");
						System.exit(0);
					}
				}else{
				if (!(incomingObject instanceof Task)) {
					throw new ClassNotFoundException();
				}
				Task incomingTask = (Task) incomingObject;
				
				incomingTask.compute(); //this is where it happens!
				
				oos.writeObject(incomingTask);
				oos.flush();
				
				}
				
			} catch (ClassNotFoundException e) {
				System.err.println("Ignored non-Task data from "
						+ socket.getInetAddress());
			}
			
			
			
			
			
		} catch (UnknownHostException e) {
			System.err.println("Could not connect to "+host+":"+port);
		} catch (IOException e) {
			System.err.println("Error while communicating with "+host+":"+port);
		}
		
		
		
		

	}

}
