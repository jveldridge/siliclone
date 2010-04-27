package edu.brown.cs32.siliclone.operators.dnaInput;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DNAInputPropertiesSelector extends PropertiesSelector {

	private TextAreaItem _manualSequence;
	private TabSet _sourceTabs;
	
	public DNAInputPropertiesSelector() {
		this.setSize("300px", "250px");
		this.clear();
		_sourceTabs = new TabSet();
		
		_manualSequence = new TextAreaItem("Sequence");
		Canvas manualEntryPane = new Canvas();
		_form.setFields(_manualSequence); //will need other forms for other panes
		manualEntryPane.addChild(_form);
		Tab manualEntryTab = new Tab();
		manualEntryTab.setTitle("Manual Entry");
		manualEntryTab.setPane(manualEntryPane);
		_sourceTabs.addTab(manualEntryTab);
		_sourceTabs.selectTab(manualEntryTab);
		
		Canvas ncbiPane = new Canvas();
		Tab ncbiTab = new Tab();
		ncbiTab.setTitle("NCBI");
		ncbiTab.setPane(ncbiPane);
		_sourceTabs.addTab(ncbiTab);
		
		this.addMember(_sourceTabs);
	}

	@Override
	protected void processInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean verifyFields() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
