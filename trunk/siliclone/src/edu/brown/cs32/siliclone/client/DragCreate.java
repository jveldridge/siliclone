package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.interfaces.OperatorFactory;

public class DragCreate extends Label {
	private OperatorFactory factory;
	public DragCreate(OperatorFactory factory){
		super(factory.getName());
		this.factory = factory;
		setShowEdges(true);
		setCanDrag(true);
		setCanDrop(true);
		setDragType("b");
		setDragAppearance(DragAppearance.TRACKER);
	}
	public String getName(){
		return factory.getName();
	}
	public OperatorFactory getFactory(){
		return factory;
	}
}
