package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.workspace.BasicWorkspace;


public class MainView extends Canvas {

	private Siliclone _main;
	
	public MainView(Siliclone main){
		_main = main;
		
		this.setWidth100();
		this.setHeight100();
		
		VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
  
        //top menu
        mainLayout.addMember(new TopMenu());  
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        OpTemplateListing opList = new OpTemplateListing();
        
        notMenuLayout.addMember(opList);  
        notMenuLayout.addMember(new WorkspaceView(new BasicWorkspace())); 
  
        mainLayout.addMember(notMenuLayout);  
        
        this.addChild(mainLayout);
		
	}
	
}
