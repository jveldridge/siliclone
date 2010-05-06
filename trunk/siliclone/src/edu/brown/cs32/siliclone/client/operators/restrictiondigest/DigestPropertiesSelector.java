package edu.brown.cs32.siliclone.client.operators.restrictiondigest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DigestPropertiesSelector extends PropertiesSelector {

	public DigestPropertiesSelector(final DigestOperator operator) {
		super();
		/*
		final SelectItem enzyme1 = new SelectItem();
		enzyme1.setTitle("Enzyme1");
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String,String>();
		valueMap.put("HindIII", "HindIII");
		valueMap.put("XhoI", "XhoI");
		valueMap.put("HindIII", "HindIII");
		valueMap.put("XhoI", "XhoI");
		enzyme1.setValueMap(valueMap);
	
		
		
		
		form.setFields(enzyme1, okButton);
			*/
		this.setAlign(Alignment.CENTER);
	
		
        final ListGrid enzymeGrid = new ListGrid();
        enzymeGrid.setWidth(200);
        enzymeGrid.setHeight(200);
        enzymeGrid.setShowAllRecords(true);
        enzymeGrid.setSelectionType(SelectionStyle.SIMPLE);
        enzymeGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE);

        
        
        
        ListGridField nameField = new ListGridField("enzymeName", "Restriction Enzyme");
        enzymeGrid.setFields(nameField);
        enzymeGrid.setData(new ListGridRecord[]{new EnzymeRecord("NdeI"),new EnzymeRecord("PvuII"),new EnzymeRecord("XhoI"),new EnzymeRecord("SmaI"),new EnzymeRecord("HindIII")});
        addMember(enzymeGrid);
        
        
        final Button ok = new Button("OK");
        ok.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				ListGridRecord[] lgra = enzymeGrid.getSelection();

				System.out.println("hihi");
				operator.getProperties().put("enzyme",lgra[0].getAttributeAsObject("enzymeName"));
				System.out.println("hihi2");
				
				
				operator.calculate();
				System.out.println("hihi3");
			}
		});
        
        this.addMember(ok);
        

		
        }
	
	private class EnzymeRecord extends ListGridRecord{
        	public EnzymeRecord(String s) {
				setAttribute("enzymeName", s);
			}
        	
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
