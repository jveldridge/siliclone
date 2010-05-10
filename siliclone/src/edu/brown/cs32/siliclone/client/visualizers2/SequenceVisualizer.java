package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;
import edu.brown.cs32.siliclone.operators.Operator;

public class SequenceVisualizer extends VisualizerCanvas {

	private StaticTextItem contents;
	private SequenceServiceAsync service;
	private Map<SequenceHook, String> sequences;
	
	public SequenceVisualizer(WorkspaceView workspace, Operator owner) {
		super(workspace, owner);
	}
	
	public void update() {
		if(contents == null){
			final VLayout layout = new VLayout();
			final DynamicForm form = new DynamicForm();
			contents = new StaticTextItem("");
			//contents.setWidth(width)();
			contents.setWrap(true);
			contents.setTitleOrientation(TitleOrientation.TOP);
			form.setWidth100();
			form.setFields(contents);
			contents.setWidth(300);
			layout.addChild(form);
			this.addChild(layout);
		}
		
		
		
		System.out.println("update called");
		System.out.println("contents: " + contents);
		if (service == null) {
			service = GWT.create(SequenceService.class);
		}
		
		final Collection<SequenceHook> seqs = owner.getOutputSequence();

		if(seqs == null || seqs.isEmpty()) {
			contents.setValue("Add more input to get results from this operator.");
		}
		
		else {
			sequences = new HashMap<SequenceHook, String>();
			
			for (final SequenceHook hook : seqs) {
				final AsyncCallback<Map> propertiesCallback = new AsyncCallback<Map>(){
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
					public void onSuccess(Map result) {
						String entry = sequences.get(hook);
						entry += " <br/><b> <u> Properties </u></b>: <br/> ";
						for(String name : ((Map<String, Object>) result).keySet()){
							entry += name + " : " + result.get(name) + "<br/>";
						}
						sequences.put(hook, entry);
						String toDisplay = "<h4>Sequences:</h4> <br/>";
						for(String s : sequences.values()){
							toDisplay += s;
						}
						contents.clearValue();
						contents.setValue(toDisplay);
						DynamicForm form = new DynamicForm();
						form.setFields(contents);
					}
				};
				
				AsyncCallback<String> sequenceCallback = new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}
					
					public void onSuccess(String result) {
						sequences.put(hook, "<br/> <b><u> name :  " +hook.getSeqName() + 
									"</b> </u> <br/>" + result.replaceAll("", " ") + "<br/>");
						service.getAllProperties(hook, propertiesCallback);
						SequenceVisualizer.super.setProgress(100 * (sequences.size() / seqs.size()));

						if (sequences.size() == seqs.size()) {
							for (SequenceHook hook : sequences.keySet()) {
								String text = sequences.get(hook);
								text += "";
								sequences.put(hook, text);
							}

							String toDisplay = "<h4>Sequences:</h4> <br/>";
							for(String s : sequences.values()){
								toDisplay += s;
							}
						}
					}
				};
	
				service.getNucleotides(hook, sequenceCallback);
			}
		}		
	}
	
	

	public String getName() {
		return "Sequence Visualization";
	}

}
