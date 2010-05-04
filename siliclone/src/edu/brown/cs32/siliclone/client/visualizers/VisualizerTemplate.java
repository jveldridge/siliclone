package edu.brown.cs32.siliclone.client.visualizers;

import com.google.gwt.user.client.ui.Widget;


public interface VisualizerTemplate {
	
	/**
	 * Returns an actual instance of this Visualizer to be used in the workspace.
	 * Will be called when the VisualizerTemplate is dragged from the selection
	 * tree on the left into the workspace.
	 * 
	 * @return an actual instance of this Visualizer to be used in the workspace.
	 */
	public Visualizer makeVisualizer();
	
	/**
	 * Returns the name of the Visualizer with which this VisualizerTemplate is 
	 * associated.
	 * 
	 * @return the name of the Visualizer with which this VisualizerTemplate is 
	 * associated.
	 */
	public String getName();
}
