package edu.brown.cs32.siliclone.client.workspace;

import com.smartgwt.client.widgets.tree.TreeNode;

import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class DragCreate extends TreeNode {
	private OperatorTemplate factory;
	public DragCreate(OperatorTemplate factory){
		super(factory.getName());
		this.factory = factory;
	}
	public OperatorTemplate getFactory(){
		return factory;
	}
}
