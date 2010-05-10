package edu.brown.cs32.siliclone.client.connectors2;

import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

import edu.brown.cs32.siliclone.client.forms.RegisterForm;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerDisplay;
/**
 * A connecting line shows the three lines that connect two operators 
 * in the visual workspace.
 * It is constructed with both inputnode and output node set, and 
 * is responsible for updating its positioning when reposition is called.
 */
public class ConnectingLine extends Canvas {
	private final String LINE_COLOR = "#222222";
	private final int LINE_WIDTH = 3;
	
	private InputNode in;
	private OutputNode out;
	private Canvas topLine, middleLine, bottomLine;
	
	/**
	 * Constructs line connecting an input node to an output node,
	 * where both nodes are not null and already placed.
	 * @param in Already placed in workspace view, not null
	 * @param out Already placed in workspace view, not null
	 */
	public ConnectingLine(InputNode in, OutputNode out){
		this.in = in;
		this.out = out;
		init();
		reposition();
	}
	
	/**
	 * @return The InputNode set on initialization.
	 */
	public InputNode getIn(){
		return in;
	}
	
	/**
	 * @return the outputNode set on initialization.
	 */
	public OutputNode getOut(){
		return out;
	}
	
	/**
	 * Draws the line.
	 */
	private void init(){
		setPosition(Positioning.ABSOLUTE);
		
		topLine = new Canvas();
		topLine.setBackgroundColor(LINE_COLOR);
		topLine.setWidth(LINE_WIDTH);
		addChild(topLine);
		middleLine = new Canvas();
		middleLine.setBackgroundColor(LINE_COLOR);
		//middleLine.setMinHeight(LINE_WIDTH);
		middleLine.setBottom(middleLine.getTop() + LINE_WIDTH); //some property makes set height fail
		addChild(middleLine);
		bottomLine = new Canvas();
		bottomLine.setBackgroundColor(LINE_COLOR);
		bottomLine.setWidth(LINE_WIDTH);
		addChild(bottomLine);
		
		
		middleLine.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				out.getOwner().showVisualizerDisplay();
			}
		});
		
	}
	
	/**
	 * Updates the position of the lines on the workspace view
	 * to reflect the nodes' positions.
	 */
	public void reposition(){
		setLeft(Math.min(in.getParentElement().getLeft() + in.getLeft(),
						out.getParentElement().getLeft() + out.getLeft()));
		setTop(Math.min(in.getParentElement().getTop() + in.getTop(),
						out.getParentElement().getTop() + out.getTop()));
		setWidth(1 + Math.abs(in.getAbsoluteLeft() - out.getAbsoluteLeft()));
		setHeight(1 + Math.abs(in.getAbsoluteTop() - out.getAbsoluteTop()));
		
		if(in.getAbsoluteLeft() < out.getAbsoluteLeft() ^ in.getAbsoluteTop() > out.getAbsoluteTop()){ //positions at topleft, bottomright
			topLine.setLeft(0);
			topLine.setHeight(LINE_WIDTH + getHeight() / 2);
			middleLine.setTop(getHeight() / 2);
			middleLine.setWidth(1 + getWidth());
			bottomLine.setLeft(getWidth() - LINE_WIDTH);
			bottomLine.setTop(getHeight()/ 2);
			bottomLine.setHeight(1 + getHeight() / 2);
		}else { //positions at bottomleft, topright
			topLine.setLeft(getWidth() - LINE_WIDTH);
			topLine.setHeight(1 + getHeight() / 2);
			middleLine.setTop(getHeight() / 2);
			middleLine.setWidth(1 + getWidth());
			bottomLine.setLeft(0);
			bottomLine.setTop(getHeight()/ 2);
			bottomLine.setHeight(1 + getHeight() / 2);
		}
	}
}
