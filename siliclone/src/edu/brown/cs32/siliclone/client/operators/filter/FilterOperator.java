package edu.brown.cs32.siliclone.client.operators.filter;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Img;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.AbstractOperator;
import edu.brown.cs32.siliclone.operators.PropertiesSelector;

@SuppressWarnings("serial")
public class FilterOperator extends AbstractOperator {

	transient private Widget image;
	transient private FilterOpPropertiesSelector properties;
	
	@Override
	public void init() {
		this.image = new Img("filter.gif", 113, 94);
		this.properties = new FilterOpPropertiesSelector(this);
		properties.processInput();
	}
	
	public void calculate() {
		properties.processInput();
		properties.show();
	}

	public int getNumInputs() {
		return 1;
	}

	public PropertiesSelector getPropertiesSelector() {
		return properties;
	}

	public Widget getWidget() {
		return image;
	}
	
	public void addOuputSeq(SequenceHook toAdd) {
		System.out.println("before: " + outputSequence);
		outputSequence.add(toAdd);
		System.out.println("after: " + outputSequence);
	}

}
