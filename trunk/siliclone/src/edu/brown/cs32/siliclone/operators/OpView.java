package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.connectors2.InputNode;
import edu.brown.cs32.siliclone.client.connectors2.OutputDragger;




/**
 * OpView displays an Operator object to be displayed in a WorkspaceView and manipulated
 * by the client.
 */
public class OpView extends Canvas {//implements Connectable {
	private final Operator op;
	private final PropertiesSelector selector;
	private Rectangle _beforeDrag;
	private InputNode[] inputs;
	private OutputDragger output;
	
	private Progressbar progress;
	
	/**
	 * @param op An already initialized operator that this OpView described (not null)
	 */
	public OpView(final Operator op, final WorkspaceView workspace){
		this.op = op; //associate with operator, positions are related
		this.op.init();
		op.setOpView(this);
		this.addDragRepositionStopHandler(new RepositionHandler());
		
		setTop(op.getY());
		setLeft(op.getX());
		
		//add the graphical representation of the operator
		Canvas opWidgetC = new Canvas();
		opWidgetC.addChild(op.getWidget());
		this.addChild(opWidgetC);
		
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
		
		
		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		//this.addDragRepositionStopHandler(new RepositionHandler());
		this.addDragRepositionMoveHandler(new DragRepositionMoveHandler() {
			
			public void onDragRepositionMove(DragRepositionMoveEvent event) {
				for(InputNode i : inputs)
					if(i != null){
						i.reposition();
					}
				output.reposition();
//				int horizontal = getLeft() - _beforeDrag.getLeft();
//				int vertical = getTop() - _beforeDrag.getTop();
			}
		});
//		this.addDragRepositionStartHandler(new DragRepositionStartHandler() {
//			
//			public void onDragRepositionStart(DragRepositionStartEvent event) {
//				startDrag(null);
//			}
//		});

		selector = op.getPropertiesSelector();
		
		initPropertiesSelector();
		/*
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
		*/
		 initPropertiesSelector();
         
         opWidgetC.addClickHandler(new ClickHandler() {
                 public void onClick(ClickEvent event) {
                	 	bringToFront();
                         if (selector.isVisible()) {
                                 selector.hide();
                         }
                         else {
                                 selector.show();
                                 selector.bringToFront();
                         }
                 }
         });
         
         inputs = new InputNode[op.getNumInputs()];
         for(int i = 0; i < inputs.length; i ++){
        	 inputs[i] = new InputNode(i, op);
        	 addChild(inputs[i]);
        	 inputs[i].setTop(0);
        	 inputs[i].setLeft(getWidth() * i / inputs.length + close.getWidth());
         }
         output = new OutputDragger(op, getWidth() / 2, getHeight());
         addChild(output);
         output.setTop(getHeight());
         output.setLeft(getWidth() / 2 + output.getWidth() / 2);
         bringToFront();
         
         progress = new Progressbar();
         progress.setBreadth(15);
         progress.setLength(getWidth() - 20);
         addChild(progress);
         progress.setLeft(5);
         progress.setTop(getHeight() - progress.getHeight());
         progress.hide();
	}
	
	public InputNode[] getInputs(){
		return inputs;
	}
	
	public OutputDragger getOutput(){
		return output;
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
	
	
	private void showProgressBar(){
		progress.show();
	}
	private void hideProgressBar(){
		progress.hide();
	}
	public void setProgress(int percent){
		if(percent >= 100){
			hideProgressBar();
		}else{
			showProgressBar();
			progress.setPercentDone(percent);
		}
	}
	
	/*
	public void addConnection(Connectable toAdd, Direction dir) {		// TODO Auto-generated method stub
		//Should not add connections to the OpView directly - the output and input lines
		//are created with the OpView and should never change
	}

	public void adjustHorizontal(int change, Direction cameFrom) {
		this.setLeft(_beforeDrag.getLeft() + change);
		op.setX(this.getLeft());
	}

	public void adjustVertical(int change, Direction cameFrom) {
		this.setTop(_beforeDrag.getTop() + change);
		op.setY(this.getTop());
	}

	public void changeConnection(Connectable toAdd, Direction dir) {
		// TODO Auto-generated method stub
		
	}

	public void removeConnection(Connectable toRemove) {
		// TODO Auto-generated method stub
		
	}

	public void startDrag(Direction dir) {
		_beforeDrag = this.getRect();
	}

	public void translate(int horizontal, int vertical, Direction cameFrom) {
		this.setLeft(_beforeDrag.getLeft() + horizontal);
		this.setTop(_beforeDrag.getTop() + vertical);
		op.setX(getLeft());
	}
	*/
	
}
