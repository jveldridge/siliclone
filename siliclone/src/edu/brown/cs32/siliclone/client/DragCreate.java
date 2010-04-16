package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.interfaces.OperatorFactory;

public class DragCreate extends Label {
	private String name;
	private OperatorFactory factory;
	public DragCreate(String name, OperatorFactory factory){
		super(name);
		this.factory = factory;
		this.name = name;
		setShowEdges(true);
		setCanDrag(true);
		setCanDrop(true);
		setDragType("b");
		setDragAppearance(DragAppearance.TRACKER);
	}
	public String getName(){
		return name;
	}
	public OperatorFactory getFactory(){
		return factory;
	}
}
