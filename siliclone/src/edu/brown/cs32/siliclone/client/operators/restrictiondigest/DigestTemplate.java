package edu.brown.cs32.siliclone.client.operators.restrictiondigest;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class DigestTemplate implements OperatorTemplate {
	private String name;
	
	public DigestTemplate(){
		this.name = "Restriction Digest";
	}
	
	public Widget getWidget() {
		return new Label(name);
	}

	public Operator makeOperator() {
		DigestOperator r = new DigestOperator();
		Map m = new HashMap();
		m.put("enzyme", "SmaI");
		r.setProperties(m);
		return r;
	}
	
	public String getName(){
		return name;
	}

	public String getIconPath() {
		return "digest.gif";
	}

}
