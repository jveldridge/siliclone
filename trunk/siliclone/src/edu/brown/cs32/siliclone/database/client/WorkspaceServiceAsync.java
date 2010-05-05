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

	void getOwnedWorkspaces(AsyncCallback<List<String>> callback);

	void getPermittedGroups(String workspace,
			AsyncCallback<List<String>> callback);

	void permitUser(String workspace, String user, AsyncCallback<Void> callback);

	void getPermittedUsers(String workspace, AsyncCallback<List<User>> callback);

	void permitGroup(String workspace, String group,
			AsyncCallback<Void> callback);

	void disallowGroup(String workspace, String group,
			AsyncCallback<Void> callback);

	void disallowUser(String workspace, String user,
			AsyncCallback<Void> callback);

}
