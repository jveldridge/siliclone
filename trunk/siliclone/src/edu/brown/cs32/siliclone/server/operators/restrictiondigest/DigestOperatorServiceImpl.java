package edu.brown.cs32.siliclone.server.operators.restrictiondigest;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.BadComputationHookException;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.ComputationHook;
import edu.brown.cs32.siliclone.client.operators.restrictiondigest.DigestOperatorService;
import edu.brown.cs32.siliclone.client.operators.slowoperator.SlowOperatorService;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.AbstractRemoteOperatorServiceImpl;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class DigestOperatorServiceImpl extends AbstractRemoteOperatorServiceImpl implements DigestOperatorService {

	@Override
	protected OperatorComputer getNewOperatorComputer() {
		
		return new DigestOperatorComputer();
	}


}
