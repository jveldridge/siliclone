package edu.brown.cs32.siliclone.client;

import com.smartgwt.client.widgets.tree.TreeNode;

import edu.brown.cs32.siliclone.operators.OperatorTemplate;

/**
 * Provides the display for an OperatorTemplate to
 * be shown in the OpTemplateListing on the left-hand
 * side of the main screen.
 * 
 * OpTemplateViews are dragged out of the OpTemplateListing
 * into a WorkspaceView to create new Operators in the underlying
 * Workspaces.
 */
public class OpTemplateView extends TreeNode {
	private OperatorTemplate template;
	
	public OpTemplateView(OperatorTemplate template) {
		super(template.getName());
		this.template = template;
		this.setIcon(template.getIconPath());
	}
	
	/**
	 * Returns the OperatorTemplate for which this
	 * OpTemplateView provides the graphical representation.
	 * 
	 * @return the OperatorTemplate for which this
	 * OpTemplateView provides the graphical representation.
	 */
	public OperatorTemplate getTemplate() {
		return template;
	}
}
