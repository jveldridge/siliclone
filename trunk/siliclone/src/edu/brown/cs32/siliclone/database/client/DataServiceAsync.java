package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public interface DataServiceAsync {


	void saveSequence(DNASequence s, String name, AsyncCallback<Boolean> callback);

	void getAvailableSequences(AsyncCallback<List<SequenceHook>> callback);

	
	
	void saveWorkspace(Workspace w, String name, AsyncCallback<Boolean> callback);

	void findWorkspace(String name, AsyncCallback<Workspace> callback);

	void getAvailableWorkspaces(AsyncCallback<List<String>> callback);
}
