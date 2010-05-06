package edu.brown.cs32.siliclone.client.operators.ligation;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class LigationPropertiesSelector extends PropertiesSelector {

	private LigationOperator op;
	
	public LigationPropertiesSelector(LigationOperator operator)
	{
		super();
		op = operator;
		this.addMember(new Label("No properties for ligation"), 0);
		form.setFields(okButton);		
		this.setAlign(Alignment.CENTER);
	}
	
	@Override
	protected void processInput() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean verifyFields() {
		//Just make sure some enzyme has been selected
		return true;
	}

}
