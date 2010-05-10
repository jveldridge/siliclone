package edu.brown.cs32.siliclone.client.operators.filter;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;

import edu.brown.cs32.siliclone.operators.Operator;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class FilterOpTemplate implements OperatorTemplate {
	private String name;
	
	public FilterOpTemplate(){
		this.name = "Magic Filter";
	}
	
	public Widget getWidget() {
		return new Label(name);
	}

	public Operator makeOperator() {
		return new FilterOperator();
	}
	
	public String getName(){
		return name;
	}

}
