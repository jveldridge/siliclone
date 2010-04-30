package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.client.workspace.BasicWorkspace;
import edu.brown.cs32.siliclone.client.workspace.Workspace;


public class MainView extends Canvas {

	private Siliclone _main;
	private final TabSet _workspaceTabs;
	
	public MainView(Siliclone main){
		_main = main;
		_workspaceTabs = new TabSet();
		
		this.setWidth100();
		this.setHeight100();
		
		VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
  
        //top menu
        mainLayout.addMember(new TopMenu(_main));  
        
        Tab initialTab = new Tab("New Workspace");
		initialTab.setCanClose(true);
		_workspaceTabs.addTab(initialTab);
		initialTab.setPane(new WorkspaceView(new BasicWorkspace("New Workspace")));
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        OpTemplateListing opList = new OpTemplateListing();
        
        notMenuLayout.addMember(opList);
        
        notMenuLayout.addMember(_workspaceTabs); 
  
        mainLayout.addMember(notMenuLayout);  
        
        this.addChild(mainLayout);
	}
	
	public void addWorkspace(Workspace w) {
		Tab newTab = new Tab();
		newTab.setTitle(w.getName());
		newTab.setCanClose(true);
		newTab.setPane(new WorkspaceView(w));
		_workspaceTabs.addTab(newTab);
		_workspaceTabs.selectTab(newTab);
	}

	public Workspace getCurrentWorkspace() {
		WorkspaceView w = (WorkspaceView) _workspaceTabs.getSelectedTab().getPane();
		return w.getWorkspace();
	}
	
}
