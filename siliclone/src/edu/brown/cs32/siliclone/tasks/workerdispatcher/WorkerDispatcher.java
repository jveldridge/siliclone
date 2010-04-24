package edu.brown.cs32.siliclone.tasks.workerdispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.tools.ant.taskdefs.Sleep;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class WorkerDispatcher {
	
	private static Object lock = new Object();
	
	private static Session sshSession;
	
	private static boolean useremote = false;
	private static MyUserInfo ui;
	private static JSch jsch;
	
	/**
	 * WorkerDispatcher <host> <port> <command>[<ssh-server> <ssh-user> <server-session-length> <ssh-identity-file>]]
	 */
	public static void main(String[] args) {
		if(args.length!=3&&args.length!=6&args.length!=7){
		
			System.err.println("WorkerDispatcher <host> <port> <command>[<ssh-server> <ssh-user> <server-session-length> [<ssh-identity-file>]]");
			System.exit(1);
		}
		host = args[0];
		
		
		
		port = Integer.parseInt(args[1]);
		
	//	command = "java -cp "+args[2]+" edu.brown.cs32.siliclone.tasks.workernode.WorkerNode "+host+" "+port+" &";
		command = args[2];
		
		if(args.length==6||args.length==7){
			
			useremote = true;
			server = args[3];
			user = args[4];
			//String password = args[5];
			
			
			
			
			
			
			
			
			System.out.println("Please enter password. Leave blank if not necessary");
		    
			/*
			 *The following code comes from http://java.sun.com/developer/technicalArticles/Security/pwordmask/ 
			 */
			EraserThread et = new EraserThread("Password:");
		      Thread mask = new Thread(et);
		      mask.start();

		      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		      String password = "";

		      try {
		         password = in.readLine();
		      } catch (IOException ioe) {
		        ioe.printStackTrace();
		      }
		      // stop masking
		      et.stopMasking();

		      /*
		       * Now it's our own code again
		       */
		
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			timeout = Integer.parseInt(args[5]);
			
	        jsch=new JSch();  
	        if(args.length==7){
	        	try {
					jsch.addIdentity(args[6]);
				} catch (JSchException e) {
					System.err.println("Error: "+e.getMessage());
					e.printStackTrace();
				}
	        }
	        
	        //password will be given via UserInfo interface.
	        ui=new MyUserInfo(password);
	        
	        sshConnect();
	        
			
		}
		
		
		Socket socket = null;
		try {
			System.out.println("Connecting to TaskServer...");
			socket = new Socket(host, port+1);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("Connected to Server, now waiting for dispatch requests");
			int incomingInt;
			while(true){
				incomingInt = ois.readInt();
				for(int i =0;i<incomingInt;i++){
	
					dispatch();
				
				}
			}
			
			
			
			
			
		} catch (UnknownHostException e) {
			System.err.println("Could not connect to "+host+":"+(port+1));
			System.exit(3);
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
				if(sshSession==null||!sshSession.isConnected()){
					System.err.println("Not connected to SSH server after trial.\nThis probably means the SSH server is down or you didn't give the right credentials\nDispatch failed.");
					return;
				}
				
				while(true){
				Channel channel=null;
		        
				try {
		        	
		        channel=sshSession.openChannel("exec");
		        
		      	
						

		        
		        
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
		        break;
		        
		        
				
				} catch (JSchException e) {
					if(channel.isClosed()){
						break;
					}
					System.err.println("Problem dispatching job");
					e.printStackTrace();
				}
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
	    	if(sshSession==null||!sshSession.isConnected()){
	    		
	    	System.out.println("Connecting to Ssh server "+server);
			sshSession=jsch.getSession(user, server, 22);
			sshSession.setUserInfo(ui);
	        sshSession.connect();
	        System.out.println("Ssh session connected");
	        new Thread(new TimeOutRunner()).start();
	    	}
	      }
	      catch(JSchException e){
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
	    
	    
	    
	    
	    
	    
	    
	    
	    /**
	     * From http://java.sun.com/developer/technicalArticles/Security/pwordmask/
	     * masks the password input
	     *
	     */
	    private static class EraserThread implements Runnable {
	    	   private boolean stop;
	    	 
	    	   /**
	    	    *@param The prompt displayed to the user
	    	    */
	    	   public EraserThread(String prompt) {
	    	       System.out.print(prompt);
	    	   }

	    	   /**
	    	    * Begin masking...display asterisks (*)
	    	    */
	    	   public void run () {
	    	      stop = true;
	    	      while (stop) {
	    	         System.out.print("\010*");
	    		 try {
	    		    Thread.currentThread().sleep(1);
	    	         } catch(InterruptedException ie) {
	    	            ie.printStackTrace();
	    	         }
	    	      }
	    	   }

	    	   /**
	    	    * Instruct the thread to stop masking
	    	    */
	    	   public void stopMasking() {
	    	      this.stop = false;
	    	   }
	    	}

	    
	    
	    
	    
	    
	
	
}

