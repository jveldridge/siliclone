package edu.brown.cs32.siliclone.operators.client.dnaInput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.client.SequenceService;
import edu.brown.cs32.siliclone.database.client.SequenceServiceAsync;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;
import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;

public class DNAInputPropertiesSelector extends PropertiesSelector {
	
	private TabSet _sourceTabs;
	private DNAInputOp _operator;
	private String _nameToLoad; //used in saved sequence tab
	
	public DNAInputPropertiesSelector(DNAInputOp op) {
		_operator = op;
		
		this.setSize("400px", "300px");
		this.clear();
		_sourceTabs = new TabSet();
		
		//initialize tabs
		this.initManualEntryTab();
		this.initUploadTab();
		this.initSavedSeqTab();
		//this.initNCBITab();

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
		
		final CheckboxItem circular = new CheckboxItem("Circular?");
		circular.setEndRow(true);
		circular.setValue(false);
		
		final TextAreaItem manualSequence = new TextAreaItem("Sequence");
		manualSequence.setEndRow(true);
		/*RegExpValidator manualEntryVal = new RegExpValidator();
		manualEntryVal.setErrorMessage("Invalid sequence: only A,C,G,T are permitted.");
		manualEntryVal.setExpression("^(A|C|G|T|a|t|g|c|)+$");
		manualSequence.setValidators(manualEntryVal);*/
		
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
							System.out.println("erroring on save sequence");
						}

