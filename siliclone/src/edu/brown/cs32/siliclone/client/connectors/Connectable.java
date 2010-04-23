package edu.brown.cs32.siliclone.client.connectors;

public interface Connectable {

	public void startDrag();
	public void endDrag();
	public void adjustVertical(int change, Direction cameFrom);
	public void adjustHorizontal(int change, Direction cameFrom);
	public void translate(int horizontal, int vertical, Direction cameFrom);
	public void removeConnection(Connectable toRemove);
	
}
