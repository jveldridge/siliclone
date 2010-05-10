package edu.brown.cs32.siliclone.client.operators.ligation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperator;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorServiceAsync;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

@SuppressWarnings("serial")
public class LigationOperator extends AbstractRemoteOperator {

	transient private LigationPropertiesSelector propertiesSelector;
	transient private Img image; 
	
	public void init() {
		image = new Img("ligation.gif");
		propertiesSelector = new LigationPropertiesSelector(this);
	}


	public int getNumInputs() {
		// TODO Auto-generated method stub
		return 2;
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
		return GWT.create(LigationOperatorService.class);
	}

}
