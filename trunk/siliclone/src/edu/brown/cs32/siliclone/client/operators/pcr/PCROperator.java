package edu.brown.cs32.siliclone.client.operators.pcr;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperator;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorServiceAsync;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

@SuppressWarnings("serial")
public class PCROperator extends AbstractRemoteOperator {

	transient private PCRPropertiesSelector propertiesSelector;
	transient private Img image; 
	
	public void init() {
		super.init();
		image = new Img("pcr.gif");
		propertiesSelector = new PCRPropertiesSelector(this);
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


	@Override
	protected AbstractRemoteOperatorServiceAsync getServiceAsyncObject() {
		return GWT.create(PCROperatorService.class);
	}

}
