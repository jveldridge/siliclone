package edu.brown.cs32.siliclone.operators.dnaInput;

import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBinaryField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploadStatus.Status;

public class DNAInputPropertiesSelector extends PropertiesSelector {
	
	private TabSet _sourceTabs;
	private DNAInputOp _operator;
	
	public DNAInputPropertiesSelector(DNAInputOp op) {
		_operator = op;
		
		this.setSize("400px", "300px");
		this.clear();
		_sourceTabs = new TabSet();
		
		//initialize tabs
		this.initManualEntryTab();
		this.initUploadTab();
		this.initSavedSeqTab();
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
		
		final TextItem seqName = new TextItem("Name");
		seqName.setEndRow(true);
		
		final TextAreaItem manualSequence = new TextAreaItem("Sequence");
		RegExpValidator manualEntryVal = new RegExpValidator();
		manualEntryVal.setErrorMessage("Invalid sequence: only A,C,G,T are permitted.");
		manualEntryVal.setExpression("^(A|C|G|T|a|t|g|c)+$");
		manualSequence.setValidators(manualEntryVal);
		
		//TODO add ability for user to enter annotations

		manualEntryPane.addChild(manualForm);
		
		ButtonItem manualEntrySubmit = new ButtonItem("OK");
		manualEntrySubmit.addClickHandler(new ClickHandler() {
			private SequenceServiceAsync _service = GWT.create(SequenceService.class);
			
			public void onClick(ClickEvent event) {
				String name = seqName.getDisplayValue();
				String seq = manualSequence.getDisplayValue();
				if (!name.equals("") && !seq.equals("") && manualForm.validate()) {
					AsyncCallback<SequenceHook> callback = new AsyncCallback<SequenceHook>() {
						public void onFailure(Throwable caught) {
							SC.say(caught.getMessage());
						}

						public void onSuccess(SequenceHook result) {
							SC.say("Sequence saved successfully.");
							_operator.setSequence(result);
						}
					};
					try {
						_service.saveSequence(new NucleotideString(seq), new HashMap<String,Collection<Feature>>(), name, new HashMap<String,IsSerializable>(), callback);
					} catch (DataServiceException e) {}
				}
				
				DNAInputPropertiesSelector.this.hide();
			}
		});
		
		manualForm.setFields(seqName, manualSequence, manualEntrySubmit);
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
		
	    SingleUploader single1 = new SingleUploader();
	    uploadPane.addChild(single1);
	    single1.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
	        public void onFinish(IUploader uploader) {
	        	if (uploader.getStatus() == Status.SUCCESS) {
	        		SC.say(uploader.fileUrl());
	        	}
	        	else {
	        		SC.say("Something bad happened");
	        	}
	        }});
/*
		final DataSource data = new DataSource();
		DataSourceField key = new DataSourceField("key", FieldType.INTEGER);
		key.setPrimaryKey(true);
		DataSourceBinaryField dsbf = new DataSourceBinaryField("file");
		data.setFields(key, dsbf);
		data.setClientOnly(true);
		
		final DynamicForm uploadForm = new DynamicForm();
		ButtonItem upload = new ButtonItem("Upload");
		upload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DSCallback fetchCallback = new DSCallback() {
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						System.out.println("fetch response: " + response);
						System.out.println("fetch object: " + rawData);
						System.out.println("fetch request: " + request.getDataAsString());
					}
				};
				DSCallback saveCallback = new DSCallback() {	
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						System.out.println("save response: " + response);
						System.out.println("save object: " + rawData);
						System.out.println("save request: " + request.getDataAsString());
					}
				};
				
				uploadForm.saveData(saveCallback);
				data.fetchData(new Criteria("data", "*"), fetchCallback);
			}
		});

		FileItem toUpload = new FileItem("File");
		uploadForm.setFields(toUpload, upload);
		uploadForm.setDataSource(data);
		
		uploadPane.addChild(uploadForm);
		*/
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
	
	private void initSavedSeqTab() {
		Canvas ssPane = new Canvas();
		Tab ssTab = new Tab();
		ssTab.setTitle("Saved Sequence");
		ssTab.setPane(ssPane);
		_sourceTabs.addTab(ssTab);
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
