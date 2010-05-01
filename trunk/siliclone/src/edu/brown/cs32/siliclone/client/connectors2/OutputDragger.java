package edu.brown.cs32.siliclone.client.connectors2;

import java.util.ArrayList;
import java.util.Collection;

import com.smartgwt.client.types.DragAppearance;
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
	private static final int WIDTH = 4, HEIGHT = 4;
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
		
		setCanDragReposition(true);
		setCanDrop(true);
		setDragAppearance(DragAppearance.TARGET);
		addDragRepositionStartHandler(new DragStart());
		addMouseOverHandler(new HoverStart());
		addMouseOutHandler(new HoverStop());
		addDragRepositionStopHandler(new DragStop());
		
	}

	private class DragStart implements DragRepositionStartHandler { 
		public void onDragRepositionStart(DragRepositionStartEvent event) {
			startX = getLeft();
			startY = getTop();
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
	
	private class DragStop implements DragRepositionStopHandler {
		public void onDragRepositionStop(DragRepositionStopEvent event) {
			setLeft(startX);
			setTop(startY);
			setBackgroundColor(STATIONARY_COLOR);
		}
	}
}
