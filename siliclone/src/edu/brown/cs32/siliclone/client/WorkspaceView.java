package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC; //very handy for debug

import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.tree.TreeGrid;
public class WorkspaceView extends Canvas {
	public WorkspaceView(){
		setCanAcceptDrop(true);
		
		setHeight100();
		setWidth100();
		setShowEdges(true);
		
		addDropOverHandler(new HoverHandle());
		addDropHandler(new WorkDropHandle());
//		addShowContextMenuHandler(new RightClickHandler());
	}
	private class HoverHandle implements DropOverHandler{
		public void onDropOver(DropOverEvent event) {
			setBackgroundColor("#ffffff");
		}
		
	}
	private class WorkDropHandle implements DropHandler {
		public void onDrop(DropEvent event) {
			// TODO Auto-generated method stub
			Canvas dropped = EventHandler.getDragTarget();
			if(dropped instanceof TreeGrid){
				DragCreate operatorSelected = (DragCreate) ((TreeGrid) dropped).getSelectedRecord();
				OperatorDragger newOp = new OperatorDragger(operatorSelected.getFactory().makeOperator());
				newOp.setTop(getOffsetY());
				newOp.setLeft(getOffsetX());
			
				Layout opSelector = newOp.getSelectorPopup();
				opSelector.setTop(getOffsetY());
				opSelector.setLeft(newOp.getRight() + newOp.getEdgeSize() + opSelector.getEdgeSize());
				
				addChild(newOp);
				newOp.addPeer(opSelector);
			}
		}
	}
	/**
	private class RightClickHandler implements ShowContextMenuHandler {
		@Override
		public void onShowContextMenu(ShowContextMenuEvent event) {
			// TODO Auto-generated method stub
			event.cancel();
			SC.say("No right clicking!"); 
		}
	}**/
	
}


/* {
public Canvas(){
             setCanAcceptDrop(true);  
  
             addShowContextMenuHandler(new ShowContextMenuHandler() {  
                 public void onShowContextMenu(ShowContextMenuEvent event) {  
                     event.cancel();  
                 }  
             });  
             addDropOverHandler(new DropOverHandler() {  
                 public void onDropOver(DropOverEvent event) {  
                     //if (willAcceptDrop())  
                         setBackgroundColor("#ffff80");  
                 }  
             });  
             addDropOutHandler(new DropOutHandler() {  
                 public void onDropOut(DropOutEvent event) {  
                     setBackgroundColor("#ffffff");  
                 }  
             });  
             addDropHandler(new DropHandler() {  
                 public void onDrop(DropEvent event) {  
                     addChild(new Label("Test"));
                       
                 }  
             });  
}
}; 
*/   
