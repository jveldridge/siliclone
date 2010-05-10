package edu.brown.cs32.siliclone.client.operators.restrictiondigest;

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
		return new DigestOperator();
	}
	
	public String getName(){
		return name;
	}

	public String getIconPath() {
		return "digest.gif";
	}

}
