package edu.brown.cs32.siliclone.client.connectors2;

import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.DropOverEvent;
import com.smartgwt.client.widgets.events.DropOverHandler;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorCycleException;


/**
 * An inputNode is held on an operator's view, 
 * and is responsible for accepting output draggers,
 * creating new connections and updating the underlying operator data.
 * 
 */
public class InputNode extends Canvas {
	private static final int WIDTH = 8, HEIGHT = 5;
	private static final String EMPTY_COLOR = "#222288", 
								HOVER_COLOR = "#BBBBBB", 
								CONNECTED_COLOR = "#0000BB";
	
	
	private int slotNum;
	private Operator owner;
	private boolean isConnected;
	private ConnectingLine connection;
	
	/**
	 * Creates the input node to represent the owner's input at the given slot.
	 * @param slotNum The number of the input for the operator
	 * @param owner The operator whose input this input node represents.
	 */
	public InputNode(int slotNum, Operator owner){
		this.slotNum = slotNum;
		this.owner = owner;
		isConnected = false;
		init();
	}
	
	/**
	 * draws this inputnode
	 */
	private void init(){
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setBackgroundColor(EMPTY_COLOR);
		
		setCanAcceptDrop(true);
		addDropOverHandler(new DropHoverStart());
		addDropOutHandler(new DropHoverStop());
		addDropHandler(new Acceptor());
		addClickHandler(new Closer());
	}

	
	
	/**
	 * creates a new connectingline given an outputdragger from
	 * another operator. If there is a cycle, displays an error popup using SC
	 * @param o The outputdragger from an operator that is not the owner.
	 */
	public void connect(OutputDragger o){
		if(!isConnected){
			Operator op = o.getOwner();
			try {
				owner.setInput(slotNum, op);
				isConnected = true;
				setBackgroundColor(CONNECTED_COLOR);
				OutputNode out = new OutputNode(op);
				o.addRecipient(this);
				o.getParentElement().addChild(out);
				out.setLeft(o.getStartX());
				out.setTop(o.getStartY());
				connection = new ConnectingLine(this, out);
				getParentElement().getParentElement().addChild(connection);
				connection.reposition();
				o.getParentElement().bringToFront();
				o.bringToFront();
				this.getParentElement().bringToFront(); //moving the canvas seems to allow more drops
			} catch (OperatorCycleException e) {
				SC.say(e.getMessage());
			}
		}
	}
	
	/**
	 * Removes the connectingline connected to this node, 
	 * updating the underlying data as well
	 */
	public void disconnect(){
		if(isConnected){
			isConnected = false;
			owner.removeInput(slotNum);
			connection.getOut().removeFromParent();
			connection.removeFromParent();
			connection.hide();
			connection = null;
			setBackgroundColor(EMPTY_COLOR);
			this.getParentElement().bringToFront();
		}
	}
	
	/**
	 * repositions the connecting line attached to this if there is one.
	 */
	public void reposition(){
		if(isConnected){
			connection.reposition();
		}
	}
	
	/**
	 * Can change color when something overs over it.
	 */
	private class DropHoverStart implements DropOverHandler {
		public void onDropOver(DropOverEvent event) {
			getParentElement().bringToFront();
			if(!isConnected){
				setBackgroundColor(HOVER_COLOR);
			}
		}
	}
	
	/**
	 * change color back
	 */
	private class DropHoverStop implements DropOutHandler {
		public void onDropOut(DropOutEvent event) {
			if(!isConnected){
				setBackgroundColor(EMPTY_COLOR);
			}
		}
	}
	
	/**
	 * Removes connection when this node is clicked.
	 */
	private class Closer implements ClickHandler {
		public void onClick(ClickEvent event) {
			if(isConnected){
				disconnect();
			}
		}
	}
	
	/**
	 * creates connection when output dragger is dropped on this node.
	 * (must node be already connected, must not form a cycle)
	 */
	private class Acceptor implements DropHandler {
		public void onDrop(DropEvent event) {
			Canvas dropped = EventHandler.getDragTarget();
			if(!isConnected && dropped instanceof OutputDragger){
				connect((OutputDragger) dropped);
			}
		}
	}
	
}
