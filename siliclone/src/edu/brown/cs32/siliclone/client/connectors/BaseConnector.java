/**
 * 
 */
package edu.brown.cs32.siliclone.client.connectors;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.rpc.server.Pair;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.DragResizeStartEvent;
import com.smartgwt.client.widgets.events.DragResizeStartHandler;
import com.smartgwt.client.widgets.events.DragResizeStopEvent;
import com.smartgwt.client.widgets.events.DragResizeStopHandler;

/**
 * @author Noah Langowitz (nlangowi)
 *
 */
public abstract class BaseConnector extends Canvas implements Connectable {

	
	
	protected Connectable _left, _right, _up, _down;
	protected Rectangle _beforeDrag;
	protected static final int LINE_WIDTH = 5, MIN_LENGTH = LINE_WIDTH, DEFAULT_LENGTH = 100;
	protected static final String LINE_COLOR = "#000000", NODE_COLOR = "#00FFFF";
	private boolean _dragging;
	
	/**
	 * 
	 */
	public BaseConnector() {
		super();
		_dragging = false;
		this.setCanDragResize(true);
		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		
		DragStateHandler stateHandler = new DragStateHandler();
		this.addDragRepositionStartHandler(stateHandler);
		this.addDragResizeStartHandler(stateHandler);
		this.addDragRepositionStopHandler(stateHandler);
		this.addDragResizeStopHandler(stateHandler);
	}
	
	public BaseConnector(Connectable left, Connectable right, Connectable up, Connectable down)
	{
		this();
		_left = left;
		_right = right;
		_up = up;
		_down = down;
	}


	/* (non-Javadoc)
	 * @see edu.brown.cs32.siliclone.client.connectors.Connectable#adjustHorizontal(int, edu.brown.cs32.siliclone.client.connectors.Connectable)
	 */
	@Override
	public abstract void adjustHorizontal(int change, Direction cameFrom);
	
	
	/* (non-Javadoc)
	 * @see edu.brown.cs32.siliclone.client.connectors.Connectable#adjustVertical(int, edu.brown.cs32.siliclone.client.connectors.Connectable)
	 */
	@Override
	public abstract void adjustVertical(int change, Direction cameFrom);
	
	@Override
	final public void translate(int horizontal, int vertical, Direction cameFrom) {
		this.setLeft(_beforeDrag.getLeft() + horizontal);
		this.setTop(_beforeDrag.getTop() + vertical);
		if(_left != null && cameFrom != Direction.LEFT)
			_left.translate(horizontal, vertical, Direction.RIGHT);
		if(_right != null && cameFrom != Direction.RIGHT)
			_right.translate(horizontal, vertical, Direction.LEFT);
		if(_up != null && cameFrom != Direction.UP)
			_up.translate(horizontal, vertical, Direction.DOWN);
		if(_down != null && cameFrom != Direction.DOWN)
			_down.translate(horizontal, vertical, Direction.UP);
	}
	
	@Override
	final public void removeConnection(Connectable toRemove)
	{
		//If toRemove matches any of this Connector's connections, remove it
		if(toRemove == _left)
			_left = null;
		if(toRemove == _right)
			_right = null;
		if(toRemove == _up)
			_up = null;
		if(toRemove == _down)
			_down = null;
	}
	
	/**
	 * Handles the start of a drag move or resize action and propagates calls
	 * of startDrag to any connected Connectables
	 * @see edu.brown.cs32.siliclone.client.connectors.Connectable#startDrag()
	 */
	@Override
	public void startDrag() {
		if(_dragging == false)
		{
			_dragging = true;
			_beforeDrag = this.getRect();
			
			if(_left != null)
				_left.startDrag();
			if(_right != null)
				_right.startDrag();
			if(_up != null)
				_up.startDrag();
			if(_down != null)
				_down.startDrag();
		}
	}
	
	/** 
	 * Handles the end of a drag move or resize action and propagates calls
	 * of endDrag to any connected Connectables
	 * @see edu.brown.cs32.siliclone.client.connectors.Connectable#endDrag()
	 */
	@Override
	public void endDrag() {
		if(_dragging == true)
		{
			_dragging = false;
			
			if(_left != null)
				_left.endDrag();
			if(_right != null)
				_right.endDrag();
			if(_up != null)
				_up.endDrag();
			if(_down != null)
				_down.endDrag();
		}
	}
	
	private class DragStateHandler implements DragRepositionStartHandler, DragResizeStartHandler,
	                                          DragRepositionStopHandler, DragResizeStopHandler {

		@Override
		public void onDragRepositionStart(DragRepositionStartEvent event) {
			startDrag();
		}

		@Override
		public void onDragResizeStart(DragResizeStartEvent event) {
			startDrag();			
		}

		@Override
		public void onDragRepositionStop(DragRepositionStopEvent event) {
			endDrag();
		}

		@Override
		public void onDragResizeStop(DragResizeStopEvent event) {
			endDrag();
		}
		
	}	

}
