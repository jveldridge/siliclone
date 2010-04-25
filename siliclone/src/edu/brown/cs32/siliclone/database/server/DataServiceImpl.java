package edu.brown.cs32.siliclone.database.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.client.workspace.Workspace;
import edu.brown.cs32.siliclone.database.client.DataService;
import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.dna.DNASequence;

public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	public DNASequence findSequence(User u, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Workspace findWorkspace(User u, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAvailableSequences(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAvailableWorkspaces(User u) {
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
