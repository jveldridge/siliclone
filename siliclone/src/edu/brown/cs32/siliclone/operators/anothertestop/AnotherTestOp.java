package edu.brown.cs32.siliclone.operators.anothertestop;

import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class AnotherTestOp extends AbstractOperator {
	private String name;
	private int x,y;
	
	public AnotherTestOp(){
		this.name = "TestOpChanged";
		_widget.setWidth(300);
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

	public PropertiesSelector getPropertiesSelector() {
		// TODO Auto-generated method stub
		return new Properties(_widget) ;
	}
	
	private Label _widget = new Label(name);

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return _widget;
	}


	public void connect(Operator adjacent) {
		// TODO Auto-generated method stub
		
	}

	public void addCompletedListener(CompletedListener l) {
		// TODO Auto-generated method stub
		
	}

	public void setInput(int slotNum, Operator input) {
		// TODO Auto-generated method stub
		
	}
	

	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public int getNumInputs() {
		return 1;
	}

}
