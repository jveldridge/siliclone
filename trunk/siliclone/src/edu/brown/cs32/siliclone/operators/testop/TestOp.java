package edu.brown.cs32.siliclone.operators.testop;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.interfaces.CompletedListener;
import edu.brown.cs32.siliclone.interfaces.DNASequence;
import edu.brown.cs32.siliclone.interfaces.Operator;

public class TestOp implements Operator {
	private String name;
	
	public TestOp(String name){
		this.name = name;
	}
	
	public void calculate() {
		// TODO Auto-generated method stub

	}

	public void cancel() {
		// TODO Auto-generated method stub

	}

	public Collection<Operator> getAdjacentOperators() {
		// TODO Auto-generated method stub
		return null;
	}

	public Widget getPropertiesSelector() {
		// TODO Auto-generated method stub
		return new Label("Selector for " + name);
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return new Label(name);
	}

	public void setInput(int slotNum, Collection<DNASequence> input) {
		// TODO Auto-generated method stub

	}

	public void connect(Operator adjacent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCompletedListener(CompletedListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInput(int slotNum, Operator input) {
		// TODO Auto-generated method stub
		
	}

}