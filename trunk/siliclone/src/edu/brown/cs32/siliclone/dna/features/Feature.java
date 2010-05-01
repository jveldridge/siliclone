package edu.brown.cs32.siliclone.dna.features;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Specifies the required methods of a Feature of a DNASequence.
 * 
 * @author jeldridg
 */
public interface Feature extends Serializable {

	/**
	 * Returns the a String representing the type of DNA annotation
	 * that this feature represents.
	 */
	public String getType();
	
	/**
	 * Returns a value associated with the feature.  The specifications
	 * of what this value may be are up to the individual Feature
	 * implementations.
	 */
	public IsSerializable getValue();
	
	/**
	 * Returns the position of the nucleotide at which this feature starts
	 * in the DNASequence with which it is associated.
	 */
	public int getStartPosition();
	
	/**
	 * Returns the position of the nucleotide at which this feature starts
	 * in the DNASequence with which it is associated.
	 */
	public int getEndPosition();
	
}
