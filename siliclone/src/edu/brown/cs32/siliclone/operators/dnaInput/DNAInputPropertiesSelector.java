package edu.brown.cs32.siliclone.operators.dnaInput;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DNAInputPropertiesSelector extends PropertiesSelector {
	
	private TabSet _sourceTabs;
	
	public DNAInputPropertiesSelector() {
		this.setSize("300px", "250px");
		this.clear();
		_sourceTabs = new TabSet();
		
		//initialize tabs
		this.initManualEntryTab();
		this.initUploadTab();
		this.initNCBITab();

		this.addMember(_sourceTabs);
	}
	
	/**
	 * Helper method to make the Tab and associated pane
	 * to allow users to manually input a sequence of nucleotides
	 */
	private void initManualEntryTab() {
		Tab manualEntryTab = new Tab();
		manualEntryTab.setTitle("Manual Entry");
		
		Canvas manualEntryPane = new Canvas();
		manualEntryPane.setAlign(Alignment.CENTER);
		
		final DynamicForm manualForm = new DynamicForm();
		manualForm.setTitleOrientation(TitleOrientation.TOP);
		
		final TextAreaItem manualSequence = new TextAreaItem("Sequence");
		RegExpValidator manualEntryVal = new RegExpValidator();
		manualEntryVal.setErrorMessage("Invalid sequence: only A,C,G,T are permitted.");
		manualEntryVal.setExpression("^(A|C|G|T)+$");
		manualSequence.setValidators(manualEntryVal);
		
		//TODO add ability for user to enter annotations

		manualEntryPane.addChild(manualForm);
		
		ButtonItem manualEntrySubmit = new ButtonItem("OK");
		manualEntrySubmit.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				if (manualForm.validate()) {
					String seq = manualSequence.getDisplayValue();
					
					DNAInputPropertiesSelector.this.hide();
				}
			}
		});
		
		manualForm.setFields(manualSequence, manualEntrySubmit);
		manualEntryTab.setPane(manualEntryPane);
		_sourceTabs.addTab(manualEntryTab);
		_sourceTabs.selectTab(manualEntryTab);
	}
	
	/**
	 * Helper method to make the Tab and associated pane
	 * to allow users to upload a supported file format
	 */
	private void initUploadTab() {
		Tab uploadTab = new Tab();
		uploadTab.setTitle("Upload");
		
		Canvas uploadPane = new Canvas();
		
		DynamicForm uploadForm = new DynamicForm();
		uploadForm.setFields(new TextItem("hi"));
		uploadPane.addChild(uploadForm);
		
		uploadTab.setPane(uploadPane);
		_sourceTabs.addTab(uploadTab);
	}
	
	/**
	 * Helper method to make the Tab and associated pane
	 * to allow users to query the NCBI database
	 */
	private void initNCBITab() {
		Canvas ncbiPane = new Canvas();
		Tab ncbiTab = new Tab();
		ncbiTab.setTitle("NCBI");
		ncbiTab.setPane(ncbiPane);
		_sourceTabs.addTab(ncbiTab);
	}

	@Override
	protected void processInput() {
		
	}

	@Override
	protected boolean verifyFields() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
