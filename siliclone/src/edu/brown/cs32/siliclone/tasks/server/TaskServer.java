

package edu.brown.cs32.siliclone.tasks.server;

/**
 * The TaskServer listens to connections from TaskClients, WorkerDispatchers, and WorkerNodes,
 * and starts threads to handle their needs.
 */
public class TaskServer {

    /**
     * Usage: TaskServer <TaskClient port> <WorkerDispatcher port> <WorkerNode port>
     */
    public static void main(String[] args) {
        
    	if(args.length!=3){
    		
			System.err.println("Usage: TaskServer <TaskClient port> <WorkerDispatcher port> <WorkerNode port>");
			System.exit(1);
		}
    	
    	int taskClientPort = Integer.parseInt(args[0]);
    	int workerDispatcherPort = Integer.parseInt(args[1]);
    	int workerNodePort = Integer.parseInt(args[2]);
    	
    	WorkerDispatcherListener workerDispatcherListener = new WorkerDispatcherListener(workerDispatcherPort);
    	
    	TaskScheduler scheduler = new TaskScheduler(workerDispatcherListener);
    	
    	TaskClientListener taskClientListener = new TaskClientListener(taskClientPort, scheduler);    	
    	
    	WorkerNodeListener workerNodeListener = new WorkerNodeListener(workerNodePort, scheduler);
    	
    	new Thread(workerDispatcherListener,"WorkerDispatcherListener").start();
    	new Thread(taskClientListener, "TaskClientListener").start();
    	new Thread(workerNodeListener,"WorkerNodeListener").start();
    	
    }

}
