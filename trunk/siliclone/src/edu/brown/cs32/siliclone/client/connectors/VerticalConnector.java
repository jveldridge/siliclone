package edu.brown.cs32.siliclone.client.connectors;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;

public class VerticalConnector extends BaseConnector implements Connectable {
	
	public VerticalConnector()
	{
		this(null, null);
		
	}
	
	public VerticalConnector(Connectable up, Connectable down)
	{
		super(null, null, up, down);
		this.setBackgroundColor(LINE_COLOR);
		this.setOverflow(Overflow.HIDDEN);
		this.setResizeFrom("T", "B");
		this.setWidth(LINE_WIDTH);
		this.setHeight(DEFAULT_LENGTH);
		this.addDragRepositionMoveHandler(new VerticalMoveHandler());
		this.addDoubleClickHandler(new AddNodeHandler());
		this.addRightMouseDownHandler(new DeleteLineHandler());
	}
	
	@Override
	public void adjustVertical(int change, Direction cameFrom)
	{
		if(cameFrom == Direction.UP)
		{
			if(change < _beforeDrag.getHeight() - MIN_LENGTH)
			{
				this.setRect(this.getLeft(), _beforeDrag.getTop() + change,
					     this.getWidth(), _beforeDrag.getHeight() - change);
			}
			else
			{
				//Start pushing over instead of resizing horizontal segment
				this.setRect(this.getLeft(), _beforeDrag.getTop() + change,
				             this.getWidth(), MIN_LENGTH);
				//Right end is pushed over by (change + MIN_LENGTH - oldWidth)
				if(_down != null)
					_down.adjustVertical(change + MIN_LENGTH - _beforeDrag.getHeight(), Direction.UP);
			}
			
		}
		else if(cameFrom == Direction.DOWN)
		{
			if(change > -_beforeDrag.getHeight() + MIN_LENGTH)
			{
				this.setRect(this.getLeft(), _beforeDrag.getTop(),
						     this.getWidth(), _beforeDrag.getHeight() + change);
			}
			else
			{
				this.setRect(this.getLeft(), _beforeDrag.getTop() + _beforeDrag.getHeight() + change - MIN_LENGTH,
			                 this.getWidth(), MIN_LENGTH);
				if(_up != null)
					_up.adjustVertical(change - MIN_LENGTH + _beforeDrag.getHeight(), Direction.DOWN);
			}
		}
	}
	
	@Override
	public void adjustHorizontal(int change, Direction cameFrom)
	{
		this.setLeft(_beforeDrag.getLeft() + change);
		if(_up != null && cameFrom != Direction.UP)
			_up.adjustHorizontal(change, Direction.DOWN);
		if(_down != null && cameFrom != Direction.DOWN)
			_down.adjustHorizontal(change, Direction.UP);
	}
	
	
	private class VerticalMoveHandler implements DragRepositionMoveHandler {

		@Override
		public void onDragRepositionMove(DragRepositionMoveEvent event) {
			// TODO Auto-generated method stub
			//Do not change vertical position on dragMove
			setTop(_beforeDrag.getTop());
			//Horizontal position will automatically change, do notify connected elements
			if(_up != null)
				_up.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.DOWN);
			if(_down != null)
				_down.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.UP);
		}
		
	}
	
	private class AddNodeHandler implements DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			if(_up == null)
			{
				NodeConnector up = new NodeConnector(null, null, null, VerticalConnector.this);
				up.moveTo(getLeft(), getTop() - up.getHeight());
				getParentElement().addChild(up);
				_up = up;
			}
			if(_down == null)
			{
				NodeConnector down = new NodeConnector(null, null, VerticalConnector.this, null);
				down.moveTo(getLeft(), getBottom());
				getParentElement().addChild(down);
				_down = down;
			}	
		}
	}
	
	private class DeleteLineHandler implements RightMouseDownHandler {

		@Override
		public void onRightMouseDown(RightMouseDownEvent event) {
			event.cancel();
			//If this connector is only connected to one other connection, delete it
			if(_up == null || _down == null)
			{
				getParentElement().removeChild(VerticalConnector.this);
				if(_up != null)
					_up.removeConnection(VerticalConnector.this);
				if(_down != null)
					_down.removeConnection(VerticalConnector.this);
			}
		}
			
	}
}
