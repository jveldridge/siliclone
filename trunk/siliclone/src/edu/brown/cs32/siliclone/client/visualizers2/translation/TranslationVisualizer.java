package edu.brown.cs32.siliclone.client.visualizers2.translation;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerCanvas;
import edu.brown.cs32.siliclone.operators.Operator;

public class TranslationVisualizer extends VisualizerCanvas {
	
	public TranslationVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}

	private StaticTextItem text;
	private TranslationServiceAsync service;
	private String message1;
	private String message2;
	private String message3;
	
	public String getName() {
		return "Translation Visualization";
	}

	public void update() {
		if (text == null) {
			VLayout contents = new VLayout();
			contents.setHeight100();
			contents.setWidth100();
			this.clear();
			text = new StaticTextItem("");
			text.setWidth(300);
			DynamicForm form = new DynamicForm();
			contents.addChild(form);
			form.setFields(text);
			
			this.setContents("");
			this.addChild(contents);
		}
		

		message1 = message2 = message3 = "";
		
		if (service == null) {
			service = GWT.create(TranslationService.class);
		}
		
		Collection<SequenceHook> seqs = owner.getOutputSequence();
		
		if(seqs == null || seqs.isEmpty()){
			text.setValue("<h1> No sequence </h1>");
		}else {
			message1 = "<h1><u> 5'3' Frame 1  </u></h1> \n";
			message2 += "<h1><u> 5'3' Frame 2  </u></h1> \n";
			message3 += "<h1><u> 5'3' Frame 3  </u></h1> \n";
			text.setValue(message1 + message2 + message3);
			
			AsyncCallback<String> updateMessage1 = new AsyncCallback<String>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(String result) {
					message1 += "<h3>" + result.replace("", " ") + "</h3>\n";
					text.setValue(message1 + message2 + message3);
				}
			};
			AsyncCallback<String> updateMessage2 = new AsyncCallback<String>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(String result) {
					message2 += "<h3>" + result.replace("", " ") + "</h3>\n";
					text.setValue(message1 + message2 + message3);
				}
			};
			AsyncCallback<String> updateMessage3 = new AsyncCallback<String>(){
				public void onFailure(Throwable caught) {
					SC.say(caught.getMessage());
				}
				public void onSuccess(String result) {
					message3 += "<h3>" + result.replace("", " ") + "</h3> \n";
					text.setValue(message1 + message2 + message3);
				}
			};
			
			

			
			service.getForwardTranslationOne(seqs.iterator().next(), updateMessage1);
			service.getForwardTranslationTwo(seqs.iterator().next(), updateMessage2);
			service.getForwardTranslationThree(seqs.iterator().next(), updateMessage3);
		}
	}
}
