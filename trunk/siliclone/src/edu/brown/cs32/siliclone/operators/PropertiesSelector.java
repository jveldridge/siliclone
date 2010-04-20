package edu.brown.cs32.siliclone.operators;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class PropertiesSelector extends VLayout {

	public PropertiesSelector() {
		this.setBackgroundColor("#C0C0C0");
		this.setOpacity(20);
		this.addChild(new Label("PCR Properties"));
		Button okButton = new Button("OK");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		this.addChild(okButton);
	}
	
}
