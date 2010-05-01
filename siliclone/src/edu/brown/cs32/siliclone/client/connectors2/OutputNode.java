package edu.brown.cs32.siliclone.client.connectors2;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragMoveEvent;
import com.smartgwt.client.widgets.events.DragMoveHandler;

import edu.brown.cs32.siliclone.operators.Operator;

public class OutputNode extends Canvas {
	private Operator owner;
	
	public OutputNode(Operator owner){
		this.owner = owner;
		this.setHeight(2);
		this.setWidth(2);
		this.setBackgroundColor("#00ff00");
		
	}
	public Operator getOwner(){
		return owner;
	}
}
