package edu.brown.cs32.siliclone.server.operators.ligation;

import edu.brown.cs32.siliclone.client.operators.ligation.LigationOperatorService;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.AbstractRemoteOperatorServiceImpl;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class LigationOperatorServiceImpl extends AbstractRemoteOperatorServiceImpl
		implements LigationOperatorService {

	@Override
	protected OperatorComputer getNewOperatorComputer() {
		// TODO Auto-generated method stub
		return new LigationOperatorComputer();
	}

}
