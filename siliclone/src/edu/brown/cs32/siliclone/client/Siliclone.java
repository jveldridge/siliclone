package edu.brown.cs32.siliclone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;

import edu.brown.cs32.siliclone.client.workspace.BasicWorkspace;

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
		_panel = new Canvas();
		_panel.setHeight100();
		_panel.setWidth100();
		
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
	
	public void makeNewWorkspace() {
		_mainView.addWorkspace(new BasicWorkspace());
	}
}
