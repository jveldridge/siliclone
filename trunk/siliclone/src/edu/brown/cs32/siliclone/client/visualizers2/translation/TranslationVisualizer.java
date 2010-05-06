package edu.brown.cs32.siliclone.client.visualizers2.translation;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerCanvas;
import edu.brown.cs32.siliclone.operators.Operator;

public class TranslationVisualizer extends VisualizerCanvas {
	
	private DrawingArea drawing;
	private TranslationServiceAsync service;

	public TranslationVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
		service = GWT.create(TranslationService.class);
	}

	@Override
	public String getName() {
		return "Translation Visualization";
	}

	@Override
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
			AsyncCallback<String> callback1 = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					drawing.add(new Text(20,50,"5'3' Frame 1"));
					drawing.add(new Text(20,50,result));
				}
			};
			
			if (service == null) {
				service = GWT.create(TranslationService.class);
			}
			service.getForwardTranslationOne(seqs.iterator().next(), callback1);
			
			AsyncCallback<String> callback2 = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					drawing.add(new Text(20,50,"5'3' Frame 2"));
					drawing.add(new Text(20,50,result));
				}
			};
			
			service.getForwardTranslationTwo(seqs.iterator().next(), callback2);
			
			AsyncCallback<String> callback3 = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					drawing.add(new Text(20,50,"5'3' Frame 3"));
					drawing.add(new Text(20,50,result));
				}
			};
			
			service.getForwardTranslationThree(seqs.iterator().next(), callback3);
		}

		this.redraw();
	}

}
