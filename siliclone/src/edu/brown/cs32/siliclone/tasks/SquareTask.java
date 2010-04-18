/**
 * Just a simple example of a task. The compute() should do something
 * computationally intensive, ideally.
 */
package edu.brown.cs32.siliclone.tasks;

public class SquareTask implements Task {

	public int numberToBeSquared;
	public int squaredNumber;
	
	public void compute() {
		for(int j = 0;j<numberToBeSquared;j++){
		for(int i = 0;i<numberToBeSquared;i++){
			Math.pow(i,j); //just something to make this thing computationally intensive
		}
		}
		squaredNumber = numberToBeSquared * numberToBeSquared;

	}

}