package edu.brown.cs32.siliclone.client.visualizers2.translation;

import java.util.Collection;

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

	private VLayout contents;
	private TranslationServiceAsync service;

	public String getName() {
		return "Translation Visualization";
	}
	
	

	public void update() {
		if (contents == null) {
			contents = new VLayout();
			contents.setHeight100();
			contents.setWidth100();
			this.clear();
			this.setContents("");
			this.addChild(contents);
		}
		contents.clear();
		System.out.println("clearing");
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		
		if(seqs == null || seqs.isEmpty()){
			contents.addMember(new Label("<h1> No sequence </h1>"));
		
		}else {
			Label l1 = new Label();
			Label l2 = new Label();
			Label l3 = new Label();

			System.out.println("adding");
			contents.addMember(new Label("<h1><u> 5'3' Frame 1  </u></h1>"));
			contents.addMember(l1);
			contents.addMember(new Label("<h1><u> 5'3' Frame 2  </u></h1>"));
			contents.addMember(l2);
			contents.addMember(new Label("<h1><u> 5'3' Frame 3  </u></h1>"));
			contents.addMember(l3);
			
			if (service == null) {
				service = GWT.create(TranslationService.class);
			}
			
			service.getForwardTranslationOne(seqs.iterator().next(), new updateLabelCallback(l1));
			service.getForwardTranslationTwo(seqs.iterator().next(), new updateLabelCallback(l2));
			service.getForwardTranslationThree(seqs.iterator().next(), new updateLabelCallback(l3));
		}
		this.redraw();
	}
	
	private class updateLabelCallback implements AsyncCallback<String> {
		private Label l;
		public updateLabelCallback(Label l){
			this.l = l;
		}
		public void onFailure(Throwable caught) {
			SC.say(caught.getMessage());
		}
		public void onSuccess(String result) {
			l.setContents("<h3>" + result+ "</h3>");
		}
	}

}
