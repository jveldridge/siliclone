package edu.brown.cs32.siliclone.client.connectors2;

import java.util.ArrayList;
import java.util.Collection;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;

import edu.brown.cs32.siliclone.operators.Operator;

public class OutputDragger extends Canvas {
	private static final int WIDTH = 7, HEIGHT = 7;
	private static final String STATIONARY_COLOR = "#882288", 
								HOVER_COLOR = "#BBBBBB";
	
	private int startX, startY; 
	private Operator owner;
	private Collection<InputNode> recipients;
	
	public OutputDragger(Operator owner, int x, int y){
		this.owner = owner;
		init();
		recipients = new ArrayList<InputNode>();
		startX = x;
		startY = y;
	}
	
	public void addRecipient(InputNode i){
		recipients.add(i);
	}
	public void reposition(){
		for(InputNode i : recipients){
			i.reposition();
		}
	}
	public void disconnect(){
		for(InputNode i : recipients){
			i.disconnect();
		}
	}
	
	public Operator getOwner(){
		return owner;
	}
	
	public int getStartX(){
		return startX;
	}
	public int getStartY(){
		return startY;
	}
	
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
		
	}

	private class DragStart implements DragStartHandler { 
		public void onDragStart(DragStartEvent event) {
			startX = getLeft();
			startY = getTop();
			bringToFront();
		}
	}
	
	private class HoverStart implements MouseOverHandler {
		public void onMouseOver(MouseOverEvent event) {
			setBackgroundColor(HOVER_COLOR);
		}	
	}
	
	private class HoverStop implements MouseOutHandler{
		public void onMouseOut(MouseOutEvent event) {
			setBackgroundColor(STATIONARY_COLOR);
		}
		
	}
	
	private class DragStop implements DragStopHandler {
		public void onDragStop(DragStopEvent event) {
			setLeft(startX);
			setTop(startY);
			setBackgroundColor(STATIONARY_COLOR);
		}
	}
}
