package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.Collection;



public interface VisualizerAdder { 
	public void addVisualizer(VisualizerTemplate visualizerFactory);
	public Collection<VisualizerTemplate> getVisualizers();
}
