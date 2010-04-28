package edu.brown.cs32.siliclone.operators.dnaInput;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DNAInputOp extends AbstractOperator {
	
	private Widget _image;
	private DNAInputPropertiesSelector _properties;
	
	public DNAInputOp() {
		_image = new Img("dnaInput.gif");
		_properties = new DNAInputPropertiesSelector(this);
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
		return _properties;
	}

	public Widget getWidget() {
		return _image;
	}

}
