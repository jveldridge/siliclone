package edu.brown.cs32.siliclone.database.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface WorkspaceServiceAsync {

	void saveWorkspace(Workspace w, String name, AsyncCallback<Void> callback) throws DataServiceException;

	void findWorkspace(String name, AsyncCallback<Workspace> callback) throws DataServiceException;

	void getAvailableWorkspaces(AsyncCallback<List<String>> callback) throws DataServiceException;

	void overwriteWorkspace(Workspace w, String name,
			AsyncCallback<Void> callback);
}
