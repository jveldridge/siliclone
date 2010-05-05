package edu.brown.cs32.siliclone.server.operators.slowoperator;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.BadComputationHookException;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.ComputationHook;
import edu.brown.cs32.siliclone.client.operators.slowoperator.SlowOperatorService;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.AbstractRemoteOperatorServiceImpl;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;

public class SlowOperatorServiceImpl extends AbstractRemoteOperatorServiceImpl implements SlowOperatorService {

	@Override
	protected OperatorComputer getNewOperatorComputer() {
		
		return new SlowOperatorComputer();
	}


}
