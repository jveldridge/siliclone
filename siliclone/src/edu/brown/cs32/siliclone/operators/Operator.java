package edu.brown.cs32.siliclone.operators;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;

import edu.brown.cs32.siliclone.client.visualizers2.VisualizerDisplay;
import edu.brown.cs32.siliclone.client.workspace.CompletedListener;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;

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
	 * @throws OperatorConnectionException 
	 */
	public void setInput(int slotNum, Operator input) throws OperatorCycleException;
	
	public int getNumInputs();
	
	public Operator[] getInputs();
	
	public void setProperties(Map<String, String> properties);
	
	public void removeInput(int slotNum);
	
	public void addChild(Operator op) throws OperatorCycleException;
	
	public Collection<Operator> getChildren();
	
	public void removeChild(Operator op);
	
	public void setProgress(int percent);
	
	public void setOpView(OpView view);
	public OpView getOpView();
	
	/**
	 * Method to be called upon construction of an Operator to initialize
	 * its GUI components.  (This is necessary because these components do
	 * not get serialized, so they must be reconstructed when an object is
	 * deserialized).
	 */
	public void init();

	/**
	 * Returns the SequenceHook representing the DNA sequence that results
	 * from running this operation with its current input.  If the output
	 * sequence is not currently valid, the SequenceHook returned will cause
	 * a NoSuchSequenceException to be thrown if it is passed to any RPC
	 * that accesses the sequence database.
	 * 
	 * @return the SequenceHook representing the DNA sequence that results
	 * from running this operation with its current input
	 */
	public Collection<SequenceHook> getOutputSequence();
	
	
	//NOTE Operator is serializable! these need to be transient or (even better) not fields
	/**
	 * Causes the Operator to show a floating window that allows the user to
	 * select properties for the operator.
	 * >>> It probably makes more sense for this to be a getPropertiesSelector method
	 *     that returns a PropertiesSelector object (which the workspace can then
	 *     display appropriately), right?
	 *     
	 */
	public PropertiesSelector getPropertiesSelector();
	
	public Widget getWidget();
	
	public Map getProperties();
	
	
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
/**
 * Returns the graphical representation of the set of visualizers associated with this
 * operator
 * @return
 */
	public void showVisualizerDisplay();
	
}
