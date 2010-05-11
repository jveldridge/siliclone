package edu.brown.cs32.siliclone.client.operators.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class FilterOpPropertiesSelector extends PropertiesSelector {

	private FilterOperator operator;
	private ListGrid sequenceGrid;
	private Map<String,SequenceHook> hooks;
	private Collection<ListGridRecord> selected;
	private ButtonItem myOkButton;
	private DynamicForm myForm;
	
	public FilterOpPropertiesSelector(FilterOperator op) {
		this.operator = op;
		this.removeMember(form);
		myOkButton = new ButtonItem("OK");
		myForm = new DynamicForm();
		selected = new LinkedList<ListGridRecord>();
		sequenceGrid = new ListGrid();
		sequenceGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField nameField = new ListGridField("seq", "Sequence");
		sequenceGrid.setWidth(200);
		sequenceGrid.setHeight(200);
		sequenceGrid.setShowAllRecords(true);
		sequenceGrid.setFields(nameField);
		
		sequenceGrid.addCellClickHandler(new CellClickHandler() {
			
			public void onCellClick(CellClickEvent event) {
				System.out.println("onCellClickCalled");
				ListGridRecord r = event.getRecord();
				if (selected.contains(r)) {
					selected.remove(r);
				}
				else {
					selected.add(r);
				}
				System.out.println("after handling, selected is " + selected);
			}
		});
		
		myOkButton.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				System.out.println("in ok onclick, selected is " + selected);
				for (ListGridRecord r : selected) {
					System.out.println("seq for r: " + r.getAttribute("seq"));
					operator.addOuputSeq(hooks.get(r.getAttribute("seq")));
				}
				//processInput();
				
				FilterOpPropertiesSelector.this.hide();
				//propagate to children
				operator.onCompletion();
			}
		});
		
		myForm.setFields(myOkButton);
		this.addMember(myForm);
	}
	
	protected void processInput() {
		//this.removeMember(sequenceGrid);
		System.out.println("processInput calleed");
		selected = new LinkedList<ListGridRecord>();
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
		return true;
	}

}
