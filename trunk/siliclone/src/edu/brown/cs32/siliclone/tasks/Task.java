/**
 * The Task interface is what any user of the Tasks framework will
 * implement by implementing the compute() method.
 */
package edu.brown.cs32.siliclone.tasks;

import java.io.Serializable;


public interface Task extends Serializable {
	
	/**
	 * Performs some computationally intensive task defined by
	 * the implementing class. It could alter the class somehow
	 * as a way to return data to the original caller.
	 */
	public void compute();
	
}
