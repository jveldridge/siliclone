package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.docs.Positioning;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.client.connectors.StickyNodeConnector;
import edu.brown.cs32.siliclone.client.connectors.VerticalConnector;




/**
 * OpView displays an Operator object to be displayed in a WorkspaceView and manipulated
 * by the client.
 */
public class OpView extends Canvas {
	private final Operator op;
	private final PropertiesSelector selector;
	
	/**
	 * @param op An already initialized operator that this OpView described (not null)
	 */
	public OpView(final Operator op, final WorkspaceView workspace){
		this.op = op; //associate with operator, positions are related
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
		
		//add a connector
		VerticalConnector out = new VerticalConnector();
		out.setCanDragReposition(true);
		out.setKeepInParentRect(true);
		out.setDragAppearance(DragAppearance.TARGET);
		out.setDragTarget(this);
		out.setHeight(30);
		
		out.setTop(this.getBottom());
		out.setLeft((this.getLeft() + this.getRight())/2);
		
		this.addPeer(out);
		
		StickyNodeConnector node = new StickyNodeConnector();
		node.setTop(out.getBottom());
		node.setLeft(out.getLeft());
		node.setDragTarget(this);
		this.addPeer(node);
		
		

		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		this.addDragRepositionStopHandler(new RepositionHandler());

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
}
