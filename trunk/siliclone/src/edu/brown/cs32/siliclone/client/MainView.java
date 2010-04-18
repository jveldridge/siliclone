package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.workspace.DragCreate;
import edu.brown.cs32.siliclone.client.workspace.WorkspaceView;
import edu.brown.cs32.siliclone.operators.testop.TestOpFactory;

public class MainView extends Canvas {

	public MainView(){
		
		this.setWidth100();
		this.setHeight100();
		
		VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
  
        //top menu
        mainLayout.addMember(new TopMenu());  
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        OpListing opList = new OpListing();
        
        notMenuLayout.addMember(opList);  
        notMenuLayout.addMember(new WorkspaceView()); 
  
        mainLayout.addMember(notMenuLayout);  
        
        this.addChild(mainLayout);
		
	}
	
}
