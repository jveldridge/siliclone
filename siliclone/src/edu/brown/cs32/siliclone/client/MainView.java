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

/**
 * This class is responsible for providing the GUI that
 * users will interact with when logged in to the web
 * application.  This consists of a TopMenu menu bar, a
 * listing of all the available Operators, and a large
 * tabbed area that will contain WorkspaceViews.
 * 
 * Also provides some methods to change/add workspaces to
 * the underlying tabs of WorkspaceViews.
 */
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
	
	/**
	 * Creates a new tab with a new WorkspaceView to display
	 * the workspace passed in as a parameter.
	 * 
	 * @param w
	 */
	public void addWorkspace(Workspace w) {
		Tab newTab = new Tab();
		_tabMap.put(w, newTab);
		newTab.setTitle(w.getName());
		newTab.setCanClose(true);
		newTab.setPane(new WorkspaceView(w));
		_workspaceTabs.addTab(newTab);
		_workspaceTabs.selectTab(newTab);
	}

	/**
	 * Returns the Workspace object that is being displayed
	 * in the currently active WorkspaceView.
	 * 
	 * @return the workspace object that is being displayed
	 * in the currently active WorkspaceView.
	 */
	public Workspace getCurrentWorkspace() {
		WorkspaceView w = (WorkspaceView) _workspaceTabs.getSelectedTab().getPane();
		return w.getWorkspace();
	}
	
	/**
	 * Changes the display name of the Workspace passed in as a
	 * parameter.  This consists of changing the title of the tab
	 * for containing the Workspaceview that is displaying that
	 * workspace.
	 * 
	 * @param w	the workspace whose name should be changed
	 * @param newName the new name for the workspace
	 */
	public void changeWorkspaceName(Workspace w, String newName) {
		_workspaceTabs.setTabTitle(_tabMap.get(w), (newName));
	}
	
}
