package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;
import edu.brown.cs32.siliclone.operators.Operator;

public class SequenceVisualizer extends VisualizerCanvas {

	private Label contents;
	public SequenceVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}
	
	public void update() {
		if(contents == null){
			VLayout layout = new VLayout();
			contents = new Label();
			contents.setText("");
			layout.addMember(contents);
			this.addChild(layout);
		}
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		
		if(seqs == null || seqs.isEmpty()){
			contents.setText("Add more input to get results from this operator.");
		}else {
			final Button string = new Button("hi");
			AsyncCallback<String> callback = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				
				public void onSuccess(String result) {
					contents.setText(result);
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
