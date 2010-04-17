package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.operators.Operator;

public interface DataService extends RemoteService {
	void saveWorkspace(String user, Workspace w);
	void saveSequence(String user, DNASequence s);
	void saveOperation(String user, Operator o);
	
}
