package edu.brown.cs32.siliclone.tasks.workerdispatcher;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import com.jcraft.jsch.*;

import edu.brown.cs32.siliclone.tasks.Task;

public class WorkerDispatcher {
	
	private static Object lock;
	
	private static Session sshSession;
	
	private static boolean useremote = false;

	/**
	 * WorkerDispatcher <host> <port> [<ssh-server> <ssh-user> <ssh-password> <server-session-length>]
	 */
	public static void main(String[] args) {
		if(args.length!=2&&args.length!=6){
		
			System.err.println("WorkerDispatcher <host> <port> [<ssh-server> <ssh-user> <ssh-password> <server-session-length>]");
			System.exit(1);
		}
		String host = args[0];

		
		
		int port = Integer.parseInt(args[1]);
		
		
		

		
		if(args.length==6){
			
			useremote = true;
			server = args[2];
			user = args[3];
			password = args[4];
			timeout = Integer.parseInt(args[5]);
			
	        JSch jsch=new JSch();  


	        try {
				sshSession=jsch.getSession(user, server, 22);
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //password will be given via UserInfo interface.
	        UserInfo ui=new MyUserInfo(password);
	        sshSession.setUserInfo(ui);
			
		}
		
		
		
		
		
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
		
		if(useremote){
			synchronized (lock) {
				
				sshConnect();
		        try {
		        Channel channel=sshSession.openChannel("exec");
		        ((ChannelExec)channel).setCommand("sh dispatch.sh");
		        
				channel.connect();
		        channel.disconnect();
				
				} catch (JSchException e) {
					System.err.println("This is not good.");
					e.printStackTrace();
				}
				}
		}else{
			try {
				Process p = Runtime.getRuntime().exec("/bin/bash dispatch.sh");
			} catch (IOException e) {
				System.err.println("Error executing dispatch script");
			}
		}

	}
	
	
	private static String server, user, password;
	private static int timeout;
	
	private static void sshConnect(){
		
		synchronized(lock){
	    try{
	    	if(!sshSession.isConnected()){
	        sshSession.connect();
	        System.out.println("Ssh session connected");
	        new Thread(new TimeOutRunner()).start();
	    	}
	      }
	      catch(Exception e){
	        System.out.println(e);
	      }
	    }
	}
	
	private static class TimeOutRunner implements Runnable{

		@Override
		public void run() {
			try {
				Thread.sleep(timeout*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (lock) {
				sshSession.disconnect();
				System.out.println("Ssh session disconnected");
			}
			
		}
		
		
	}

	    private static class MyUserInfo implements UserInfo{
	    	public MyUserInfo(String password){
	    		super();
	    		passwd=password;
	    	}
	      public String getPassword(){ return passwd; }
	      public boolean promptYesNo(String str){
	    	  System.out.println("promptYesNo "+str);
	         return true;
	      }
	    
	      private String passwd;

	      public String getPassphrase(){ return null; }
	      public boolean promptPassphrase(String message){ return true; }
	      public boolean promptPassword(String message){
	          return true;
	        }
	      public void showMessage(String message){
	    	  System.out.println("showMessage "+message);
	      }

		
		
	}
	
	
}

