package edu.brown.cs32.siliclone.client.operators.slowoperator;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorTemplate;
import edu.brown.cs32.siliclone.operators.Operator;

public class SlowOperatorTemplate extends AbstractRemoteOperatorTemplate {

	public String getName() {
		return "Slow operator";
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public Operator makeOperator() {
		return new SlowOperator();
	}

}
