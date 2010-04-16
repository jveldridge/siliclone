package edu.brown.cs32.siliclone.implementations;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.interfaces.DNASequence;
import edu.brown.cs32.siliclone.interfaces.Operator;

public class TestOp implements Operator {
	private String name;
	
	public TestOp(String name){
		this.name = name;
	}
	
	@Override
	public void calculate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Operator> getAdjacentOperators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget getPropertiesSelector() {
		// TODO Auto-generated method stub
		return new Label("Selector for " + name);
	}

	@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return new Label(name);
	}

	@Override
	public void setInput(int slotNum, Collection<DNASequence> input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect(Operator adjacent) {
		// TODO Auto-generated method stub
		
	}

}
