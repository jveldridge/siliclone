package edu.brown.cs32.siliclone.dna;


import java.util.Collection;

/**
 * This class provides static utility methods to perform common
 * operations on DNASequences.
 * 
 * @author jeldridg
 */

public class DNAOperations {

	/**
	 * Constructor is private because class should be fully static
	 */
	private DNAOperations() { }
	
	/**
	 * Cuts the given DNASequence at a particular position.
	 * 
	 * @param toCut
	 * @param pos
	 * @return
	 */
	public static Collection<DNASequence> cutSequence(DNASequence toCut, int pos) {
		return null;
	}
	
	/**
	 * Concatenates two DNASequences.  The second sequence will be appended to
	 * the first.  Neither sequence may be null.
	 * 
	 * @param sequence the sequence to which the toAdd sequence will be concatenated
	 * @param toAdd the sequence which will be added to the sequence 
	 * @return
	 */
	public static DNASequence concatenate(DNASequence sequence, DNASequence toAdd) {
		return null;
	}
	
}
