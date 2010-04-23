package edu.brown.cs32.siliclone.client.connectors;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;

public class StickyNodeConnector extends NodeConnector implements Stickable {

	private static final int TOLERANCE = 2*LINE_WIDTH;
	
	public StickyNodeConnector(Connectable left, Connectable right, Connectable up, Connectable down)
	{
		super(left, right, up, down);
		this.setBackgroundColor(STICKY_COLOR);
		moveRegistration.removeHandler();
		this.addDragRepositionMoveHandler(new StickyNodeMoveHandler());
		this.addDragRepositionStopHandler(new MakeConnectionHandler());
		rightClickRegistration.removeHandler();
		this.addRightMouseDownHandler(new MakeUnstickyHandler());
	}
	
	public StickyNodeConnector() {
		this(null, null, null, null);
	}
	
	@Override
	public Connectable getConnection(Direction dir)
	{
		switch(dir)
		{
		case LEFT:
			return _left;
		case RIGHT:
			return _right;
		case UP:
			return _up;
		case DOWN:
			return _down;
		default:
			return null;
		}
	}
	
	private class StickyNodeMoveHandler implements DragRepositionMoveHandler {

		@Override
		public void onDragRepositionMove(DragRepositionMoveEvent event) {
			if(_left != null)
			{

				_left.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.RIGHT);
				_left.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.RIGHT);
			}
			if(_right != null)
			{
				_right.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.LEFT);
				_right.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.LEFT);
			}
			if(_up != null)
			{
				_up.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.DOWN);
				_up.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.DOWN);
			}
			if(_down != null)
			{
				_down.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.UP);
				_down.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.UP);
			}			
		}
		
	}
	
	private class MakeConnectionHandler implements DragRepositionStopHandler {

		@Override
		public void onDragRepositionStop(DragRepositionStopEvent event) {
			Canvas parent = getParentElement();
			Canvas[] children = parent.getChildren();
			for(Canvas c : children)
			{
				if(c instanceof Stickable && c != StickyNodeConnector.this)
				{
					int hDelta = getLeft() - c.getLeft();
					int vDelta = getTop() - c.getTop();
					if(Math.abs(hDelta) < TOLERANCE && Math.abs(vDelta) < TOLERANCE)
					{
						Stickable s = (Stickable) c;
						//Can only connect nodes if they do not both have connections
						//going in the same direction
						Connectable left, right, up, down;
						if(_left == null) //if this doesn't have a left
						{
							left = s.getConnection(Direction.LEFT); //use c's left (even if it's null!)
						}
						else if(s.getConnection(Direction.LEFT) == null)
						{
							left = _left;
						}
						else
							continue; //Can't stick to this because of overlap
						
						if(_right == null)
						{
							right = s.getConnection(Direction.RIGHT);
						}
						else if(s.getConnection(Direction.LEFT) == null){
							right = _right;
						}
						else
							continue;
						
						if(_up == null) {
							up = s.getConnection(Direction.UP);
						}
						else if(s.getConnection(Direction.UP) == null) {
							up = _up;
						}
						else
							continue;
						
						if(_down == null) {
							down = s.getConnection(Direction.DOWN);
						}
						else if(s.getConnection(Direction.DOWN) == null) {
							down = _down;
						}
						else
							continue;
						
						s.startDrag(null); //make sure s has the right saved location
						s.translate(hDelta, vDelta, null);
						
						NodeConnector replacement = new NodeConnector(left, right, up, down);
						if(left != null) left.changeConnection(replacement, Direction.RIGHT);
						if(right != null) right.changeConnection(replacement, Direction.LEFT);
						if(up != null) up.changeConnection(replacement, Direction.DOWN);
						if(down != null) down.changeConnection(replacement, Direction.UP);
						replacement.moveTo(getLeft(), getTop());
						parent.addChild(replacement);
						parent.removeChild(StickyNodeConnector.this);
						parent.removeChild(c);
					}
				}
			}	
		}	
	}
	
	private class MakeUnstickyHandler implements RightMouseDownHandler {

		@Override
		public void onRightMouseDown(RightMouseDownEvent event) {
			event.cancel();
			StickyNodeConnector node = StickyNodeConnector.this;
			Canvas parent = node.getParentElement();
			parent.removeChild(node);
			NodeConnector unsticky = new NodeConnector(_left, _right, _up, _down);
			if(_left != null)
				_left.changeConnection(unsticky, Direction.RIGHT);
			if(_right != null)
				_right.changeConnection(unsticky, Direction.LEFT);
			if(_up != null)
				_up.changeConnection(unsticky, Direction.DOWN);
			if(_down != null)
				_down.changeConnection(unsticky, Direction.UP);
			unsticky.moveTo(node.getLeft(), node.getTop());
			parent.addChild(unsticky);		
		}		
	}	
}
