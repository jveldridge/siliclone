package edu.brown.cs32.siliclone.client.operators.abstractremoteoperator;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.anothertestop.SquareService;
import edu.brown.cs32.siliclone.operators.anothertestop.SquareServiceAsync;

public abstract class AbstractRemoteOperator extends edu.brown.cs32.siliclone.operators.AbstractOperator {

	
	transient private static final int CHECK_DELAY=300;
	transient Timer checkProgressTimer = new Timer() {
		
		public void run() {
			updateProgress();
			
		}
	};
	
	protected abstract AbstractRemoteOperatorServiceAsync getNewAsync();
	
	transient private ComputationHook computationHook;
	
	private void propagateCalculations(){
		for (Operator op : children) {
			op.calculate();
		}
	}
	
	private void updateProgress(){
		
		abstractRemoteOperatorServiceAsync.getProgress(computationHook, new AsyncCallback<Integer>() {

			public void onSuccess(Integer result) {
				// TODO set the progress bar
				setProgress(result);
				if(result>=100){
					abstractRemoteOperatorServiceAsync.getResult(computationHook, new AsyncCallback<Collection<SequenceHook>>() {


						public void onFailure(Throwable caught) {
							SC.say(caught.toString());
							computationHook=null;
						}

						public void onSuccess(Collection<SequenceHook> result) {
							outputSequence = result;
							propagateCalculations();
							
						}
					});
				}else{
					checkProgressTimer.schedule(CHECK_DELAY);
				}
				
			}

			public void onFailure(Throwable caught) {
				SC.say(caught.toString());
				
			}
		});
	}
	
	transient final AbstractRemoteOperatorServiceAsync abstractRemoteOperatorServiceAsync = getNewAsync();
	
	public void calculate() {
		

		
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
			inputSequences[i]=inputs[i].getOutputSequence();		
		}
		
		abstractRemoteOperatorServiceAsync.startComputation(inputSequences, null, new AsyncCallback<ComputationHook>() {

			
			public void onFailure(Throwable caught) {
				SC.say(caught.toString());
				computationHook=null;
				
			}

			
			public void onSuccess(ComputationHook result) {
				computationHook=result;
				checkProgressTimer.schedule(CHECK_DELAY);
				
			}
		});
		
	}

}