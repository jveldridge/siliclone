package edu.brown.cs32.siliclone.client.operators.abstractremoteoperator;

import java.util.Collection;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;

@SuppressWarnings("serial")
public abstract class AbstractRemoteOperator extends edu.brown.cs32.siliclone.operators.AbstractOperator {

	transient private static final int CHECK_DELAY=300;
	transient Timer checkProgressTimer;
	
	protected abstract AbstractRemoteOperatorServiceAsync getServiceAsyncObject();
	
	transient private ComputationHook computationHook;
	
	private void startTimer(){
		if(checkProgressTimer==null){
			checkProgressTimer=new Timer() {
				
				public void run() {
					updateProgress();
					
				}
			};
		}
		checkProgressTimer.schedule(CHECK_DELAY);
	}
	
	private void updateProgress(){
		
		abstractRemoteOperatorServiceAsync.getProgress(computationHook, new AsyncCallback<Integer>() {

			public void onSuccess(Integer result) {
				setProgress(result);
				if(result>=100){
					abstractRemoteOperatorServiceAsync.getResult(computationHook, new AsyncCallback<Collection<SequenceHook>>() {


						public void onFailure(Throwable caught) {
							SC.say(caught.toString());
							computationHook=null;
						}

						public void onSuccess(Collection<SequenceHook> result) {
							outputSequence = result;
							onCompletion();							
						}
					});
				}else{
					startTimer();
				}
				
			}

			public void onFailure(Throwable caught) {
				SC.say(caught.toString());
				
			}
		});
	}
	
	private transient AbstractRemoteOperatorServiceAsync abstractRemoteOperatorServiceAsync;
	
	public void calculate() {
		System.out.println("hi debug");
		abstractRemoteOperatorServiceAsync = getServiceAsyncObject();

		
		if(computationHook!=null){
			abstractRemoteOperatorServiceAsync.cancelComputation(computationHook, new AsyncCallback<Void>() {

						public void onSuccess(Void result) {
							//do nothing
							
						}

						public void onFailure(Throwable caught) {
							SC.say(caught.toString());
							
						}
					});
			computationHook=null;
		}
					
				
				
		Collection<SequenceHook>[] inputSequences = new Collection[inputs.length];
		for (int i = 0 ;i<inputs.length;i++){
			if(inputs[i]==null){
			inputSequences[i]=new LinkedList<SequenceHook>();
			}else{
			inputSequences[i]=inputs[i].getOutputSequence();		
		}
		}
		
		abstractRemoteOperatorServiceAsync.startComputation(inputSequences, getProperties(), new AsyncCallback<ComputationHook>() {

			
			public void onFailure(Throwable caught) {
				SC.say(caught.toString());
				computationHook=null;
				
			}

			
			public void onSuccess(ComputationHook result) {
				computationHook=result;
				startTimer();
			}
		});
		
	}

}