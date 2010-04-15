package edu.brown.cs32.siliclone.tasks.workerdispatcher;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.brown.cs32.siliclone.tasks.Task;

public class WorkerDispatcher {

	/**
	 * WorkerDispatcher <host> <port>
	 */
	public static void main(String[] args) {
		if(args.length!=2){
		
			System.err.println("Usage: WorkerDispatcher <host> <port>");
			System.exit(1);
		}
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Object incomingObject;
			while(true){
			try {
				incomingObject = ois.readObject();
				if (incomingObject!=null) {
					throw new ClassNotFoundException();
				}else{
	
					dispatch();
					
				}
		
				
			} catch (ClassNotFoundException e) {
				System.err.println("Ignored non-null data from "
						+ socket.getInetAddress());
			}
			}
			
			
			
			
		} catch (UnknownHostException e) {
			System.err.println("Could not connect to "+host+":"+port);
		} catch (IOException e) {
			System.err.println("Error while communicating with "+host+":"+port);
			if(socket==null||!socket.isConnected()){
				System.err.println("The server went down. Please restart the server and then restart this WorkerDispatcher.");
				System.exit(3);
			}
		}
		
		
		
		

	}

	private static void dispatch(){
		//dispatch a new worker.
		
		 
		try {
			Process p = Runtime.getRuntime().exec("/bin/bash dispatch.sh");
		} catch (IOException e) {
			System.err.println("Error executing dispatch script");
		}


	}
	
	
}

