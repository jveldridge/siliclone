package edu.brown.cs32.siliclone.database.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataService;
import edu.brown.cs32.siliclone.dna.DNASequence;
import edu.brown.cs32.siliclone.dna.SequenceHook;

public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	public Workspace findWorkspace(User u, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAvailableSequences(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SequenceHook> getAvailableWorkspaces(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean saveSequence(User user, DNASequence s) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean saveWorkspace(User user, Workspace w) {
		// TODO Auto-generated method stub
		return false;
	}

}
