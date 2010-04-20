package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.client.WorkspaceView;




/**
 * OpView displays an Operator object to be displayed in a WorkspaceView and manipulated
 * by the client.
 */
public class OpView extends VLayout {
	private final Operator op;
	private final PropertiesSelector selector;
	
	/**
	 * @param op An already initialized operator that this OpView described (not null)
	 */
	public OpView(final Operator op, final WorkspaceView workspace){
		this.op = op; //associate with operator, positions are related
		setTop(op.getY());
		setLeft(op.getX());
		
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
		
		addMember(close);
		
		//add the graphical representation of the operator
		Widget opWidget = op.getWidget();
		this.addMember(opWidget);
		
		this.setShowEdges(true);
		this.setEdgeSize(0); //This is the default, but w/out it getEdgeSize returns null
		this.setCanDragReposition(true);
		this.setKeepInParentRect(true);
		this.setDragAppearance(DragAppearance.TARGET);
		this.addDragRepositionStopHandler(new RepositionHandler());

		selector = op.getPropertiesSelector();
		
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
		//addMember(new Button("Show Selector", new ShowSelectorHandler()));
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
