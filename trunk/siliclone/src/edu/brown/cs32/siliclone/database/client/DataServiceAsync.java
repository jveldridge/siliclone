package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public interface DataServiceAsync {

	void findWorkspace(User u, String id, AsyncCallback<Workspace> callback);

	void getAvailableSequences(User u, AsyncCallback<List<String>> callback);

	void getAvailableWorkspaces(User u, AsyncCallback<List<SequenceHook>> callback);

	void saveSequence(User user, DNASequence s, AsyncCallback<Boolean> callback);

	void saveWorkspace(User user, Workspace w, AsyncCallback<Boolean> callback);


}
