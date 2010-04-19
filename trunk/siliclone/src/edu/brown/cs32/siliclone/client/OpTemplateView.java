package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.tree.TreeNode;

import edu.brown.cs32.siliclone.operators.OperatorTemplate;

/**
 * OpTemplateView directly represents the operator template singleton
 * TODO fix factory/template naming   
 */
public class OpTemplateView extends TreeNode {
	private OperatorTemplate factory;
	public OpTemplateView(OperatorTemplate factory){
		super(factory.getName());
		this.factory = factory; //TODO should a this menu item have a specific icon?
	}
	public OperatorTemplate getFactory(){
		return factory;
	}
}
