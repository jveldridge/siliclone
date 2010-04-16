package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;


public class TopMenu extends ToolStrip {
	public TopMenu(){
        ToolStripButton newButton = new ToolStripButton();
        newButton.setTitle("New");
        newButton.setWidth("100px");
        ToolStripButton loadButton = new ToolStripButton();
        loadButton.setTitle("Load");
        loadButton.setWidth("100px");
        ToolStripButton saveButton = new ToolStripButton();
        saveButton.setTitle("Save");
        saveButton.setWidth("100px");
        addButton(newButton); 
        addButton(loadButton); 
        addButton(saveButton); 
        setAlign(Alignment.CENTER);  
        setOverflow(Overflow.HIDDEN);  
        setHeight("30px");  
        setWidth100();
        setShowResizeBar(false);
        
        saveButton.setDisabled(true);
	}
}
