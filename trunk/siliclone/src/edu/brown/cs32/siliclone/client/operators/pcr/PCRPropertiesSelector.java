package edu.brown.cs32.siliclone.client.operators.pcr;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import edu.brown.cs32.siliclone.client.utilities.enzymes.EnzymeListGenerator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class PCRPropertiesSelector extends PropertiesSelector {

	private PCROperator op;
	private SelectItem enzyme;
	private TextItem matchLength;
	
	public PCRPropertiesSelector(PCROperator operator)
	{
		super();
		op = operator;
		enzyme = new SelectItem();
		enzyme.setTitle("Enzyme");
		enzyme.setValueMap(EnzymeListGenerator.pcrEnzymeList());
		enzyme.setValue((String)operator.getProperties().get("enzyme"));
		matchLength = new TextItem();
		matchLength.setTitle("Minimum matched region");
		matchLength.setValue((Integer)operator.getProperties().get("match"));
		
		form.setFields(enzyme, matchLength, okButton);
		
		this.setAlign(Alignment.CENTER);
	}
	
	@Override
	protected void processInput() {
		// TODO Auto-generated method stub
		Map properties = new HashMap();
		properties.put("enzyme", enzyme.getDisplayValue());
		properties.put("match", Integer.parseInt(matchLength.getDisplayValue()));
		op.setProperties(properties);
		if(op.getInputs()[0] != null && op.getInputs()[1] != null && op.getInputs()[2] != null)
			op.calculate();
	}

	@Override
	protected boolean verifyFields() {
		//Just make sure some enzyme has been selected
		if(enzyme.getDisplayValue() == "") {
			SC.say("Please choose an enzyme for PCR");
			return false;
		}
		try {
			Integer.parseInt(matchLength.getDisplayValue());
		}
		catch(NumberFormatException e) {
			SC.say("Please input an integer for minimum matched region");
			return false;
		}
		return true;
	}

}
