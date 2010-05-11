package edu.brown.cs32.siliclone.client.connectors2;

import java.util.ArrayList;
import java.util.Collection;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;

import edu.brown.cs32.siliclone.operators.Operator;

/**
 * An outputDragger is associated with an operator owner, 
 * and creates connections from that operator to other 
 * operators' inputs.
 */
public class OutputDragger extends Canvas {
	private static final int WIDTH = 7, HEIGHT = 7;
	private static final String STATIONARY_COLOR = "#882288", 
								HOVER_COLOR = "#BBBBBB";
	
	private int startX, startY; 
	private Operator owner;
	private Collection<InputNode> recipients;
	
	/**
	 * @param owner The operator whose output this visually represents
	 * @param x The x-coordinate of the left side of this dragger
	 * @param y The y-coordinate of the right side of this dragger
	 */
	public OutputDragger(Operator owner, int x, int y){
		this.owner = owner;
		init();
		recipients = new ArrayList<InputNode>();
		startX = x;
		startY = y;
	}
	
	/**
	 * @param i The inputNode to connect to this dragger's operator's output.
	 */
	public void addRecipient(InputNode i){
		recipients.add(i);
	}
	
	/**
	 * updates the lines connecting to this operator's output.
	 */
	public void reposition(){
		for(InputNode i : recipients){
			i.reposition();
		}
	}
	
	/**
	 * removes all connections using this operator's output.
	 */
	public void disconnect(){
		for(InputNode i : recipients){
			i.disconnect();
		}
	}
	
	/**
	 * gives the owner of this outputdragger.
	 * @return The owner set on construction
	 */
	public Operator getOwner(){
		return owner;
	}
	
	/**
	 * The location of this dragger (not where it currently is, 
	 * but where it should return on the owner's opview).
	 * @return The left coordinate
	 */
	public int getStartX(){
		return startX;
	}
	/**
	 * The location of this dragger (not where it currently is, 
	 * but where it should return on the owner's opview).
	 * @return The top coordinate
	 */
	public int getStartY(){
		return startY;
	}
	
	/**
	 * Positions this dragger.
	 */
	private void init(){
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setBackgroundColor(STATIONARY_COLOR);
		
		setCanDragReposition(false);
		setCanDrag(true);
		setCanDrop(true);
		setDragAppearance(DragAppearance.TARGET);
		addDragStartHandler(new DragStart());
		addMouseOverHandler(new HoverStart());
		addMouseOutHandler(new HoverStop());
		addDragStopHandler(new DragStop());
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				owner.showVisualizerDisplay();
				
			}
		});
	}

	/**
	 * saves the position to return, and drops below all potential drop targets.
	 */
	private class DragStart implements DragStartHandler { 
		public void onDragStart(DragStartEvent event) {
			startX = getLeft();
			startY = getTop();
			getParentElement().sendToBack();
		}
	}
	
	/**
	 * can change color when over a drop target
	 */
	private class HoverStart implements MouseOverHandler {
		public void onMouseOver(MouseOverEvent event) {
			setBackgroundColor(HOVER_COLOR);
		}	
	}
	
	/**
	 * can change back
	 */
	private class HoverStop implements MouseOutHandler{
		public void onMouseOut(MouseOutEvent event) {
			setBackgroundColor(STATIONARY_COLOR);
		}
		
	}
	
	/**
	 * and change back to stationary color
	 */
	private class DragStop implements DragStopHandler {
		public void onDragStop(DragStopEvent event) {
			setLeft(startX);
			setTop(startY);
			setBackgroundColor(STATIONARY_COLOR);
		}
	}
}
