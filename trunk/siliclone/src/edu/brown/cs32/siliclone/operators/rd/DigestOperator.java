package edu.brown.cs32.siliclone.operators.rd;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DigestOperator extends AbstractOperator {
	
	transient private Widget image;
	transient private DigestPropertiesSelector propertiesSelector;
	
	public DigestOperator() {

	}
	
	public void init() {
		image = new Img("digest.gif");
		propertiesSelector = new DigestPropertiesSelector(this);
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
		return propertiesSelector;
	}

	public Widget getWidget() {
		return image;
	}

	public int getNumInputs() {
		return 3;
	}

}
