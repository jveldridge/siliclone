package edu.brown.cs32.siliclone.client.visualizers2;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.operators.Operator;



public class SequenceVisualizerTemplate implements VisualizerTemplate {

	public String getName() {
		return "Sequence Visualizer";
	}
	public VisualizerCanvas makeVisualizer(WorkspaceView w, Operator o) {
		return new SequenceVisualizer(w, o);
	}


}
