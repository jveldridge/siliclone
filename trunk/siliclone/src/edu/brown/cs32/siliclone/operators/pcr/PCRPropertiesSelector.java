package edu.brown.cs32.siliclone.operators.pcr;

import java.util.LinkedHashMap;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class PCRPropertiesSelector extends PropertiesSelector {

	public PCRPropertiesSelector(PCROperator operator) {
		super();
		
		SelectItem enzyme = new SelectItem();
		enzyme.setTitle("Enzyme");
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String,String>();
		valueMap.put("Taq", "Taq");
		valueMap.put("Pfu", "Pfu");
		enzyme.setValueMap(valueMap);
		
		form.setFields(enzyme, okButton);
		
		this.setAlign(Alignment.CENTER);
		
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
