package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;

import edu.brown.cs32.siliclone.client.workspace.BasicWorkspace;
import edu.brown.cs32.siliclone.client.workspace.Workspace;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Siliclone implements EntryPoint {
	
	private LoginScreen _loginScreen;
	private MainView _mainView;
	private Canvas _panel;
	
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
		
		_panel = new Canvas();
		_panel.setHeight100();
		_panel.setWidth100();
		_panel.setOverflow(Overflow.HIDDEN);
		
		_loginScreen = new LoginScreen(this);
		_mainView = new MainView(this);
       
		this.showMainView();
		//this.showLoginScreen();
		_panel.draw();
	}
	
	public void showLoginScreen() {
		_panel.removeChild(_mainView);
		_panel.addChild(_loginScreen);
	}
	
	public void showMainView() {
		_panel.removeChild(_loginScreen);
		_panel.addChild(_mainView);
	}
	
	public void addWorkspace(Workspace w) {
		_mainView.addWorkspace(w);
	}

	public Workspace getCurrentWorkspace() {
		return _mainView.getCurrentWorkspace();
	}
	
	public void changeWorkspaceName(Workspace w, String newName) {
		_mainView.changeWorkspaceName(w, newName);
	}
}
