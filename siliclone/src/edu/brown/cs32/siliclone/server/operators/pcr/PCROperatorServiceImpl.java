package edu.brown.cs32.siliclone.server.operators.pcr;

import edu.brown.cs32.siliclone.client.operators.pcr.PCROperatorService;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.AbstractRemoteOperatorServiceImpl;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class PCROperatorServiceImpl extends AbstractRemoteOperatorServiceImpl
		implements PCROperatorService {

	@Override
	protected OperatorComputer getNewOperatorComputer() {
		// TODO Auto-generated method stub
		return new PCROperatorComputer();
	}

}
