package edu.brown.cs32.siliclone.client.visualizers;

public class SequenceVisualizerTemplate implements VisualizerTemplate {

	public String getName() {
		return "Sequence Visualizer";
	}

	public Visualizer makeVisualizer() {
		return new SequenceVisualizer();
	}

}
