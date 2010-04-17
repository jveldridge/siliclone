package edu.brown.cs32.siliclone.operators.testop;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.operators.Operator;

public class AnotherTestOp implements Operator {
	private String name;
	
	public AnotherTestOp(){
		this.name = "TestOp2";
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
		Label l =  new Label("Selector for " + name);
		l.setBackgroundColor("green");
		return l;
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		Label l = new Label(name);
		l.setBackgroundColor("yellow");
		return l;
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
