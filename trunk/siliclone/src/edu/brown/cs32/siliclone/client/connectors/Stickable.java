package edu.brown.cs32.siliclone.client.connectors;

public interface Stickable extends Connectable {
	
	public Connectable getConnection(Direction dir);

}
