package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;

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
		_panel.addChild(_loginScreen);
		_panel.show();
	}
	
	public void login() {
		if (_loginScreen != null) {
			_loginScreen.destroy();
		}
		
		_mainView = new MainView(this);
		_panel.addChild(_mainView);
	}
	
	
	public void logout() {
		if (_mainView != null) {
			_mainView.destroy();
		}
		
		_loginScreen = new LoginScreen(this);
		_panel.addChild(_loginScreen);
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
