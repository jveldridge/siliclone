package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.brown.cs32.siliclone.tasks.Task;

/**
 * A task is a serializable object that is completely defined specific for an operation.
 */
public interface TaskService extends RemoteService {
	/**
	 * Runs the task on the back-end, either on the server machine or elsewhere.
	 * @param t The task to be run, annotated for persistence and runnable. not null
	 * @return The same task, after having been run and re-serialized. null if failure.
	 */
	Task processTask(Task t);
}
