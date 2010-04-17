package edu.brown.cs32.siliclone.database.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.interfaces.DNASequence;
import edu.brown.cs32.siliclone.interfaces.Operator;
import edu.brown.cs32.siliclone.interfaces.Workspace;

public interface DataServiceAsync {

	void saveOperation(String user, Operator o, AsyncCallback<Void> callback);

	void saveSequence(String user, DNASequence s, AsyncCallback<Void> callback);

	void saveWorkspace(String user, Workspace w, AsyncCallback<Void> callback);

}
