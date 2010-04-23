package edu.brown.cs32.siliclone.client.connectors;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;

public class HorizontalConnector extends BaseConnector implements Connectable {
	
	public HorizontalConnector()
	{
		this(null, null);
		
	}
	
	public HorizontalConnector(Connectable left, Connectable right)
	{
		super(left, right, null, null);
		this.setBackgroundColor(LINE_COLOR);
		this.setOverflow(Overflow.HIDDEN);
		this.setResizeFrom("R", "L");
		this.setHeight(LINE_WIDTH);
		this.setWidth(DEFAULT_LENGTH);
		this.addDragRepositionMoveHandler(new HorizontalMoveHandler());
		this.addDoubleClickHandler(new AddNodeHandler());
		this.addRightMouseDownHandler(new DeleteLineHandler());
	}
	
	@Override
	public void adjustHorizontal(int change, Direction cameFrom)
	{
		if(cameFrom == Direction.LEFT)
		{
			if(change < _beforeDrag.getWidth() - MIN_LENGTH)
			{
				this.setRect(_beforeDrag.getLeft() + change, this.getTop(), 
						     _beforeDrag.getWidth() - change, this.getHeight());
			}
			else
			{
				//Start pushing over instead of resizing horizontal segment
				this.setRect(_beforeDrag.getLeft() + change, this.getTop(),
				             MIN_LENGTH, this.getHeight());
				//Right end is pushed over by (change + MIN_LENGTH - oldWidth)
				if(_right != null)
					_right.adjustHorizontal(change + MIN_LENGTH - _beforeDrag.getWidth(), Direction.LEFT);
			}
			
		}
		else if(cameFrom == Direction.RIGHT)
		{
			if(change > -_beforeDrag.getWidth() + MIN_LENGTH)
			{
				this.setRect(_beforeDrag.getLeft(), this.getTop(),
						     _beforeDrag.getWidth() + change, this.getHeight());
			}
			else
			{
				this.setRect(_beforeDrag.getLeft() + _beforeDrag.getWidth() + change - MIN_LENGTH, this.getTop(),
						     MIN_LENGTH, this.getHeight());
				if(_left != null)
					_left.adjustHorizontal(change - MIN_LENGTH + _beforeDrag.getWidth(), Direction.RIGHT);
			}
		}
	}
	
	@Override
	public void adjustVertical(int change, Direction cameFrom)
	{
		this.setTop(_beforeDrag.getTop() + change);
		if(_left != null && cameFrom != Direction.LEFT)
			_left.adjustVertical(change, Direction.RIGHT);
		if(_right != null && cameFrom != Direction.RIGHT)
			_right.adjustVertical(change, Direction.LEFT);
	}
	
	
	private class HorizontalMoveHandler implements DragRepositionMoveHandler {

		@Override
		public void onDragRepositionMove(DragRepositionMoveEvent event) {
			// TODO Auto-generated method stub
			//Do not change horizonal position on dragMove
			setLeft(_beforeDrag.getLeft());
			//Vertical position will automatically change, do notify connected elements
			if(_left != null)
				_left.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.RIGHT);
			if(_right != null)
				_right.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.LEFT);
		}
		
	}
	
	private class AddNodeHandler implements DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			if(_left == null)
			{
				NodeConnector left = new NodeConnector(null, HorizontalConnector.this, null, null);
				left.moveTo(getLeft() - left.getWidth(), getTop());
				getParentElement().addChild(left);
				_left = left;
			}
			if(_right == null)
			{
				NodeConnector right = new NodeConnector(HorizontalConnector.this, null, null, null);
				right.moveTo(getRight(), getTop());
				getParentElement().addChild(right);
				_right = right;
			}	
		}
	}
	
	private class DeleteLineHandler implements RightMouseDownHandler {

		@Override
		public void onRightMouseDown(RightMouseDownEvent event) {
			event.cancel();
			//If this connector is only connected to one other connection, delete it
			if(_left == null || _right == null)
			{
				getParentElement().removeChild(HorizontalConnector.this);
				if(_left != null)
					_left.removeConnection(HorizontalConnector.this);
				if(_right != null)
					_right.removeConnection(HorizontalConnector.this);
			}
		}
			
	}
}
