package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.brown.cs32.siliclone.tasks.Task;

public interface TaskService extends RemoteService {
	Task processTask(Task t);
}
