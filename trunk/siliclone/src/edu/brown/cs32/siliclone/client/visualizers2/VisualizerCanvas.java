package edu.brown.cs32.siliclone.client.visualizers2;

import com.smartgwt.client.widgets.Canvas;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.operators.Operator;

public abstract class VisualizerCanvas extends Canvas {
	protected WorkspaceView workspace;
	protected Operator owner;
	
	
	public VisualizerCanvas(WorkspaceView workspace, Operator owner){
		this.workspace = workspace;
		this.owner = owner;
		update();
	}
	
	public abstract void update();
	public abstract String getName();
}
