package edu.brown.cs32.siliclone.server.operators.pcr2;

import edu.brown.cs32.siliclone.client.operators.pcr2.PCROperatorService;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.AbstractRemoteOperatorServiceImpl;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class PCR2OperatorServiceImpl extends AbstractRemoteOperatorServiceImpl
		implements PCROperatorService {

	@Override
	protected OperatorComputer getNewOperatorComputer() {
		// TODO Auto-generated method stub
		return new PCR2OperatorComputer();
	}

}
