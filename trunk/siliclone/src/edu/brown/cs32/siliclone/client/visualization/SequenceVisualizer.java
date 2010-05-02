package edu.brown.cs32.siliclone.client.visualization;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.dna.SequenceHook;

public class SequenceVisualizer implements Visualizer {

	public Widget Visualize(SequenceHook seq) {
		DrawingArea toReturn = new DrawingArea(600,600);
		if(seq == null){
			toReturn.add(new Text(20,50, "Name: Sequence 0"));
			Text text = new Text( 0, 100, "actg");
			text.setFillColor("#ff0000");
			text.setFontSize(35);
			toReturn.add(text);
			return toReturn;
		
		}
		return new Label("Not yet implemented");
	}

	public String getName() {
		return "Sequence Visualization";
	}

}
