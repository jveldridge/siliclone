package edu.brown.cs32.siliclone.client.operators.abstractremoteoperator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface AbstractRemoteOperatorService extends RemoteService {
	public ComputationHook startComputation(Collection<SequenceHook> input[], Map properties) throws IllegalArgumentException;
	public Integer getProgress(ComputationHook hook) throws BadComputationHookException;
	public Collection<SequenceHook> getResult(ComputationHook hook) throws BadComputationHookException;
	public void cancelComputation(ComputationHook hook) throws BadComputationHookException;
}
