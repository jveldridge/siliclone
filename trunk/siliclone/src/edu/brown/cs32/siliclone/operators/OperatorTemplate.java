package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;


public interface OperatorTemplate {
	/**
	 * Returns the graphical Widget used to display this OperatorTemplate
	 * (in the tree of Operators displayed on the left of the home screen)
	 * 
	 * @return the graphical Widget used to display this OperatorTemplate
	 */
	public Widget getWidget();
	
	/**
	 * Returns an actual instance of this Operator to be used in the workspace.
	 * Will be called when the OperatorTemplate is dragged from the selection
	 * tree on the left into the workspace.
	 * 
	 * @return an actual instance of this Operator to be used in the workspace.
	 */
	public Operator makeOperator();
	
	/**
	 * Returns the name of the Operator with which this OperatorTemplate is 
	 * associated.
	 * 
	 * @return the name of the Operator with which this OperatorTemplate is 
	 * associated.
	 */
	public String getName();
	
	public String getIconPath();
}
