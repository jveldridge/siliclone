package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.tasks.Task;

public interface TaskServiceAsync {

	void processTask(Task t, AsyncCallback<Task> callback);

}
