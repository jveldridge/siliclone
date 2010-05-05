package edu.brown.cs32.siliclone.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers.VisualizerDisplay;

@SuppressWarnings("serial")
public abstract class AbstractOperator implements Operator {
	
	protected int x, y;
	protected Operator[] inputs = new Operator[getNumInputs()];
	protected Collection<SequenceHook> outputSequence; 
	protected Collection<Operator> children = new ArrayList<Operator>();
	private transient VisualizerDisplay visualizerDisplay;
	private transient Window visualizerDisplayWindow;
	
	protected transient OpView view;
	
	public void init() {
		visualizerDisplay = new VisualizerDisplay(this);
	}
	
	public void setProgress(int percent){
		if(view != null){
			view.setProgress(percent);
		}
	}
	
	public void setOpView(OpView view){
		this.view = view;
	}
	public OpView getOpView(){
		return view;
	}
	
	//TODO better algorithm? if this is used when there already was a loop, goes into infinite loop
	//used to check if the operator is connected to itself through inputs
	private boolean containsCycle(Operator op, Operator start){
		for(Operator input : op.getChildren()){
			if(op == start){
				return true;
			}
			if(input != null && containsCycle(input, start)){
				return true;
			}
		}
		return false;
	}
	
	public void setInput(int slotNum, Operator input) throws OperatorCycleException {
		if(slotNum > inputs.length) {
			return;
		}
		input.addChild(this);
		
		inputs[slotNum] = input;
	}

	public Operator[] getInputs(){
		return inputs;
	}
	
	public void addChild(Operator op) throws OperatorCycleException {
		children.add(op);
		if(this.containsCycle(op, this)){
			children.remove(op);
			throw new OperatorCycleException();
		}
	}
	
	public void removeChild(Operator op){
		children.remove(op);
	}
	
	public void removeInput(int slotNum){
		if(0 <= slotNum && slotNum < inputs.length){
			inputs[slotNum].removeChild(this);
			inputs[slotNum] = null;
		}
	}
	
	public Collection<Operator> getChildren(){
		return children;
	}
	
	public void cancel() {
		
	}
	
//	remove
	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Collection<SequenceHook> getOutputSequence() {
		return outputSequence;
	}
	
	public void showVisualizerDisplay(){
		if(visualizerDisplayWindow==null){
			visualizerDisplayWindow = new Window();
			
			visualizerDisplayWindow.setTitle("Visualizations");
			visualizerDisplayWindow.setShowCloseButton(true);
			visualizerDisplayWindow.setHeight(400);
			visualizerDisplayWindow.setWidth(400);
			
			visualizerDisplayWindow.addItem(visualizerDisplay);
			
			visualizerDisplayWindow.setCanDragReposition(true);
			visualizerDisplayWindow.setCanDragResize(true);
			
			visualizerDisplayWindow.addCloseClickHandler(new CloseClickHandler() {
				
				public void onCloseClick(CloseClientEvent event) {
					visualizerDisplayWindow.hide();
					
				}
			});
			
		}
		visualizerDisplayWindow.show();
		
	}
	
	
}
