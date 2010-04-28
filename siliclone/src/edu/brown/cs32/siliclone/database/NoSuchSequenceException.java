package edu.brown.cs32.siliclone.database;

import edu.brown.cs32.siliclone.dna.SequenceHook;

/**
 * Thrown when an invalid SequenceHook--that is, a SequenceHook
 * containing a sequence data ID or sequence string ID that does
 * not reference a valid entry in the appropriate database table--
 * is passed to an RPC method that contacts the database to fetch
 * sequence information.
 * 
 * @author jeldridg
 */
public class NoSuchSequenceException extends Exception {

	private SequenceHook _hook;
	
	public NoSuchSequenceException(SequenceHook invalidSeq) {
		_hook = invalidSeq;
	}
	
	public String getMessage() {
		return "The SequenceHook " + _hook + " does not reference a valid DNA sequence in the database";
	}
}
