package edu.brown.cs32.siliclone.operators.client.dnaInput;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class DNAInputOp extends AbstractOperator {
	
	transient private Widget image;
	transient private DNAInputPropertiesSelector properties;
	
	public DNAInputOp() {
		
	}
	
	public void setSequence(Collection<SequenceHook> seq) {
		outputSequence = seq;
	}
	
	public void init() {
		this.image = new Img("dnaInput.gif");
		this.properties = new DNAInputPropertiesSelector(this);
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
		return properties;
	}

	public Widget getWidget() {
		return image;
	}

	public int getNumInputs() {
		return 0;
	}

}
