package edu.brown.cs32.siliclone.client.operators.slowoperator;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;

import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

@SuppressWarnings("serial")
public class SlowOperator extends AbstractRemoteOperator {

	@Override
	protected Class getServiceClass() {
		return SlowOperatorService.class;
	}


	public int getNumInputs() {
		// TODO Auto-generated method stub
		return 1;
	}

	public PropertiesSelector getPropertiesSelector() {
		return new PropertiesSelector() {
			
			@Override
			protected boolean verifyFields() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			protected void processInput() {
				calculate();
				
			}
		};
	}

	public Widget getWidget() {
		Canvas w = new Canvas();
		w.setBackgroundColor("blue");
//		w.addDoubleClickHandler(new DoubleClickHandler() {
//			
//			public void onDoubleClick(DoubleClickEvent event) {
//				calculate();
//				
//			}
//		});
		return w;
	}


	public void init() {
		
	}

}
