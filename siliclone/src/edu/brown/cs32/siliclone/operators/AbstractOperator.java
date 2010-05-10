package edu.brown.cs32.siliclone.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerDisplay;

@SuppressWarnings("serial")
public abstract class AbstractOperator implements Operator {
	
	private transient VisualizerDisplay visualizerDisplay;
	private transient Window visualizerDisplayWindow;
	private Map properties = new HashMap();
	
	protected int x, y;
	protected Operator[] inputs = new Operator[getNumInputs()];
	protected Collection<SequenceHook> outputSequence = new LinkedList<SequenceHook>();
	protected Collection<Operator> children = new ArrayList<Operator>();
	
	protected transient OpView view;
	
	public void setProgress(int percent){
		if(view != null){
			view.setProgress(percent);
		}
	}
	public abstract void init();
	
	public void setOpView(OpView view){
		this.view = view;
		
		visualizerDisplay = new VisualizerDisplay(view.getWorkspaceView(), this);	
	}
	
	public Map getProperties(){
		return properties;
	}
	
	
	public OpView getOpView(){
		return view;
	}
	
	public void setProperties(Map properties)
	{
		this.properties = properties;
	}
	
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
		if(slotNum > inputs.length || slotNum < 0) {
			return;
		}
		
		input.addChild(this);
		inputs[slotNum] = input;
		
		this.update();
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
			AbstractOperator.this.outputSequence.clear();
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
			
			view.addPeer(visualizerDisplayWindow);
			visualizerDisplayWindow.setKeepInParentRect(true);
			
			visualizerDisplayWindow.addCloseClickHandler(new CloseClickHandler() {

				public void onCloseClick(CloseClientEvent event) {
					visualizerDisplayWindow.hide();
				}
			});

		}
		visualizerDisplayWindow.show();
		
	}
	
	public void updateVisualizations(){
		visualizerDisplay.update();
	}
	
	public void update(){
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i] == null) {
				return;
			}
			
			if (i == inputs.length - 1) {
				//calculate new output sequence
				this.calculate();
			}
		}
	}
	
	public void onCompletion() {
		System.out.println("onCompletion called");
		//update visualizations
		updateVisualizations();
		
		//propagate calculations
		for (Operator op : children) {
			op.update();
		}
	}
}
