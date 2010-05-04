package edu.brown.cs32.siliclone.client.visualizers;

public class DataVisualizerTemplate implements VisualizerTemplate {

	public String getName() {
		return "Visualizer for Debugging";
	}


	public Visualizer makeVisualizer() {
		return new DataVisualizer();
	}

}
