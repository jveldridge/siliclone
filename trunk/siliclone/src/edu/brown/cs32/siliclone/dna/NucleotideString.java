package edu.brown.cs32.siliclone.dna;

import java.io.Serializable;

/**
 * Represents the actual string of nucleotides that make
 * up a DNA sequence.  This class contains a variety of
 * special methods to permit fast queries that determine
 * whether a particular substring is in the nucleotide
 * sequence.
 * 
 * @author jeldridg
 */
@SuppressWarnings("serial")
public class NucleotideString implements Serializable {
	
	private String seq;
	
	@SuppressWarnings("unused")
	private NucleotideString() {
		//constructor for serialization
	}
	
	public NucleotideString(String seq) {
		this.seq = seq;
	}
	
	public int getLength(){
		return 0;
	}
	
	public String getString() {
		return seq;
	}
}
