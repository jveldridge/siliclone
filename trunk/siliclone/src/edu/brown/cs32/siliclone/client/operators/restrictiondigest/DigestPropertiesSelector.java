package edu.brown.cs32.siliclone.client.operators.restrictiondigest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
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
	
        final SelectItem enzymeChoice= new SelectItem();
        enzymeChoice.setTitle("Restriction Enzyme");
        enzymeChoice.setWidth(200);
        enzymeChoice.setValueMap("NdeI","PvuII","XhoI","SmaI","HindIII");
        enzymeChoice.setValue((String)operator.getProperties().get("enzyme"));
        form.setFields(enzymeChoice);
        
        
        final Button ok = new Button("OK");
        ok.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

				
				if(!Arrays.asList(enzymeChoice.getValues()).contains(enzymeChoice.getValue())){
					SC.say("Please select an existing restriction enzyme");
					return;
				}
					
				operator.getProperties().put("enzyme",enzymeChoice.getValue());
				
				
				operator.update();
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
