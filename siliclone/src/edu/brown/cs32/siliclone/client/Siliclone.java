package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.workspace.DragCreate;
import edu.brown.cs32.siliclone.client.workspace.OpListing;
import edu.brown.cs32.siliclone.client.workspace.TopMenu;
import edu.brown.cs32.siliclone.client.workspace.WorkspaceView;
import edu.brown.cs32.siliclone.operators.testop.TestOpFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Siliclone implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

        MainView theOnlyView = new MainView();
        
        //RootPanel.get().add(mainLayout);
        //I had to use this to make the selection panel work right --Noah
        theOnlyView.draw();
  
       
	}
}
