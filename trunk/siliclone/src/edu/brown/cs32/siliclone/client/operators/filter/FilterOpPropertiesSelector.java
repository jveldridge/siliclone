package edu.brown.cs32.siliclone.client.operators.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class FilterOpPropertiesSelector extends PropertiesSelector {

	private FilterOperator operator;
	private ListGrid sequenceGrid;
	private Map<String,SequenceHook> hooks;
	
	public FilterOpPropertiesSelector(FilterOperator op) {
		this.operator = op;
		form = new DynamicForm();
		sequenceGrid = new ListGrid();
		sequenceGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField nameField = new ListGridField("seq", "Sequence");
		sequenceGrid.setWidth(200);
		sequenceGrid.setHeight(200);
		sequenceGrid.setShowAllRecords(true);
		sequenceGrid.setFields(nameField);
		
		okButton.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				for (ListGridRecord r : sequenceGrid.getSelection()) {
					operator.addOuputSeq(hooks.get(r.getAttribute("seq")));
				}
				processInput();
			}
		});
		
		form.setFields(okButton);
		this.addMember(form);
	}
	
	protected void processInput() {
		//this.removeMember(sequenceGrid);
		hooks = new HashMap<String,SequenceHook>();
		ArrayList<ListGridRecord> records = new ArrayList<ListGridRecord>();
		System.out.println(operator.getInputs());
		for(Operator o : operator.getInputs()) {
			if(o != null){
				for(SequenceHook s : o.getOutputSequence()){
					hooks.put(s.getSeqName(), s);
					ListGridRecord r = new ListGridRecord();
					r.setAttribute("seq", s.getSeqName());
					records.add(r);
				}
			}
		}
		
		sequenceGrid.setData(records.toArray(new ListGridRecord[0]));
		this.addMember(sequenceGrid);
		//this.redraw();
	}

	protected boolean verifyFields() {
		// TODO Auto-generated method stub
		return false;
	}

}
