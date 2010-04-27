package edu.brown.cs32.siliclone.operators;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.EdgedCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class PropertiesSelector extends VLayout {
	
	protected DynamicForm _form;

	public PropertiesSelector() {
		_form = new DynamicForm();
		
		this.setBackgroundColor("#C0C0C0");
		this.setEdgeSize(5);
		this.setEdgeBackgroundColor("#000000");
		this.setOpacity(20);
		Button okButton = new Button("OK");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (verifyFields()) {
					processInput();
				}
			}
		});
		
		this.addMember(_form);
		this.addMember(okButton);
	}
	
	protected abstract boolean verifyFields();
	
	protected abstract void processInput();
	
}
