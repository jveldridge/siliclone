package edu.brown.cs32.siliclone.database.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.database.client.DataService;
import edu.brown.cs32.siliclone.interfaces.DNASequence;
import edu.brown.cs32.siliclone.interfaces.Operator;
import edu.brown.cs32.siliclone.interfaces.Workspace;

public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	@Override
	public void saveOperation(String user, Operator o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSequence(String user, DNASequence s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveWorkspace(String user, Workspace w) {
		// TODO Auto-generated method stub

	}

}
