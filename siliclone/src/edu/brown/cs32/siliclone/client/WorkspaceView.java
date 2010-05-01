package edu.brown.cs32.siliclone.client;

import java.util.HashMap;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.util.EventHandler;

import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.brown.cs32.siliclone.client.connectors.StickyNodeConnector;
import edu.brown.cs32.siliclone.client.connectors2.InputNode;
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
		
		HashMap<Operator, OpView> opViews = new HashMap<Operator, OpView>(); //this is necessary for connecting
		for(Operator op : workspace.getOperators()){ //TODO validation of op position?
			opViews.put(op, new OpView(op, this));
			op.getPropertiesSelector().hide();
			addChild(opViews.get(op));
		}
		for(Operator op : workspace.getOperators()){
			for(int i = 0; i < op.getInputs().size(); i++){
				Operator input = op.getInputs().get(i);
				if(input != null){
					opViews.get(op).getInputs()[i].connect(
							opViews.get(input).getOutput());
				}
			}
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
		addChild(new OpView(op, this));
	}
	
	public void removeOperator(Operator op, OpView view) {
		//remove the operator from the workspace
		workspace.removeOperator(op);
		
		//and remove the OpView from the Workspaceview
		for(InputNode i : view.getInputs()){
			i.disconnect();
		}
		view.getOutput().disconnect();
		removeChild(view);
	}
	
	/**
	 * Should change color when client considers dropping something in it
	 * The only thing a client should be able to drop is something that goes in
	 * the workspace.
	 */
	private class HoverHandle implements DropOverHandler{
		public void onDropOver(DropOverEvent event) {
			if(EventHandler.getDragTarget() instanceof OpView){
				setBackgroundColor("#FAF8CC");
			}
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

	public Workspace getWorkspace() {
		return workspace;
	}
	
}