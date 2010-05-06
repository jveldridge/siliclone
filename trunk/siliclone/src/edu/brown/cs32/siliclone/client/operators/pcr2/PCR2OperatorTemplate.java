package edu.brown.cs32.siliclone.client.operators.pcr2;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorTemplate;
import edu.brown.cs32.siliclone.operators.Operator;

public class PCR2OperatorTemplate extends AbstractRemoteOperatorTemplate {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PCR2";
	}

	@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Operator makeOperator() {
		// TODO Auto-generated method stub
		return new PCR2Operator();
	}

}
