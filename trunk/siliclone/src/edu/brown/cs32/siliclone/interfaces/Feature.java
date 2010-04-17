package edu.brown.cs32.siliclone.interfaces;

import java.io.Serializable;

/**
 * Specifies the required methods of a Feature of a DNASequence.
 * 
 * @author jeldridg
 */
public interface Feature extends Serializable{

	public String getName();
	
	public int getStartPosition();
	
	public int getEndPosition();
	
}
