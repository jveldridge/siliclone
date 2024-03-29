package edu.brown.cs32.siliclone.server;

import java.io.IOException;
import java.net.UnknownHostException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.operators.anothertestop.SquareService;
import edu.brown.cs32.siliclone.tasks.SquareTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;
import edu.brown.cs32.siliclone.tasks.client.TaskTimedOutException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SquareServiceImpl extends RemoteServiceServlet implements
		SquareService {

	public Integer square(Integer in) throws IllegalArgumentException {
	// TODO Auto-generated method stub
		
		SquareTask remoteTaskIn = new SquareTask();
		remoteTaskIn.numberToBeSquared = in;
		SquareTask remoteTaskOut = null;
		try {
			
			TaskClient taskClient = new TaskClient("everett-77.resnet.brown.edu", 2010);
			
			remoteTaskOut = (SquareTask) taskClient.computeTask(remoteTaskIn);
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskTimedOutException e){
			System.err.println(e);
			e.printStackTrace();
		}
		
		
		return remoteTaskOut.squaredNumber;
	}



}
