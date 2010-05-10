package edu.brown.cs32.siliclone.client.operators.restrictiondigest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperator;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorServiceAsync;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

@SuppressWarnings("serial")
public class DigestOperator extends AbstractRemoteOperator {
	
	transient private Widget image;
	transient private DigestPropertiesSelector propertiesSelector;
	
	public DigestOperator() {

	}
	
	public void init() {
		image = new Img("digest.gif");
		propertiesSelector = new DigestPropertiesSelector(this);
	}

	public PropertiesSelector getPropertiesSelector() {
		return propertiesSelector;
	}

	public Widget getWidget() {
		return image;
	}

	public int getNumInputs() {
		return 1;
	}

	@Override
	protected AbstractRemoteOperatorServiceAsync getServiceAsyncObject() {
		return GWT.create(DigestOperatorService.class);
	}

}
