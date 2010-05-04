package edu.brown.cs32.siliclone.client.visualizers;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.plugins.Plugins;


public class VisualizerDisplay extends TabSet {
	
	Operator sourceOperator;
	
	
	public VisualizerDisplay(final Operator op){
		/*if(op.getOutputSequence() == null){
			addChild(new Label("There is no output sequence defined."));
			return;
		}*/
			
		
		sourceOperator = op;
		final Tab t = new Tab("New Visualizers");
		final VLayout content = new VLayout();
		final SelectItem visualizers = new SelectItem();
		visualizers.setTitle("Select a visualizer");
		Plugins.defineVisualizers(new VisualizerAdderImpl(content));

		t.setPane(content);
		content.setHeight100();
		content.setWidth100();
		addTab(t);
		}
	

	private Tab newVisualizer(final Visualizer v){

		final Tab t = new Tab(v.getName());
		final VLayout content = new VLayout();
		
		final Button printButton = new Button("Print");
		printButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				final Window win = new Window();
				
				win.setTitle("Visualization: ");
				
				win.setHeight100();
				win.setWidth100();
				Widget l = v.visualize(sourceOperator.getOutputSequence());
				
				win.addItem(l);
				
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
				content.addChild(v.visualize(sourceOperator.getOutputSequence()));
				//content.addChild(v.Visualize(new SequenceHook(123, 456, "Sequence 1")));
				content.addChild(printButton);
			}
		});
		
		content.addChild(runButton);
		t.setPane(content);
		content.setHeight100();
		content.setWidth100();
		addTab(t);
		
		return t;
		
		
	}
	
	
		private class VisualizerAdderImpl implements VisualizerAdder {
		
			Layout layout;
			
			public VisualizerAdderImpl(Layout l){
				layout = l;
			}
		
		public void addVisualizer(final VisualizerTemplate toBeAdded){
			Button b = new Button(toBeAdded.getName());
			b.addClickHandler(new ClickHandler() {
				
				
				public void onClick(ClickEvent event) {
					newVisualizer(toBeAdded.makeVisualizer());
					
				}
			});
			b.setWidth100();
			layout.addMember(b);
		
			}
	
	
	
}
}
