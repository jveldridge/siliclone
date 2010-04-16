package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.tree.TreeNode;

import edu.brown.cs32.siliclone.interfaces.OperatorFactory;

public class DragCreate extends TreeNode {
	private OperatorFactory factory;
	public DragCreate(OperatorFactory factory){
		super(factory.getName());
		this.factory = factory;
	}
	public OperatorFactory getFactory(){
		return factory;
	}
}
