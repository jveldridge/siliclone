package edu.brown.cs32.siliclone.client.visualization;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.PrintCanvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;

public class VisualizerDisplay extends TabSet {
	
	
	public VisualizerDisplay(final Operator op){
		/*if(op.getOutputSequence() == null){
			addChild(new Label("There is no output sequence defined."));
			return;
		}*/
		for(final Visualizer v : VisualizationOptions.getVisualizers()){
			final Tab t = new Tab(v.getName());
			final VLayout content = new VLayout();
			
			final Button printButton = new Button("Print");
			printButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					final Window win = new Window();
					
					win.setTitle("Visualization : ");
					
					win.setHeight100();
					win.setWidth100();
					
					win.addItem(v.Visualize(op.getOutputSequence()));
					
					win.show();
					
					com.google.gwt.user.client.Window.print();
					win.hide();
					t.setPane(content);
				}
			});
			
			final Button runButton = new Button("Visualize");
			runButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					runButton.hide();
					content.addChild(v.Visualize(op.getOutputSequence()));
					content.addChild(v.Visualize(new SequenceHook(123, 456, "Sequence 1")));
					content.addChild(printButton);
				}
			});
			
			content.addChild(runButton);
			t.setPane(content);
			content.setHeight100();
			content.setWidth100();
			addTab(t);
		}
	}
}
