package edu.brown.cs32.siliclone.operators.pcr;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class PCRTemplate implements OperatorTemplate {
	private String name;
	
	public PCRTemplate(){
		this.name = "PCR";
	}
	
	public Widget getWidget() {
		return new Label(name);
	}

	public Operator makeOperator() {
		return new PCROperator();
	}
	
	public String getName(){
		return name;
	}

}
