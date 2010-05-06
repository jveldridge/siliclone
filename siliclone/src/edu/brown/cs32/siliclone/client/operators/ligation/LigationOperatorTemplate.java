package edu.brown.cs32.siliclone.client.operators.ligation;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorTemplate;
import edu.brown.cs32.siliclone.operators.Operator;

public class LigationOperatorTemplate extends AbstractRemoteOperatorTemplate {

	public String getName() {
		// TODO Auto-generated method stub
		return "Ligation";
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public Operator makeOperator() {
		// TODO Auto-generated method stub
		return new LigationOperator();
	}

}
