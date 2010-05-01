package edu.brown.cs32.siliclone.operators.pcr;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class PCROperator extends AbstractOperator {
	
	private Widget _image;
	private PCRPropertiesSelector _propertiesSelector;
	
	public PCROperator() {

	}
	
	public void init() {
		_image = new Img("pcr.gif");
		_propertiesSelector = new PCRPropertiesSelector(this);
	}

	public void addCompletedListener(CompletedListener l) {
		// TODO Auto-generated method stub
		
	}

	public void calculate() {
		// TODO Auto-generated method stub
		
	}

	public void connect(Operator adjacent) {
		// TODO Auto-generated method stub
		
	}

	public PropertiesSelector getPropertiesSelector() {
		return _propertiesSelector;
	}

	public Widget getWidget() {
		return _image;
	}

	public int getNumInputs() {
		return 3;
	}

}
