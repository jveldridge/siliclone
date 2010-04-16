package edu.brown.cs32.siliclone.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Siliclone implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

        VLayout mainLayout = new VLayout();  
        mainLayout.setWidth100();  
        mainLayout.setHeight100();  
  
        //top menu
        mainLayout.addMember(new TopMenu());  
  
        HLayout notMenuLayout = new HLayout();  
        notMenuLayout.setHeight("*");  

        notMenuLayout.addMember(new ListingLabel());  
        notMenuLayout.addMember(new Workspace()); 
  
        mainLayout.addMember(notMenuLayout);  
        notMenuLayout.addMember(new DragCreate("one"));
        RootPanel.get().add(mainLayout);
  
       
	}
}
