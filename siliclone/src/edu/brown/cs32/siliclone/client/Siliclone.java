package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.brown.cs32.siliclone.operators.testop.TestOpFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Siliclone implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		LoginScreen loginScreen = new LoginScreen();
		//MainView theOnlyView = new MainView();
        
        //RootPanel.get().add(mainLayout);
        //I had to use this to make the selection panel work right --Noah
		loginScreen.draw();
  
       
	}
}
