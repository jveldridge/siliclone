package edu.brown.cs32.siliclone.operators.dnaInput;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class DNAInputTemplate implements OperatorTemplate {

	private String _name;
	
	public DNAInputTemplate() {
		_name = "DNA Input Selection";
	}
	
	public String getName() {
		return _name;
	}

	public Widget getWidget() {
		return new Label(_name);
	}

	public Operator makeOperator() {
		return new DNAInputOp();
	}

	
}
