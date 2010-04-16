package edu.brown.cs32.siliclone.client;


import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;

import edu.brown.cs32.siliclone.interfaces.Operator;

public class OperatorDragger extends Canvas {
	private Operator op;
	public OperatorDragger(Operator op){
		this.op = op;
		addChild(op.getWidget());
		setShowEdges(true);
		setCanDragReposition(true);
		setKeepInParentRect(true);
		setDragAppearance(DragAppearance.TARGET);
	}
	private class RightClickHandler implements ShowContextMenuHandler {
		@Override
		public void onShowContextMenu(ShowContextMenuEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}
