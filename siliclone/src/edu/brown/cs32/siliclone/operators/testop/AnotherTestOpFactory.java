package edu.brown.cs32.siliclone.operators.testop;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class AnotherTestOpFactory implements OperatorTemplate {
	private String name;
	
	public AnotherTestOpFactory(){
		this.name = "TestOp2";
	}
	
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return new Label(name);
	}

	public Operator makeOperator() {
		// TODO Auto-generated method stub
		return new AnotherTestOp();
	}
	
	public String getName(){
		return name;
	}

}
