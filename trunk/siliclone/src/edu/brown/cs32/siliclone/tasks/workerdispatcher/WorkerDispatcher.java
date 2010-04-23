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
	
	private static Object lock = new Object();
	
	private static Session sshSession;
	
	private static boolean useremote = false;

	/**
	 * WorkerDispatcher <host> <port> <command>[<ssh-server> <ssh-user> <ssh-password> <server-session-length> <ssh-identity-file>]]
	 */
	public static void main(String[] args) {
		if(args.length!=3&&args.length!=7&args.length!=8){
		
			System.err.println("WorkerDispatcher <host> <port> <command>[<ssh-server> <ssh-user> <ssh-password> <server-session-length> [<ssh-identity-file>]]");
			System.exit(1);
		}
		host = args[0];

		
		
		port = Integer.parseInt(args[1]);
		
	//	command = "java -cp "+args[2]+" edu.brown.cs32.siliclone.tasks.workernode.WorkerNode "+host+" "+port+" &";
		command = args[2];
		
		if(args.length==7||args.length==8){
			
			useremote = true;
			server = args[3];
			user = args[4];
			String password = args[5];
			timeout = Integer.parseInt(args[6]);
			
	        JSch jsch=new JSch();  
	        if(args.length==8){
	        	try {
					jsch.addIdentity(args[7]);
				} catch (JSchException e) {
					System.err.println("Error: "+e.getMessage());
					e.printStackTrace();
				}
	        }

	        try {
				sshSession=jsch.getSession(user, server, 22);
			} catch (JSchException e) {
				e.printStackTrace();
			}
	        
	        //password will be given via UserInfo interface.
	        UserInfo ui=new MyUserInfo(password);
	        sshSession.setUserInfo(ui);
			
		}
		
		
		Socket socket = null;
		try {
			socket = new Socket(host, port+1);
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
			System.err.println("Could not connect to "+host+":"+(port+1));
		} catch (IOException e) {
			System.err.println("Error while communicating with "+host+":"+(port+1));
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
				if(!sshSession.isConnected()){
					System.err.println("Not connected to SSH server after trial.\nThis probably means the SSH server is down or you didn't give the right credentials\nDispatch failed.");
					return;
				}
		        try {
		        Channel channel=sshSession.openChannel("exec");
		     // ((ChannelExec)channel).setCommand("sh dispatch.sh");
		    //  ((ChannelExec)channel).setCommand("rename 's/track/track1/' track*");
		      ((ChannelExec)channel).setCommand(command);
		        
		        
				channel.connect();
				System.out.println("Remote Dispatch happened");
				

				
				
				
				/*
				try{
					
			      channel.setInputStream(null);

			      ((ChannelExec)channel).setErrStream(System.err);

			      InputStream in=channel.getInputStream();

			      channel.connect();
			     

			      byte[] tmp=new byte[1024];
			      while(true){
			        while(in.available()>0){
			          int i=in.read(tmp, 0, 1024);
			          if(i<0)break;
			          System.out.print(new String(tmp, 0, i));
			        }
			        if(channel.isClosed()){
			          System.out.println("exit-status: "+channel.getExitStatus());
			          break;
			        }
			        try{Thread.sleep(1000);}catch(Exception ee){}
			      }
				}catch(IOException e){
					
					System.err.println("oops");
					e.printStackTrace();
				}
				
				
				
				*/
				
				
				
				
		        channel.disconnect();
				
				} catch (JSchException e) {
					System.err.println("This is not good.");
					e.printStackTrace();
				}
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}else{
			try {
				Process p = Runtime.getRuntime().exec(command);
				System.out.println("Local Dispatch happened");
			} catch (IOException e) {
				System.err.println("Error executing dispatch script");
			}
		}

	}
	
	
	private static String server, user, host,command;
	private static int timeout, port;
	
	private static void sshConnect(){
		
		synchronized(lock){
	    try{
	    	if(!sshSession.isConnected()){
	    	System.out.println("Connecting to Ssh server "+sshSession.getHost());
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

		public void run() {
			try {
				Thread.sleep(timeout*1000);
			} catch (InterruptedException e) {
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
	      public String getPassword(){ System.out.println("password sent");return passwd; }
	      public boolean promptYesNo(String str){
	    	  System.out.println(str+" Yes.");
	         return true;
	      }
	    
	      private String passwd;

	      public String getPassphrase(){ System.out.println("passphrase sent");return passwd; }
	      public boolean promptPassphrase(String message){return true; }
	      public boolean promptPassword( String message){
	          return true;
	        }
	      public void showMessage(String message){
	    	  System.out.println(message);
	      }

		
		
	}
	
	
}

