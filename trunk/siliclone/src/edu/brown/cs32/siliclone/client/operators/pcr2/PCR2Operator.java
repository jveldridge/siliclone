package edu.brown.cs32.siliclone.client.operators.pcr2;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;
import edu.brown.cs32.siliclone.operators.pcr.PCRPropertiesSelector;

@SuppressWarnings("serial")
public class PCR2Operator extends AbstractRemoteOperator {

	transient private PCR2PropertiesSelector propertiesSelector;
	transient private Img image; 
	
	public void init() {
		super.init();
		image = new Img("pcr.gif");
		propertiesSelector = new PCR2PropertiesSelector(this);
	}
	
	@Override
	protected Class getServiceClass() {
		return PCROperatorService.class;
	}

	public int getNumInputs() {
		// TODO Auto-generated method stub
		return 3;
	}

	public PropertiesSelector getPropertiesSelector() {
		// TODO Auto-generated method stub
		return propertiesSelector;	
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return image;
	}

}
