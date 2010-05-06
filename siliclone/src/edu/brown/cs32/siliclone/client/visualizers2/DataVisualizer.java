package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Text;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;

public class DataVisualizer extends VisualizerCanvas {

	public DataVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}

	public String getName() {
		return "Data Visualization.";
	}

	public void update() {
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		DrawingArea drawing = new DrawingArea(600,600);
		if(seqs == null){
			System.out.println("got here");
			drawing.add(new Ellipse(50 , 50, 45, 20));
			drawing.add(new Text(20,200, "Name: Sequence 0"));
			drawing.add(new Text(20, 300, "Data ID: 0"));
			drawing.add(new Text(20,400, "Sequence ID: 0"));
		}else{
			SequenceHook seq = seqs.iterator().next();
			drawing.add(new Ellipse(50 , 50, 45, 20));
			drawing.add(new Text(20,200, "Name: " + seq.getSeqName()));
			drawing.add(new Text(20, 300, "Data ID: " + seq.getDataID()));
			drawing.add(new Text(20,400, "Sequence ID: " + seq.getSeqID()));
		}
		addChild(drawing);
	}

}
