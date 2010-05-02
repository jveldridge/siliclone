package edu.brown.cs32.siliclone.operators.rd;

import java.util.LinkedHashMap;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DigestPropertiesSelector extends PropertiesSelector {

	public DigestPropertiesSelector(DigestOperator operator) {
		super();
		
		SelectItem enzyme1 = new SelectItem();
		enzyme1.setTitle("Enzyme1");
		SelectItem enzyme2 = new SelectItem();
		enzyme2.setTitle("Enzyme2");
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String,String>();
		valueMap.put("HindIII", "HindIII");
		valueMap.put("XhoI", "XhoI");
		enzyme1.setValueMap(valueMap);
		enzyme2.setValueMap(valueMap);
		
		form.setFields(enzyme1, enzyme2, okButton);
		
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
