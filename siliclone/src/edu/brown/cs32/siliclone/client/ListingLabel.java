package edu.brown.cs32.siliclone.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;

public class ListingLabel extends Label {
	public ListingLabel(){
        setContents("Tools");
        setAlign(Alignment.CENTER);
        setOverflow(Overflow.HIDDEN);
        setWidth("200px");
        setShowResizeBar(true);
	}
}
