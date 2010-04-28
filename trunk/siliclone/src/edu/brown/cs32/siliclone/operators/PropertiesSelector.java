package edu.brown.cs32.siliclone.operators;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class PropertiesSelector extends VLayout {
	
	protected DynamicForm _form;
	protected ButtonItem _okButton;

	public PropertiesSelector() {
		_form = new DynamicForm();
		
		this.setBackgroundColor("#C0C0C0");
		this.setEdgeSize(5);
		this.setEdgeBackgroundColor("#000000");
		this.setOpacity(20);
		_okButton = new ButtonItem("OK");
		_okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (verifyFields()) {
					processInput();
				}
			}
		});
		
		_okButton.setAlign(Alignment.CENTER);
		this.addMember(_form);
	}
	
	protected abstract boolean verifyFields();
	
	protected abstract void processInput();
	
}
