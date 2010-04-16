package edu.brown.cs32.siliclone.client;


import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;

import edu.brown.cs32.siliclone.interfaces.Operator;



public class OperatorDragger extends Canvas {
	private Operator op;
	public OperatorDragger(Operator op){
		this.op = op;
		
		Widget opWidget = op.getWidget();
		addChild(opWidget);
		setShowEdges(true);
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
		setHeight(opWidget.getOffsetHeight());
		setWidth(opWidget.getOffsetWidth());
		
		
		addShowContextMenuHandler(new RightClickHandler());
	}
	private class RightClickHandler implements ShowContextMenuHandler {
		@Override
		public void onShowContextMenu(ShowContextMenuEvent event) {
			// TODO Auto-generated method stub
			PopupPanel p = new PopupPanel();
			p.add(op.getPropertiesSelector());
			p.show();
			p.hide();
		}
	}
	
	
}
