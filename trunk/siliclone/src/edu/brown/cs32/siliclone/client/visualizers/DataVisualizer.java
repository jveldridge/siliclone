package edu.brown.cs32.siliclone.client.visualizers;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public class DataVisualizer implements Visualizer {

	public Widget visualize(Collection<SequenceHook> seqs) {
		SequenceHook seq = seqs.iterator().next();
		DrawingArea toReturn = new DrawingArea(600,600);
		if(seq == null){
			toReturn.add(new Ellipse(50 , 50, 45, 20));
			toReturn.add(new Text(20,200, "Name: Sequence 0"));
			toReturn.add(new Text(20, 300, "Data ID: 0"));
			toReturn.add(new Text(20,400, "Sequence ID: 0"));
			return toReturn;
		}
		toReturn.add(new Ellipse(50 , 50, 45, 20));
		toReturn.add(new Text(20,200, "Name: " + seq.getSeqName()));
		toReturn.add(new Text(20, 300, "Data ID: " + seq.getDataID()));
		toReturn.add(new Text(20,400, "Sequence ID: " + seq.getSeqID()));
		return toReturn;
	}
	
	public String getName(){
		return"Data Visualization.";
	}

}
