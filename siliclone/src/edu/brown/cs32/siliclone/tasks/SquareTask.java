/**
 * Just a simple example of a task. The compute() should do something
 * computationally intensive, ideally.
 */
package edu.brown.cs32.siliclone.tasks;

public class SquareTask implements Task {

	public int numberToBeSquared;
	public int squaredNumber;
	
	public void compute() {
		squaredNumber = numberToBeSquared * numberToBeSquared;

	}

}