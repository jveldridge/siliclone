package edu.brown.cs32.siliclone.operators;

import java.util.ArrayList;
import java.util.Collection;

import edu.brown.cs32.siliclone.dna.DNASequence;

public abstract class AbstractOperator implements Operator {

	private static final long serialVersionUID = -6196531418823371503L;
	
	protected int _xPos, _yPos;
	protected ArrayList<Operator> _adjacent = new ArrayList<Operator>();
	protected DNASequence _output;

	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	public Collection<Operator> getAdjacentOperators() {
		return _adjacent;
	}

	public int getX() {
		return _xPos;
	}

	public int getY() {
		return _yPos;
	}

	public void setInput(int slotNum, Operator input) {
		// TODO Auto-generated method stub
		
	}

	public void setX(int x) {
		_xPos = x;
	}

	public void setY(int y) {
		_yPos = y;
	}

	
	
}
