package edu.brown.cs32.siliclone.client.visualizers2;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.visualizers2.DataVisualizer;
import edu.brown.cs32.siliclone.operators.Operator;

public class DataVisualizerTemplate implements VisualizerTemplate {

	public String getName() {
		return "Visualizer for Debugging";
	}

	public VisualizerCanvas makeVisualizer(WorkspaceView w, Operator o) {
		return new DataVisualizer(w, o);
	}}
