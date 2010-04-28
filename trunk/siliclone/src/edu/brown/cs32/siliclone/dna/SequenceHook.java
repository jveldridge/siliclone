package edu.brown.cs32.siliclone.dna;

import java.io.Serializable;

/**
 * Class that provides a hook into a DNASequence stored in 
 * the server's database.  The class just wraps around an
 * integer that represents the DNASequence's database ID.
 * 
 * @author jeldridg
 */
public class SequenceHook implements Serializable {

	private int _hook;

	//constructor for serialization
	public SequenceHook() { }
	
	public SequenceHook(int hook) {
		_hook = hook;
	}
	
	public int getHook() {
		return _hook;
	}
	
}
