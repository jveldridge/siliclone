package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Label;

public class WidgetDisplay extends Label {
	public WidgetDisplay(String name){
		super(name);
		setShowEdges(true);
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
	}
}
