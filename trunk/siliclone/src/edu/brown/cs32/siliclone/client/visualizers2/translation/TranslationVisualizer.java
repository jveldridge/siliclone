package edu.brown.cs32.siliclone.client.visualizers2.translation;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerCanvas;
import edu.brown.cs32.siliclone.operators.Operator;

public class TranslationVisualizer extends VisualizerCanvas {
	
	public TranslationVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}

	private VLayout drawing;
	private TranslationServiceAsync service;

	@Override
	public String getName() {
		return "Translation Visualization";
	}

	@Override
	public void update() {
		if (drawing == null) {
			drawing = new VLayout();
			this.addChild(drawing);
		}
		drawing.clear();
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		
		if(seqs == null || seqs.isEmpty()){
			drawing.addMember(new Label("No sequence"));
		
		}else {
			AsyncCallback<String> callback1 = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					drawing.addMember(new Label("5'3' Frame 1:"));
					drawing.addMember(new Label(result));
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
					drawing.addMember(new Label("5'3' Frame 2:"));
					drawing.addMember(new Label(result));
				}
			};
			
			service.getForwardTranslationTwo(seqs.iterator().next(), callback2);
			
			AsyncCallback<String> callback3 = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					drawing.addMember(new Label("5'3' Frame 3:"));
					drawing.addMember(new Label(result));
				}
			};
			
			service.getForwardTranslationThree(seqs.iterator().next(), callback3);
		}

		this.redraw();
	}

}
