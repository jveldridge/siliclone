package edu.brown.cs32.siliclone.client.visualization;

import java.util.ArrayList;
import java.util.Collection;

public class VisualizationOptions {
	public static Collection<Visualizer> getVisualizers(){
		ArrayList<Visualizer> visualizers = new ArrayList<Visualizer>();
		visualizers.add(new DataVisualizer());
		visualizers.add(new SequenceVisualizer());
		return visualizers;
	}
}
