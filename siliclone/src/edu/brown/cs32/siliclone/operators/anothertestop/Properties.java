package edu.brown.cs32.siliclone.operators.anothertestop;


import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.BaseWidget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class Properties extends Canvas{
	
	Label _opWidget;
	TextItem _ti;
	DynamicForm _form = new DynamicForm();  
	
	public Properties(Label w){
	
	_opWidget = w;

	_form.setWidth(100);  
		
	_ti = new TextItem("inputsomething");
	_ti.addChangedHandler(new HandlerImpl());
	
	_form.setFields(_ti);
	this.addChild(_form);
	}
	
	private final SquareServiceAsync squareService = GWT
	.create(SquareService.class);
	
	private class HandlerImpl implements ChangedHandler{
		@Override
		public void onChanged(ChangedEvent event) {
			squareService.square(Integer.parseInt((String)_form.getValue("inputsomething")), 
					new AsyncCallback<Integer>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							SC.say("Error"+caught.getMessage());
						}

						public void onSuccess(Integer result) {
							_opWidget.setContents("result: "+result);
						}
					});
		}
		
		
		
		
	}

}
