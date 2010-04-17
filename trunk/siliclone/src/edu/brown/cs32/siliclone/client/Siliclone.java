package edu.brown.cs32.siliclone.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.operators.testop.TestOpFactory;




/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Siliclone implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

        VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
  
        //top menu
        mainLayout.addMember(new TopMenu());  
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        OpListing opList = new OpListing();
        opList.addOpCreate(new DragCreate(new TestOpFactory("test1")));
        opList.addOpCreate(new DragCreate(new TestOpFactory("test2")));
        
        notMenuLayout.addMember(opList);  
        notMenuLayout.addMember(new WorkspaceView()); 
  
        mainLayout.addMember(notMenuLayout);  
        
        //RootPanel.get().add(mainLayout);
        //I had to use this to make the selection panel work right --Noah
        mainLayout.draw();
  
       
	}
}
