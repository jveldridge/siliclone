package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;
import edu.brown.cs32.siliclone.operators.Operator;

public class SequenceVisualizer extends VisualizerCanvas {

	private DrawingArea drawing;
	
	public SequenceVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}
	
	public void update() {
		if (drawing == null) {
			drawing = new DrawingArea(600,600);
			this.addChild(drawing);
		}
		drawing.clear();
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		
		if(seqs == null || seqs.isEmpty()){
			drawing.add(new Text(20,50, "No sequence"));
			Text text = new Text( 0, 100, ":(");
			text.setFillColor("#ff0000");
			text.setFontSize(18);
			drawing.add(text);
		
		}else {
			final Button string = new Button("hi");
			AsyncCallback<String> callback = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					System.out.println("success with result: " + result);
					string.setTooltip(result);
					string.setContents(result);
					string.setTitle(result);
					drawing.add(new Text(20,50,result));
				}
			};
			System.out.println("about to make RPC in sequencevisualizer update method");
			((SequenceServiceAsync) GWT.create(SequenceService.class)).
						getNucleotides(seqs.iterator().next(), callback);
		}

		this.redraw();
	}
	
	

	public String getName() {
		return "Sequence Visualization";
	}

}
