package edu.brown.cs32.siliclone.operators.pcr;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;

public class PCROperator extends AbstractOperator {

	private static final long serialVersionUID = 4455559319147828401L;
	
	private Widget _image;
	
	public PCROperator() {
		_image = new Img("pcr.gif");
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

	public Widget getPropertiesSelector() {
		// TODO Auto-generated method stub
		return new Label("PCR");
	}

	public Widget getWidget() {
		return _image;
	}

}
