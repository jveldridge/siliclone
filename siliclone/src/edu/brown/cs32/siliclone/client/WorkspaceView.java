package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.util.EventHandler;

import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.operators.Operator;

/**
 * The WorkspaceView is a graphical representation of the workspace, that the client
 * manipulate by adding, editing, and removing operators represented by OpView objects.
 */
public class WorkspaceView extends Canvas {
	
	private Workspace workspace; 
	
	public WorkspaceView(Workspace workspace){
		setHeight100();
		setWidth100();
		setShowEdges(true);
		
		
		this.workspace = workspace;
		for(Operator op : workspace.getOperators()){ //TODO validation of op position?
			addChild(new OpView(op));
		}
		
		setCanAcceptDrop(true);
		
		addDropOverHandler(new HoverHandle());
		addDropHandler(new WorkDropHandle());
	}
	
	/**
	 * Add a single operator to view and to underlying workspace object.
	 * TODO validation here? 
	 * @param op
	 */
	private void addOperator(Operator op){
		workspace.addOperator(op);
		addChild(new OpView(op));
	}
	
	/**
	 * Should change color when client considers dropping something in it
	 * The only thing a client should be able to drop is something that goes in
	 * the workspace.
	 * TODO - do we want this?
	 */
	private class HoverHandle implements DropOverHandler{
		public void onDropOver(DropOverEvent event) {
			setBackgroundColor("#ffffff"); //right now "changes" to white so no visible effect
		}
		
	}
	
	/**
	 * This handles when anything is dropped - 
	 * The only thing that should be dropped is an OpTemplateView,
	 * which is unfortunately wrapped in a tree grid. 
	 */
	private class WorkDropHandle implements DropHandler {
		public void onDrop(DropEvent event) {
			// TODO Auto-generated method stub
			Canvas dropped = EventHandler.getDragTarget();
			if(dropped instanceof TreeGrid){
				ListGridRecord r = ((TreeGrid) dropped).getSelectedRecord();
				if(r instanceof OpTemplateView){
					Operator newOp = ((OpTemplateView) r).getFactory().makeOperator();
				
					newOp.setX(getOffsetX());
					newOp.setY(getOffsetY());
				
					addOperator(newOp);
				}
			}
		}
	}
	
}