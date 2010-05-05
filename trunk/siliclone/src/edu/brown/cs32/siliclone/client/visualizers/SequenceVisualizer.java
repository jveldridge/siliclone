package edu.brown.cs32.siliclone.client.visualizers;

import java.util.Collection;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;

public class SequenceVisualizer implements Visualizer {

	private SequenceServiceAsync _service = GWT.create(SequenceService.class);
	
	public Widget visualize(Collection<SequenceHook> seq) {
		final Button string = new Button("hi");
		final DrawingArea toReturn = new DrawingArea(600,600);
		if(seq == null){
			toReturn.add(new Text(20,50, "No sequence"));
			Text text = new Text( 0, 100, ":(");
			text.setFillColor("#ff0000");
			text.setFontSize(18);
			toReturn.add(text);
			return toReturn;
		
		}
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}

			public void onSuccess(String result) {
				System.out.println(result);
				string.setTooltip(result);
				string.setContents(result);
				string.setTitle(result);
				toReturn.add(new Text(20,50,result));
			}
			
		};
		_service.getNucleotides(seq.iterator().next(), callback);
		
		return toReturn;
	}

	public String getName() {
		return "Sequence Visualization";
	}

}
