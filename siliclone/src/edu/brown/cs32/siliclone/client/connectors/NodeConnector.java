package edu.brown.cs32.siliclone.client.connectors;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;

public class NodeConnector extends BaseConnector implements Connectable {
	
	protected HandlerRegistration moveRegistration;
	protected HandlerRegistration rightClickRegistration;
	
	public NodeConnector(Connectable left, Connectable right, Connectable up, Connectable down)
	{
		super(left, right, up, down);
		this.setBackgroundColor(NODE_COLOR);
		this.setOverflow(Overflow.HIDDEN);
		this.resizeTo(LINE_WIDTH, LINE_WIDTH);
		this.setCanDragResize(false);
		moveRegistration = this.addDragRepositionMoveHandler(new NodeHandler());
		this.addDoubleClickHandler(new AddConnectionHandler());
		rightClickRegistration = this.addRightMouseDownHandler(new MakeStickyHandler());
	}
	
	public NodeConnector() {
		this(null, null, null, null);
	}
	
	public void adjustHorizontal(int change, Direction cameFrom)
	{
		this.setLeft(_beforeDrag.getLeft() + change);
		if(_left != null && cameFrom != Direction.LEFT)
			_left.adjustHorizontal(change, Direction.RIGHT);
		if(_right != null && cameFrom != Direction.RIGHT)
			_right.adjustHorizontal(change, Direction.LEFT);
		if(_up != null && cameFrom != Direction.UP)
			_up.adjustHorizontal(change, Direction.DOWN);
		if(_down != null && cameFrom != Direction.DOWN)
			_down.adjustHorizontal(change, Direction.UP);
	}
	
	public void adjustVertical(int change, Direction cameFrom)
	{
		this.setTop(_beforeDrag.getTop() + change);
		if(_left != null && cameFrom != Direction.LEFT)
			_left.adjustVertical(change, Direction.RIGHT);
		if(_right != null && cameFrom != Direction.RIGHT)
			_right.adjustVertical(change, Direction.LEFT);
		if(_up != null && cameFrom != Direction.UP)
			_up.adjustVertical(change, Direction.DOWN);
		if(_down != null && cameFrom != Direction.DOWN)
			_down.adjustVertical(change, Direction.UP);
	}
	
	private class NodeHandler implements DragRepositionMoveHandler {

		public void onDragRepositionMove(DragRepositionMoveEvent event) {
			//When you drag the node around, everything connected to it just rigidly translates
			if(_left != null)
			{
				_left.translate(getLeft() - _beforeDrag.getLeft(), getTop() - _beforeDrag.getTop(), Direction.RIGHT);
//				_left.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.RIGHT);
//				_left.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.RIGHT);
			}
			if(_right != null)
			{
				_right.translate(getLeft() - _beforeDrag.getLeft(), getTop() - _beforeDrag.getTop(), Direction.LEFT);
//				_right.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.LEFT);
//				_right.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.LEFT);
			}
			if(_up != null)
			{
				_up.translate(getLeft() - _beforeDrag.getLeft(), getTop() - _beforeDrag.getTop(), Direction.DOWN);
//				_up.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.DOWN);
//				_up.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.DOWN);
			}
			if(_down != null)
			{
				_down.translate(getLeft() - _beforeDrag.getLeft(), getTop() - _beforeDrag.getTop(), Direction.UP);
//				_down.adjustHorizontal(getLeft() - _beforeDrag.getLeft(), Direction.UP);
//				_down.adjustVertical(getTop() - _beforeDrag.getTop(), Direction.UP);
			}
			
		}
		
	}
	
	private class AddConnectionHandler implements DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			if(_up == null)
			{
				VerticalConnector up = new VerticalConnector(null, NodeConnector.this);
				up.moveTo(getLeft(), getTop() - up.getHeight());
				getParentElement().addChild(up);
				_up = up;
			}
			else if(_right == null)
			{
				HorizontalConnector right = new HorizontalConnector(NodeConnector.this, null);
				right.moveTo(getRight(), getTop());
				getParentElement().addChild(right);
				_right = right;
			}
			else if(_down == null)
			{
				VerticalConnector down = new VerticalConnector(NodeConnector.this, null);
				down.moveTo(getLeft(), getBottom());
				getParentElement().addChild(down);
				_down = down;
			}
			else if(_left == null)
			{
				HorizontalConnector left = new HorizontalConnector(null, NodeConnector.this);
				left.moveTo(getLeft() - left.getWidth(), getTop());
				getParentElement().addChild(left);
				_left = left;
			}
		}
	}
	
	private class MakeStickyHandler implements RightMouseDownHandler {


		public void onRightMouseDown(RightMouseDownEvent event) {
			event.cancel();
			//Can only switch to sticky node if some connection is missing
			if(_left == null || _right == null || _up == null || _down == null)
			{
				NodeConnector node = NodeConnector.this;
				Canvas parent = node.getParentElement();
				parent.removeChild(node);
				StickyNodeConnector sticky = new StickyNodeConnector(_left, _right, _up, _down);
				if(_left != null)
					_left.changeConnection(sticky, Direction.RIGHT);
				if(_right != null)
					_right.changeConnection(sticky, Direction.LEFT);
				if(_up != null)
					_up.changeConnection(sticky, Direction.DOWN);
				if(_down != null)
					_down.changeConnection(sticky, Direction.UP);
				sticky.moveTo(node.getLeft(), node.getTop());
				parent.addChild(sticky);

			}
		}
		
	}
}