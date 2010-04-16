package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC; //very handy for debug

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.*;
public class Workspace extends Canvas {
	public Workspace(){
		setCanAcceptDrop(true);
		
		setHeight100();
		setWidth100();
		setShowEdges(true);
		
		addDropOverHandler(new HoverHandle());
		addDropHandler(new WorkDropHandle());
		addShowContextMenuHandler(new RightClickHandler());
	}
	private class HoverHandle implements DropOverHandler{
		@Override
		public void onDropOver(DropOverEvent event) {
			setBackgroundColor("#ffffff");
		}
		
	}
	private class WorkDropHandle implements DropHandler {
		@Override
		public void onDrop(DropEvent event) {
			// TODO Auto-generated method stub
			SC.say("Something was added to the workspace");
			addChild(new WidgetDisplay(((DragCreate) EventHandler.getDragTarget()).getName()));
		}
	}
	private class RightClickHandler implements ShowContextMenuHandler {
		@Override
		public void onShowContextMenu(ShowContextMenuEvent event) {
			// TODO Auto-generated method stub
			event.cancel();
			SC.say("No right clicking!"); 
		}
	}
	
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
