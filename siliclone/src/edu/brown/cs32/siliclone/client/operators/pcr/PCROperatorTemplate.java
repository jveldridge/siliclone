package edu.brown.cs32.siliclone.client.operators.pcr;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorTemplate;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.Operator;

public class PCROperatorTemplate extends AbstractRemoteOperatorTemplate {

	public String getName() {
		return "PCR";
	}

	public Widget getWidget() {
		return null;
	}

	public Operator makeOperator() {
		Operator r = new PCROperator();
		Map properties = new HashMap();
		properties.put("enzyme", "Taq");
		properties.put("match", 15);
		r.setProperties(properties);
		return r;
	}

}
