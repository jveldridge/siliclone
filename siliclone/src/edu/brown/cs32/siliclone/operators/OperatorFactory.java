package edu.brown.cs32.siliclone.operators;

import com.google.gwt.user.client.ui.Widget;


public interface OperatorFactory {
	public Widget getWidget();
	public Operator makeOperator();
	public String getName();
}
