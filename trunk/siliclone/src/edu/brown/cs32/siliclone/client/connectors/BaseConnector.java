/**
 * 
 */
package edu.brown.cs32.siliclone.client.connectors;

import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DragResizeStartEvent;
import com.smartgwt.client.widgets.events.DragResizeStartHandler;

/**
 * @author Noah Langowitz (nlangowi)
 *
 */
public abstract class BaseConnector extends Canvas implements Connectable {

	
	
	protected Connectable _left, _right, _up, _down;
	protected Rectangle _beforeDrag;
	protected static final int LINE_WIDTH = 5, MIN_LENGTH = LINE_WIDTH, DEFAULT_LENGTH = 100;
	protected static final String LINE_COLOR = "#000000", NODE_COLOR = "#0088FF", STICKY_COLOR = "#00FF00";
	
	/**
	 * 
	 */
	public BaseConnector() {
		super();
		//this.setCanDragResize(true);
		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		
		DragStateHandler stateHandler = new DragStateHandler();
		this.addDragRepositionStartHandler(stateHandler);
		this.addDragResizeStartHandler(stateHandler);
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
	public void addConnection(Connectable toAdd, Direction dir)
	{
		//Only add new connection if you would not displace an existing connectable
		switch(dir)
		{
		case LEFT:
			if(_left == null)
				_left = toAdd;
			break;
		case RIGHT:
			if(_right == null)
				_right = toAdd;
			break;
		case UP:
			if(_up == null)
				_up = toAdd;
			break;
		case DOWN:
			if(_down == null)
				_down = toAdd;
			break;
		}
	}
	
	@Override
	public void changeConnection(Connectable toAdd, Direction dir)
	{
		switch(dir)
		{
		case LEFT:
			_left = toAdd;
			break;
		case RIGHT:
			_right = toAdd;
			break;
		case UP:
			_up = toAdd;
			break;
		case DOWN:
			_down = toAdd;
			break;
		}
	}	
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
	
	public void startDrag(Direction dir)
	{
		_beforeDrag = this.getRect();
		if(_left != null && dir != Direction.LEFT)
			_left.startDrag(Direction.RIGHT);
		if(_right != null && dir != Direction.RIGHT)
			_right.startDrag(Direction.LEFT);
		if(_up != null && dir != Direction.UP)
			_up.startDrag(Direction.DOWN);
		if(_down != null && dir != Direction.DOWN)
			_down.startDrag(Direction.UP);
	}
	
	
	private class DragStateHandler implements DragRepositionStartHandler, DragResizeStartHandler
 {

		@Override
		public void onDragRepositionStart(DragRepositionStartEvent event) {
			startDrag(null);
		}

		@Override
		public void onDragResizeStart(DragResizeStartEvent event) {
			startDrag(null);			
		}
	}	

}
