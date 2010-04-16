package edu.brown.cs32.siliclone.client;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.layout.Layout;

import edu.brown.cs32.siliclone.interfaces.Operator;



public class OperatorDragger extends Layout {
	private Operator op;
	private Layout selector;
	public OperatorDragger(Operator op){
		this.op = op;
		
		Widget opWidget = op.getWidget();
		addMember(opWidget);
		setShowEdges(true);
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
		
		
		selector = new Layout();
		selector.setTitle("Properties Selector:");
		selector.addMember(op.getPropertiesSelector());
		selector.addMember(new Button("close", new HideSelectorHandler()));
		selector.setShowEdges(true);
		selector.setCanDragReposition(true);
		selector.setKeepInParentRect(true);
		selector.setDragAppearance(DragAppearance.TARGET);
		selector.hide();
		
		addShowContextMenuHandler(new RightClickHandler());
	}
	public Layout getSelectorPopup(){
		return selector;
	}
	
	
	private class RightClickHandler implements ShowContextMenuHandler {
		@Override
		public void onShowContextMenu(ShowContextMenuEvent event) {
			event.cancel();
			//SC.say("right click");
			// TODO Auto-generated method stub
			selector.show();
		}
	}
	private class HideSelectorHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			selector.hide();
		}
	}
	
	
}
