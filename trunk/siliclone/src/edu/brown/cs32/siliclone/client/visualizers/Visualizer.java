package edu.brown.cs32.siliclone.client.visualizers;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface Visualizer {
	public Widget visualize(Collection<SequenceHook> seq);
	public String getName();
}
