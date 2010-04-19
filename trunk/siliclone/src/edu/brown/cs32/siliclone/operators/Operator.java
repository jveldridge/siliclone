package edu.brown.cs32.siliclone.operators;

import java.io.Serializable;
import java.util.Collection;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.workspace.CompletedListener;

/**
 * Specifies the methods of an Operator object that will be used
 * in the workspace to perform operations on DNASequences.
 * 
 * @author jeldridg
 */
public interface Operator extends Serializable {

	/**
	 * Sets the DNASequences that are given as input to a particular 
	 * input slot on the Operator.
	 * 
	 * @param slotNum the number of the slot to which the input is being added
	 * @param input the DNASequences that should be the input to this slot
	 */
	public void setInput(int slotNum, Operator input);
	
	/**
	 * Causes the Operator to show a floating window that allows the user to
	 * select properties for the operator.
	 * >>> It probably makes more sense for this to be a getPropertiesSelector method
	 *     that returns a PropertiesSelector object (which the workspace can then
	 *     display appropriately), right?
	 */
	public Widget getPropertiesSelector();
	
	public Widget getWidget();
	
	public void addCompletedListener(CompletedListener l);
	
	/**
	 * Runs the Operation that this Operator controls.  This method simply calls
	 * the calculate method of the corresponding Operation class, passing in the
	 * DNASequences that should be operated on and the properties governing the
	 * operation.
	 */
	public void calculate();
	
	/**
	 * Cancels a running calculate() operation.  When the calculate() operation
	 * is not running, this method has no effect.  (Indeed, there will be no way
	 * for it to be called from the UI of the Operator).
	 */
	public void cancel();
	
	public void connect(Operator adjacent);
	
	/**
	 * Returns the Operators adjacent to this Operator; that is, all Operators
	 * that have the output of this Operator as an input.
	 * 
	 * @return the Operators adjacent to this Operator
	 */
	public Collection<Operator> getAdjacentOperators();
	
	/**
	 * @return The x-coordinate for this operator placed on a workspace.
	 */
	int getX();
	/**
	 * @return The y-coordinate for this operator placed on a workspace.
	 */
	int getY();
	void setX(int x); 
	void setY(int y);
	
}
