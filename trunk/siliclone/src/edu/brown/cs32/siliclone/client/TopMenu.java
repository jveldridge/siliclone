package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

//TODO serious work here
public class TopMenu extends ToolStrip {
	public TopMenu(){
        Canvas logoCanvas = new Canvas();
        Img logo = new Img("logo.gif", 100, 38);
        logoCanvas.addChild(logo);
        this.addMember(logoCanvas);		
		
		ToolStripButton newButton = new ToolStripButton();
        newButton.setTitle("New");
        newButton.setWidth("100px");
        ToolStripButton loadButton = new ToolStripButton();
        loadButton.setTitle("Load");
        loadButton.setWidth("100px");
        ToolStripButton saveButton = new ToolStripButton();
        saveButton.setTitle("Save");
        saveButton.setWidth("100px");
        this.addButton(newButton); 
        this.addButton(loadButton); 
        this.addButton(saveButton);
        
        this.addSeparator();
        
        ToolStripButton settingsButton = new ToolStripButton();
        settingsButton.setTitle("Settings");
        ToolStripButton logoutButton = new ToolStripButton();
        logoutButton.setTitle("Logout");
        this.addButton(settingsButton); 
        this.addButton(logoutButton);
              
        this.setAlign(Alignment.CENTER);  
        this.setOverflow(Overflow.HIDDEN);  
        this.setHeight("38px");  
        this.setWidth100();
        this.setShowResizeBar(false);
        
        saveButton.setDisabled(true);
	}
}
