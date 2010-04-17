/**
 * A request is a wrapper of a Task that can be identified as identical
 * even if the exact contents of the containing Task has changed (i.e. after
 * compute() is called on a task)
 */
package edu.brown.cs32.siliclone.tasks;

import java.io.Serializable;
import java.util.Random;

public class Request implements Serializable{
	
	Task _task;
	int _ID;
	int _ID2;
	
	public Request(){
		
	}
	
	public Request(Task task){
		_task=task;
		_ID = task.hashCode();
		_ID2 = new Random().nextInt(Integer.MAX_VALUE);
	}
	
	public Task getTask(){
		return _task;
	}
	
	public void setTask(Task task){
		_task = task;
	}
	
	@Override
	public int hashCode(){
		return _ID;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Request)){
			return false;
		}
		Request otherRequest = (Request) o;
		return(otherRequest._ID==this._ID&&otherRequest._ID2==this._ID2);
	}

}
