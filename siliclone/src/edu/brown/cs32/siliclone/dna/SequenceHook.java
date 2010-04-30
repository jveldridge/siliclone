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

	private int dataID;
	private int seqID;
	private String seqName;
	
	//constructor for serialization
	public SequenceHook() { }
	
	public SequenceHook(int dataID, int seqID, String seqName) {
		this.dataID = dataID;
		this.seqID = seqID;
		this.seqName = seqName;
	}
	
	public int getDataID() {
		return dataID;
	}
	
	public int getSeqID() {
		return seqID;
	}
	
	public String getSeqName(){
		return seqName;
	}
	
}
