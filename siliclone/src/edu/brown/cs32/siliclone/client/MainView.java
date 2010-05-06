package edu.brown.cs32.siliclone.client;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.types.Overflow;
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
	private Map<Workspace, Tab> _tabMap;
	
	public MainView(Siliclone main){
		_main = main;
		_workspaceTabs = new TabSet();
		_tabMap = new HashMap<Workspace,Tab>();
		

		
		VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
        mainLayout.setOverflow(Overflow.HIDDEN);
  
        //top menu
        mainLayout.addMember(new TopMenu(_main));  
        
        this.addWorkspace(new BasicWorkspace("New Workspace"));
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        OpTemplateListing opList = new OpTemplateListing();
        
        notMenuLayout.addMember(opList);
        
        notMenuLayout.addMember(_workspaceTabs); 
        _workspaceTabs.setOverflow(Overflow.HIDDEN);
        _workspaceTabs.setWidth100();
        _workspaceTabs.setHeight100();
        
        mainLayout.addMember(notMenuLayout);  
		this.setWidth100();
		this.setHeight100();
        this.addChild(mainLayout);
        this.setOverflow(Overflow.HIDDEN);
	}
	
	public void addWorkspace(Workspace w) {
		Tab newTab = new Tab();
		_tabMap.put(w, newTab);
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
	
	public void changeWorkspaceName(Workspace w, String newName) {
		_workspaceTabs.setTabTitle(_tabMap.get(w), (newName));
	}
	
}
