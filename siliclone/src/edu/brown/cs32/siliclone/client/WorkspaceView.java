package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.util.EventHandler;

import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.operators.OpView;
import edu.brown.cs32.siliclone.operators.Operator;

/**
 * The WorkspaceView is a graphical representation of the workspace, that the client
 * manipulate by adding, editing, and removing operators represented by OpView objects.
 */
public class WorkspaceView extends Canvas {
	
	private Workspace workspace; 
	
	public WorkspaceView(Workspace workspace){
		this.setHeight100();
		this.setWidth100();
		this.setShowEdges(true);
		
		
		this.workspace = workspace;
		for(Operator op : workspace.getOperators()){ //TODO validation of op position?
													  //what constitutes a valid position?
			addChild(new OpView(op));
		}
		
		this.setCanAcceptDrop(true);
		
		this.addDropOverHandler(new HoverHandle());
		this.addDropHandler(new WorkDropHandle());
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
	
	public void removeOperator() {
		
	}
	
	/**
	 * Should change color when client considers dropping something in it
	 * The only thing a client should be able to drop is something that goes in
	 * the workspace.
	 */
	private class HoverHandle implements DropOverHandler{
		public void onDropOver(DropOverEvent event) {
			setBackgroundColor("#FAF8CC");
		}
		
	}
	
	/**
	 * This handles when anything is dropped - 
	 * The only thing that should be dropped is an OpTemplateView,
	 * which is unfortunately wrapped in a tree grid. 
	 */
	private class WorkDropHandle implements DropHandler {
		public void onDrop(DropEvent event) {
			setBackgroundColor("#FFFFFF");
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