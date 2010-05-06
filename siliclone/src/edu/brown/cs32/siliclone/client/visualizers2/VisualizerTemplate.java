package edu.brown.cs32.siliclone.client.visualizers2;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.visualizers.Visualizer;
import edu.brown.cs32.siliclone.operators.Operator;


public interface VisualizerTemplate {
	
	/**
	 * Returns an actual instance of this Visualizer to be used in the workspace.
	 * Will be called when the VisualizerTemplate is dragged from the selection
	 * tree on the left into the workspace.
	 * 
	 * @return an actual instance of this Visualizer to be used in the workspace.
	 */
	public VisualizerCanvas makeVisualizer(WorkspaceView w, Operator o);
	
	/**
	 * Returns the name of the Visualizer with which this VisualizerTemplate is 
	 * associated.
	 * 
	 * @return the name of the Visualizer with which this VisualizerTemplate is 
	 * associated.
	 */
	public String getName();
}
