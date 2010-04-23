package edu.brown.cs32.siliclone.operators.anothertestop;


import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import edu.brown.cs32.siliclone.operators.PropertiesSelector;

public class Properties extends PropertiesSelector {
	
	Label _opWidget;
	TextItem _ti;
	DynamicForm _form = new DynamicForm();  
	Date _latestUpdate;
	
	public Properties(Label w){
	
	_opWidget = w;

	_form.setWidth(100);  
		
	_ti = new TextItem("inputsomething");
	_ti.addChangedHandler(new HandlerImpl());
	
	_form.setFields(_ti);
	this.addChild(_form);
	_latestUpdate = new Date();
	}
	
	
	private class HandlerImpl implements ChangedHandler{
		private final SquareServiceAsync squareService = GWT
		.create(SquareService.class);

		public void onChanged(ChangedEvent event) {
			
			AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
				public Date modifyDate = new Date();  //a public field in an anonymous class is probably not so bad, is it?
				
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					SC.say("Error"+caught.getMessage());
				}

				public void onSuccess(Integer result) {
					if(_latestUpdate.before(modifyDate)){
					_latestUpdate=modifyDate;
					_opWidget.setContents("result: "+result+ ":"+modifyDate);
					}
				}
			};
			
			squareService.square(Integer.parseInt((String)_form.getValue("inputsomething")),callback
					);
		}
		
		
		
		
	}

}
