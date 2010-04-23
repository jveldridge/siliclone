

package edu.brown.cs32.siliclone.tasks.server;

/**
 * The TaskServer listens to connections from TaskClients, WorkerDispatchers, and WorkerNodes,
 * and starts threads to handle their needs.
 */
public class TaskServer {

    /**
     * Usage: TaskServer <port> [<worker-timeout> [<dispatch-timeout]]
     */
    public static void main(String[] args) {
        
    	if(args.length!=1&&args.length!=2&&args.length!=3){
    		
			System.err.println("Usage: TaskServer <port> [<worker-timeout> [<dispatch-timeout]]");
			System.exit(1);
		}
    	int workertimeout=30;
    	int dispatchtimeout=3;
    	if(args.length==3){
    		dispatchtimeout = Integer.parseInt(args[2]);
    	}
    	if(args.length>=2){
    		workertimeout = Integer.parseInt(args[1]);
    	}
    	
    	int taskClientPort = Integer.parseInt(args[0]);
    	int workerDispatcherPort = taskClientPort+1;
    	int workerNodePort = taskClientPort+2;
    	
    	WorkerDispatcherListener workerDispatcherListener = new WorkerDispatcherListener(workerDispatcherPort);
    	
    	TaskScheduler scheduler = new TaskScheduler(workerDispatcherListener,dispatchtimeout,workertimeout);
    	
    	TaskClientListener taskClientListener = new TaskClientListener(taskClientPort, scheduler);    	
    	
    	WorkerNodeListener workerNodeListener = new WorkerNodeListener(workerNodePort, scheduler);
    	
    	new Thread(workerDispatcherListener,"WorkerDispatcherListener").start();
    	new Thread(taskClientListener, "TaskClientListener").start();
    	new Thread(workerNodeListener,"WorkerNodeListener").start();
    	
    	System.out.println("TaskServer now listening on ports "+taskClientPort+", "+workerDispatcherPort+", and "+workerNodePort);
    }

}
