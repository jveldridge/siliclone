package edu.brown.cs32.siliclone.client.visualizers2;

import java.util.ArrayList;
import java.util.Collection;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.plugins.Plugins;

public class VisualizerDisplay extends Canvas { 
	
	private final ArrayList<VisualizerCanvas> visualizers = new ArrayList<VisualizerCanvas>();
	private final TabSet tabs = new TabSet();
	private WorkspaceView workspace;
	private Operator owner;
	
	
	public VisualizerDisplay(WorkspaceView w, Operator o){
		workspace = w;
		owner = o;
		Plugins.defineVisualizers(visualizerAdder);
		

		final Tab buttonTab = new Tab("New Visualizers");
//		final SelectItem visualizers = new SelectItem();
		buttonTab.setTitle("Select a visualizer");
		final VLayout content = new VLayout();
		
		for(final VisualizerTemplate template : visualizerAdder.getVisualizers()){
			Button b = new Button();
			b.setTitle(template.getName());
			b.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					VisualizerCanvas v = template.makeVisualizer(workspace, owner);
					v.update();
					Tab t = new Tab(template.getName());
					t.setPane(v);
					t.setCanClose(true);
					tabs.addTab(t);
					tabs.selectTab(t);
				}
			});
			System.out.println(template.getName());
			content.addMember(b);
		}
		
		buttonTab.setPane(content);
		tabs.addTab(buttonTab);
		tabs.setHeight100();
		tabs.setWidth100();
		
		this.addChild(tabs);
	}
	
	public void update(){
		for(VisualizerCanvas v : visualizers){
			v.update();
		}
	}
	
	private static final VisualizerAdder visualizerAdder = new VisualizerAdder(){
		private Collection<VisualizerTemplate> templates = new ArrayList<VisualizerTemplate>();
		public void addVisualizer(VisualizerTemplate visualizerTemplate) {
			this.templates.add(visualizerTemplate);
		}
		public Collection<VisualizerTemplate> getVisualizers() {
			return this.templates;
		}
	};
}
