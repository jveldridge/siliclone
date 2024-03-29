package edu.brown.cs32.siliclone.client.connectors;

public interface Connectable {
	
	public void startDrag(Direction dir);
	
	public void adjustVertical(int change, Direction cameFrom);
	public void adjustHorizontal(int change, Direction cameFrom);
	public void translate(int horizontal, int vertical, Direction cameFrom);
	
	public void removeConnection(Connectable toRemove);
	//Only adds connection if none exists already in that direction
	public void addConnection(Connectable toAdd, Direction dir);
	//Replaces connection in that direction
	public void changeConnection(Connectable toAdd, Direction dir);
	
}
