package edu.brown.cs32.siliclone.database.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.database.client.TaskService;
import edu.brown.cs32.siliclone.tasks.Task;

public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	@Override
	public Task processTask(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

}
