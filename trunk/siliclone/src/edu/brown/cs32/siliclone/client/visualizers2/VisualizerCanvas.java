package edu.brown.cs32.siliclone.client.visualizers2;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Progressbar;

import edu.brown.cs32.siliclone.client.WorkspaceView;
import edu.brown.cs32.siliclone.operators.Operator;

public abstract class VisualizerCanvas extends Canvas {
	protected WorkspaceView workspace;
	protected Operator owner;
	private Progressbar progress;
	
	
	public VisualizerCanvas(WorkspaceView workspace, Operator owner){
		this.workspace = workspace;
		this.owner = owner;
		update();
		

		progress = new Progressbar();
        progress.setBreadth(15);
        progress.setLength(getWidth() - 20);
        addChild(progress);
        progress.setLeft(5);
        progress.setTop(getHeight() - progress.getHeight());
        progress.hide();
	}
	
	

	public void showProgressBar(){
		progress.show();
	}
	
	public void hideProgressBar(){
		progress.hide();
	}
	public void setProgress(int percent){
		if(percent >= 100){
			hideProgressBar();
		}else{
			showProgressBar();
			progress.setPercentDone(percent);
		}
	}
	
	public abstract void update();
	public abstract String getName();
}
