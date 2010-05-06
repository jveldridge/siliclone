package edu.brown.cs32.siliclone.client.operators.abstractremoteoperator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface AbstractRemoteOperatorServiceAsync {

	void startComputation(Collection<SequenceHook>[] input,
			Map<String, String> properties,
			AsyncCallback<ComputationHook> callback);

	void getResult(ComputationHook hook,
			AsyncCallback<Collection<SequenceHook>> callback);

	void getProgress(ComputationHook hook, AsyncCallback<Integer> callback);

	void cancelComputation(ComputationHook hook, AsyncCallback<Void> callback);

}
