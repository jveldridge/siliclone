package edu.brown.cs32.siliclone.client;


import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.layout.Layout;

import edu.brown.cs32.siliclone.interfaces.Operator;



public class OperatorDragger extends Layout {
	private Operator op;
	public OperatorDragger(Operator op){
		this.op = op;
		
		Widget opWidget = op.getWidget();
		addMember(opWidget);
		setShowEdges(true);
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
		
		
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
