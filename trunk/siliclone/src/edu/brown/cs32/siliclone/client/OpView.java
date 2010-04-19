package edu.brown.cs32.siliclone.client;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.operators.Operator;



/**
 * OpView displays an Operator object to be displayed in a WorkspaceView and manipulated
 * by the client.
 */
public class OpView extends VLayout {
	private final Operator op;
	private final Layout selector;
	
	/**
	 * @param op An already initialized operator that this OpView described (not null)
	 */
	public OpView(Operator op){
		this.op = op; //associate with operator, positions are related
		setTop(op.getY());
		setLeft(op.getX());
		
		Widget opWidget = op.getWidget(); //display operator's information
		addMember(opWidget);
		
		setShowEdges(true);
		setEdgeSize(5); //This is the default, but w/out it getEdgeSize returns null
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
		addDragRepositionStopHandler(new RepositionHandler());

		selector = new VLayout();
		initPropertiesSelector();
		
		addMember(new Button("Show Selector", new ShowSelectorHandler()));
	}
	
	/**
	 * Creates a separate draggable window in the workspace for the 
	 * properties selector, which is anchored directly to this operator.
	 */
	private void initPropertiesSelector(){
		selector.addMember(new Label("Properties Selector:"));
		selector.addMember(op.getPropertiesSelector());
		selector.addMember(new Button("close", new HideSelectorHandler()));
		selector.setShowEdges(true);
		selector.setEdgeSize(5); //This is the default, but w/out it getEdgeSize returns null
		selector.setCanDragReposition(true);
		selector.setKeepInParentRect(true);
		selector.setDragAppearance(DragAppearance.TARGET);
		
		selector.setTop(getTop());
		selector.setLeft(getRight() + getEdgeSize() + selector.getEdgeSize());
		
		addPeer(selector);
	}
	
	
	
	/**
	 * Handles when the "show options" button on this selector's view is clicked.
	 */
	private class ShowSelectorHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			selector.show();
		}
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
