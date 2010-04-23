package edu.brown.cs32.siliclone.client.connectors;

public enum Direction {
	UP, DOWN, RIGHT, LEFT;
	
	public Direction opposite() {
		switch(this)
		{
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case RIGHT:
			return LEFT;
		case LEFT:
			return RIGHT;
		default:
			return null;
		}
	}
}
