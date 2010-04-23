package edu.brown.cs32.siliclone.tasks.client;

import edu.brown.cs32.siliclone.tasks.Task;

public class TaskTimedOutException extends Exception {

	private Task _task;
	
	public TaskTimedOutException(Task task
			) {
		_task = task;
	}
	
	public Task getTask(){
		return _task;
	}
	
	public String toString(){
		return "TaskTimedOutException: The Task '"+_task+"' timed out";
	}
	
	public String getMessage() {
		return super.getMessage()+" The Task '"+_task+"' timed out";
	}
	
}
