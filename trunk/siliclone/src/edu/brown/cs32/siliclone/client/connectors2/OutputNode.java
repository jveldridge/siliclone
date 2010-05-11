package edu.brown.cs32.siliclone.client.connectors2;

import com.smartgwt.client.widgets.Canvas;

import edu.brown.cs32.siliclone.operators.Operator;

/**
 * An outputnode is simply an anchor to the outputting operator,
 * So that the connecting line can be drawn, and has access to the operator.
 * 
 */
public class OutputNode extends Canvas {
	private Operator owner;
	/**
	 * @param owner The operator whose output this represents.
	 */
	public OutputNode(Operator owner){
		this.owner = owner;
		this.setHeight(2);
		this.setWidth(2);
		this.setBackgroundColor("#00ff00");	}
	/**
	 * @return The operator whose output this represents.
	 */
	public Operator getOwner(){
		return owner;
	}
}
