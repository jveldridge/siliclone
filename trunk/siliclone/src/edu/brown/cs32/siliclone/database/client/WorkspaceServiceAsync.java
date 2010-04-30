package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public interface WorkspaceServiceAsync {

	void saveWorkspace(Workspace w, String name, AsyncCallback<Void> callback);

	void findWorkspace(String name, AsyncCallback<Workspace> callback);

	void getAvailableWorkspaces(AsyncCallback<List<String>> callback);
}
