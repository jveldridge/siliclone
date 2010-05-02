package edu.brown.cs32.siliclone.client.visualization;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.dna.SequenceHook;

public interface Visualizer {
	public Widget visualize(SequenceHook seq);
	public String getName();
}
