package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.connectors.Connectable;
import edu.brown.cs32.siliclone.client.connectors.Direction;
import edu.brown.cs32.siliclone.client.connectors.StickyNodeConnector;
import edu.brown.cs32.siliclone.client.connectors.VerticalConnector;




/**
 * OpView displays an Operator object to be displayed in a WorkspaceView and manipulated
 * by the client.
 */
public class OpView extends Canvas implements Connectable {
	private final Operator op;
	private final PropertiesSelector selector;
	private VerticalConnector _output;
	private VerticalConnector _input;
	private Rectangle _beforeDrag;
	
	/**
	 * @param op An already initialized operator that this OpView described (not null)
	 */
	public OpView(final Operator op, final WorkspaceView workspace){
		this.op = op; //associate with operator, positions are related
		this.op.init();
		setTop(op.getY());
		setLeft(op.getX());
		
		//add the graphical representation of the operator
		Widget opWidget = op.getWidget();
		this.addChild(opWidget);
		
		//add close button
		final Img close = new Img("close.png", 20, 20);
		
		//on mouseover, show black close button
		close.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				close.setSrc("close_mouseOver.png");
			}
		});
		
		//on mouseout, show gray close button
		close.addMouseOutHandler(new MouseOutHandler() {			
			public void onMouseOut(MouseOutEvent event) {
				close.setSrc("close.png");
			}
		});
		
		//on click, remove the operator
		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				workspace.removeOperator(op, OpView.this);
			}
		});
		
		//addMember(close);
		addChild(close);
		
		//this.addMember(opWidget);
		
		//add a connector for output
		_output = new VerticalConnector(this, null);
		_output.setHeight(30);
		_output.setTop(this.getBottom());
		_output.setLeft((this.getLeft() + this.getRight())/2);
		
		this.addPeer(_output);
		
		
		StickyNodeConnector node = new StickyNodeConnector(null, null, _output, null);
		_output.addConnection(node, Direction.DOWN);
		node.setTop(_output.getBottom());
		node.setLeft(_output.getLeft());
		this.addPeer(node);
		
		//add a connector for input
		_input = new VerticalConnector(null, this);
		_input.setHeight(30);
		_input.moveTo((this.getLeft() + this.getRight())/2, this.getTop() - _input.getHeight());
		this.addPeer(_input);
		
		StickyNodeConnector inNode = new StickyNodeConnector(null, null, null, _input);
		_input.addConnection(inNode, Direction.UP);
		inNode.moveTo(_input.getLeft(), _input.getTop() - inNode.getHeight());
		this.addPeer(inNode);

		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		this.addDragRepositionStopHandler(new RepositionHandler());
		this.addDragRepositionMoveHandler(new DragRepositionMoveHandler() {
			
			public void onDragRepositionMove(DragRepositionMoveEvent event) {
				int horizontal = getLeft() - _beforeDrag.getLeft();
				int vertical = getTop() - _beforeDrag.getTop();
				if(_input != null)
					_input.translate(horizontal, vertical, Direction.DOWN);
				if(_output != null)
					_output.translate(horizontal, vertical, Direction.UP);
			}
		});
		this.addDragRepositionStartHandler(new DragRepositionStartHandler() {
			
			public void onDragRepositionStart(DragRepositionStartEvent event) {
				startDrag(null);
			}
		});

		selector = op.getPropertiesSelector();
		
		initPropertiesSelector();
		
		//add the run button
		final Img run = new Img("runButton.png", 20, 20);
		run.setVisible(false);
		run.setTop(80);
		
		run.addMouseOverHandler(new MouseOverHandler() {	
			public void onMouseOver(MouseOverEvent event) {
				run.setSrc("runButton_mouseOver.png");
			}
		});
		
		run.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				run.setSrc("runButton.png");
			}
		});

		this.addMouseOverHandler(new MouseOverHandler() {		
			public void onMouseOver(MouseOverEvent event) {
				run.setVisible(true);
			}
		});
		
		this.addMouseOutHandler(new MouseOutHandler() {	
			public void onMouseOut(MouseOutEvent event) {
				run.setVisible(false);
			}
		});
		
		run.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.say("Run was clicked");
			}
		});
		
		this.addChild(run);
		
		 initPropertiesSelector();
         
         this.addClickHandler(new ClickHandler() {
                 public void onClick(ClickEvent event) {
                         if (selector.isVisible()) {
                                 selector.hide();
                         }
                         else {
                                 selector.show();
                         }
                 }
         });

	}
	
	/**
	 * Creates a separate draggable window in the workspace for the 
	 * properties selector, which is anchored directly to this operator.
	 */
	private void initPropertiesSelector() {
		//PropertiesSelector selector = op.getPropertiesSelector();
		//selector.addMember(new Label("Properties Selector:"));
		//selector.addMember(op.getPropertiesSelector());
		//selector.addMember(new Button("close", new HideSelectorHandler()));
		//selector.setShowEdges(true);
		//selector.setEdgeSize(5); //This is the default, but w/out it getEdgeSize returns null
		selector.setCanDragReposition(true);
		selector.setKeepInParentRect(true);
		selector.setDragAppearance(DragAppearance.TARGET);
		
		selector.setTop(getTop());
		selector.setLeft(getRight());
		
		addPeer(selector);
	}
	
	
	/**
	 * Handles the close button for this opview's selector view. 
	 */
	private class HideSelectorHandler implements ClickHandler{
		public void onClick(ClickEvent event) {
			selector.hide();
		}
	}
	/**
	 * The purpose of this is to handle updating the operator object to
	 *  reflect the position of the operator view
	 */
	private class RepositionHandler implements DragRepositionStopHandler{
		public void onDragRepositionStop(DragRepositionStopEvent event) {
			op.setX(getLeft());
			op.setY(getTop());
		}
		
	}
	public void addConnection(Connectable toAdd, Direction dir) {		// TODO Auto-generated method stub
		//Should not add connections to the OpView directly - the output and input lines
		//are created with the OpView and should never change
	}

	public void adjustHorizontal(int change, Direction cameFrom) {
		this.setLeft(_beforeDrag.getLeft() + change);
		op.setX(this.getLeft());
		if(_input != null && cameFrom != Direction.UP)
			_input.adjustHorizontal(change, Direction.DOWN);
		if(_output != null && cameFrom != Direction.DOWN)
			_output.adjustHorizontal(change, Direction.UP);		
	}

	public void adjustVertical(int change, Direction cameFrom) {
		this.setTop(_beforeDrag.getTop() + change);
		op.setY(this.getTop());
		if(_input != null && cameFrom != Direction.UP)
			_input.adjustVertical(change, Direction.DOWN);
		if(_output != null && cameFrom != Direction.DOWN)
			_output.adjustVertical(change, Direction.UP);
		
	}

	public void changeConnection(Connectable toAdd, Direction dir) {
		// TODO Auto-generated method stub
		
	}

	public void removeConnection(Connectable toRemove) {
		// TODO Auto-generated method stub
		
	}

	public void startDrag(Direction dir) {
		_beforeDrag = this.getRect();
		if(_input != null && dir != Direction.UP)
			_input.startDrag(Direction.DOWN);
		if(_output != null && dir != Direction.DOWN)
			_output.startDrag(Direction.UP);
		
	}

	public void translate(int horizontal, int vertical, Direction cameFrom) {
		this.setLeft(_beforeDrag.getLeft() + horizontal);
		this.setTop(_beforeDrag.getTop() + vertical);
		op.setX(getLeft());
		op.setY(getTop());
		if(_input != null && cameFrom != Direction.UP)
			_input.translate(horizontal, vertical, Direction.DOWN);
		if(_output != null && cameFrom != Direction.DOWN)
			_output.translate(horizontal, vertical, Direction.UP);
	}
}
