package edu.brown.cs32.siliclone.implementations;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.interfaces.Operator;
import edu.brown.cs32.siliclone.interfaces.OperatorFactory;

public class TestOpFactory implements OperatorFactory {
	private String name;
	
	public TestOpFactory(String name){
		this.name = name;
	}
	
	@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return new Label(name);
	}

	@Override
	public Operator makeOperator() {
		// TODO Auto-generated method stub
		return new TestOp(name);
	}
	
	@Override
	public String getName(){
		return name;
	}

}
