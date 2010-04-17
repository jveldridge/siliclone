package edu.brown.cs32.siliclone.operators.testop;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class TestOpFactory implements OperatorTemplate {
	private String name;
	
	public TestOpFactory(String name){
		this.name = name;
	}
	
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return new Label(name);
	}

	public Operator makeOperator() {
		// TODO Auto-generated method stub
		return new TestOp(name);
	}
	
	public String getName(){
		return name;
	}

}
