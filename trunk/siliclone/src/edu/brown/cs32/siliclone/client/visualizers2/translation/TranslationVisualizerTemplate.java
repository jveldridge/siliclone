package edu.brown.cs32.siliclone.client.visualizers2.translation;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerCanvas;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerTemplate;
import edu.brown.cs32.siliclone.operators.Operator;

public class TranslationVisualizerTemplate implements VisualizerTemplate {

	public String getName() {
		return "Translation Visualizer";
	}

	public VisualizerCanvas makeVisualizer(WorkspaceView w, Operator o) {
		return new TranslationVisualizer(w, o);
	}

}