						public void onSuccess(SequenceHook result) {
							//add circularity property to the newly added sequence
 							_service.addProperty(result, "isCircular", (Boolean) circular.getValue(), new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
									caught.printStackTrace();
								}

								public void onSuccess(Void result) {
									
								}
							});
							
 							//Add rightOverhang property to the newly added sequence
 							_service.addProperty(result, "rightOverhang", new Integer(0), new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
									caught.printStackTrace();
								}

								public void onSuccess(Void result) {
									
								}
							});
 							
 							
 							_service.addProperty(result, "leftOverhang", new Integer(0), new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
									caught.printStackTrace();
								}

								public void onSuccess(Void result) {
									
								}
							});


							//set the newly added sequence as the output of the operator
							Collection<SequenceHook> r =  new LinkedList<SequenceHook>();
							r.add(result);
							_operator.setSequence(r);
							_operator.onCompletion();
						}
					};
					
					_service.saveSequence(seq, name, callback);

				
					DNAInputPropertiesSelector.this.hide();
				}
				
			}
		});
		
		manualForm.setFields(seqName, manualSequence, circular, manualEntrySubmit);
		manualEntryTab.setPane(manualEntryPane);
		_sourceTabs.addTab(manualEntryTab);
		_sourceTabs.selectTab(manualEntryTab);
	}
	
	/**
	 * Helper method to make the Tab and associated pane
	 * to allow users to upload a supported file format
	 */
	private void initUploadTab() {
		final SequenceUploadServiceAsync service = GWT.create(SequenceUploadService.class);
		Tab uploadTab = new Tab();
		uploadTab.setTitle("Upload");
		final Canvas uploadPane = new Canvas();
		
		//TODO need to store file format in a session value to be accessed by the server
		final RadioGroupItem formats = new RadioGroupItem("Format");
		LinkedHashMap<String, String> formatsMap = new LinkedHashMap<String, String>();
		formatsMap.put("FASTA", "FASTA");
		formats.setValueMap(formatsMap);
		DynamicForm uploadForm = new DynamicForm();
		final TextItem seqName = new TextItem("Name");
		uploadForm.setFields(seqName, formats);
		uploadPane.addChild(uploadForm);
		
		Canvas uploaderPane = new Canvas();
		
		//button to be used by the SingleUploader class
		final com.google.gwt.user.client.ui.Button hidden = new com.google.gwt.user.client.ui.Button();
		
		//this button is invisible so that the uploadButton is clicked instead,
		//which stores information about the file format and sequence name in a
		//session variable to be accessed by the server when parsing
		hidden.setVisible(false);
	    final SingleUploader single1 = new SingleUploader(FileInputType.BROWSER_INPUT, new BaseUploadStatus(), hidden);
	    uploaderPane.addChild(single1);
	    single1.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
	     
	    	public void onFinish(IUploader uploader) {
	    		if (uploader.getStatus() == Status.SUCCESS) {
	    			
	        		AsyncCallback<Collection<SequenceHook>> callback = new AsyncCallback<Collection<SequenceHook>>() {
	        			public void onFailure(Throwable caught) {
							SC.say(caught.toString());	
						}

						public void onSuccess(Collection<SequenceHook> result) {
							_operator.setSequence(result);
							_operator.onCompletion();
							DNAInputPropertiesSelector.this.hide();
						}
					};

	        		service.getUploadedSequenceHook(uploader.getInputName(), callback);
	        	}
	        	else {
	        		SC.say("Something bad happened; status code = " + uploader.getStatus());
	        	}
	        }
	    });
	    
	    uploadPane.addChild(uploaderPane);
	    uploaderPane.setTop(seqName.getTop() + seqName.getHeight() + formats.getHeight() + 20);	    
	    
	    Button submit = new Button("Upload");
	    submit.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				String name = seqName.getDisplayValue();
				String format = formats.getDisplayValue();
				
				if (!name.equals("")) {
					System.out.println(single1.getInputName());
					AsyncCallback<Void> callback = new AsyncCallback<Void>() {

						public void onFailure(Throwable caught) {
							SC.say(caught.getMessage());
						}

						public void onSuccess(Void result) {
							//"click" the SingleUploader's upload button to actually send the file
							hidden.click();
						}
					};
					
					try {
						service.setUploadedFileAttributes(single1.getInputName(), name, format, callback);
					} catch (UploadedFileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
	    
	    uploadPane.addChild(submit);
	    submit.setTop(uploaderPane.getBottom() - 60);
	    
	    uploadPane.addChild(submit);
	    
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
		
		final SequenceServiceAsync service = GWT.create(SequenceService.class);
		final ListGrid sequenceGrid = new ListGrid();
		sequenceGrid.setWidth100();
		sequenceGrid.setSelectionType(SelectionStyle.SINGLE);
		sequenceGrid.setShowAllRecords(true);
		sequenceGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			public void onSelectionChanged(SelectionEvent event) {
				if (sequenceGrid.getSelection().length > 0) {
					_nameToLoad = sequenceGrid.getSelection()[0].getAttribute("name");
				}
			}
		});
		
		ListGridField name = new ListGridField("name", "Select sequence to load...");
		sequenceGrid.setFields(name);
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
				caught.printStackTrace();
			}

			public void onSuccess(List<String> results) {
				ListGridRecord[] data = new ListGridRecord[results.size()];
				for (int i = 0; i < results.size(); i++) {
					data[i] = new ListGridRecord();
					data[i].setAttribute("name", results.get(i));
				}
				
				sequenceGrid.setData(data);
			}
		};
		
		service.listAvailableSequences(callback);
		
		Button okButton = new Button("Load");
		okButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				AsyncCallback<SequenceHook> callback = new AsyncCallback<SequenceHook>() {
					public void onFailure(Throwable caught) {
						SC.say(caught.getMessage());
					}

					public void onSuccess(SequenceHook result) {
						Collection<SequenceHook> outputCollection = new ArrayList<SequenceHook>();
						outputCollection.add(result);
						_operator.setSequence(outputCollection);
						_operator.onCompletion();
						DNAInputPropertiesSelector.this.hide();
					}
				};
				service.findSequence(_nameToLoad, callback);
			}
		});
		
		ssPane.addChild(sequenceGrid);
		ssPane.addChild(okButton);
		
		okButton.setTop(sequenceGrid.getBottom());
		
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
