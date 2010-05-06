package edu.brown.cs32.siliclone.dna;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


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
public class NucleotideString implements Serializable{

	private int hash=0;
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	private int indexDepth=0;
	
	public int getIndexDepth(){
		return indexDepth;
	}
	
	private byte[] sequence;
	
	public int getLength(){
		return sequence.length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NucleotideString){
			NucleotideString ns2 = (NucleotideString) obj;
			if(hash == ns2.hash){
				return Arrays.equals(sequence,ns2.sequence);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Constructor for deserialization
	 */
	public NucleotideString(){
		//do nothing
	}
	
	
	/**
	 * copy constructor
	 * @param ns
	 */
	/*
	public NucleotideString(NucleotideString ns){
		this.sequence = ns.sequence;
		this.searchTreeRoot = ns.searchTreeRoot;
		this.hash = ns.hash;
		this.indexDepth=ns.indexDepth;
		
	}*/
	
	/**
	 * makes the nucleotideString that is the composite of the two given, the overlap is the offset of
	 * ns2 over ns1. Ns2 shields ns1 (ie where they overlap, ns2 is the final product).
	 */
	public NucleotideString(NucleotideString ns1,NucleotideString ns2,int overlap){
		if(overlap>ns1.getLength()||0-overlap>ns2.getLength()) throw new IllegalArgumentException("Overlap larger than sequence");
		
		this.sequence = new byte[Math.max(ns2.getLength()+overlap,ns1.getLength())-Math.min(overlap,0)];
		System.arraycopy(ns1.sequence, 0, this.sequence, Math.max(0, 0-overlap), ns1.getLength());
		System.arraycopy(ns2.sequence, 0, this.sequence, Math.max(0, overlap), ns2.getLength());
		System.out.println(sequence.length);
		hash= Arrays.hashCode(sequence);
		
	}
	
	/**
	 * the same as NucleotideString(NucleotideString,NucleotideString,int),
	 * but with int defaulted to the length of the first nucleotidestring
	 * @param ns1 string on the left
	 * @param ns2 string on the right
	 */
	public NucleotideString(NucleotideString ns1, NucleotideString ns2){
		this(ns1,ns2,ns1.getLength());
	}
	
	/**
	 * makes a substring of this nucleotideString;
	 * @param ns
	 * @param left
	 * @param length
	 */
	public NucleotideString(NucleotideString ns,int left,int length){
		if(length<=0||length>ns.getLength()){
			throw new IllegalArgumentException("Meaningless length");
		}
		this.sequence = new byte[length];
		for(int i =0;i<length;i++){
			this.sequence[i]=ns.sequence[(i+left)%ns.getLength()];
		}
		hash= Arrays.hashCode(sequence);
	}
	
	/**
	 * Like NucleotideString(NucleotideString,int,int), but with the length being the NucleotideString's length
	 * Essentially, does a circular permutation of the nucleotidesequence passed
	 * @param ns
	 * @param left
	 */
	public NucleotideString(NucleotideString ns, int left){
		left = left % ns.getLength();
		this.sequence = new byte[ns.getLength()];
		System.arraycopy(ns.sequence, left, sequence, 0, ns.getLength()-left);
		System.arraycopy(ns.sequence, 0, sequence, ns.getLength()-left, ns.getLength());
		hash= Arrays.hashCode(sequence);
		
	}
	
	
	public NucleotideString(String input) {

		sequence = new byte[input.length()];
		for (int i = 0; i<input.length();i++){
		switch(input.charAt(i)){
		case 'a':
		case 'A':
			sequence[i]=(byte) ComplexNucleotide.adenine.ordinal();
			break;
		case 't':
		case 'T':
			sequence[i]=(byte) ComplexNucleotide.thymine.ordinal();
			break;
		case 'c':
		case 'C':
			sequence[i]=(byte) ComplexNucleotide.cytosine.ordinal();
			break;
		case 'g':
		case 'G':
			sequence[i]=(byte) ComplexNucleotide.guanine.ordinal();
			break;
		}
		}
		hash= Arrays.hashCode(sequence);
	}
	
	
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(byte b:sequence){
			sb.append(debugRepresentation[b]);
		}
		return sb.toString();
		
	}
	
	private class SearchTreeNode implements Serializable{
		
		public SearchTreeNode() {
			//for serialization
		}
		
		public SearchTreeNode(SearchTreeNode stn){
			positions = new LinkedList<Integer>(stn.positions);
			nextcharacter = new EnumMap<SimpleNucleotide, SearchTreeNode>(stn.nextcharacter);
		}
		
		private Collection<Integer> getAllPositions(){
			if(positions!=null){
				return positions;
			}
			LinkedList<Integer> r = new LinkedList<Integer>();
			for (SearchTreeNode stn : nextcharacter.values()) {
				r.addAll(stn.getAllPositions());
			}
			return r;
			
			
		}
		
		private LinkedList<Integer> positions = null;
		private EnumMap<SimpleNucleotide, SearchTreeNode> nextcharacter = new EnumMap<SimpleNucleotide, SearchTreeNode>(SimpleNucleotide.class);
	}
	
	private SearchTreeNode searchTreeRoot;
	
	
	/**
	 * 
	 * @param left left index
	 * @param length length of substring
	 * @return an array of simplenucleotides
	 */
	public SimpleNucleotide[] getSimpleNucleotides(int left, int length){
		if(length<0) throw new IllegalArgumentException("negative length is meaningless");
		SimpleNucleotide[] r =new SimpleNucleotide[length];
		for (int i = 0; i < length; i++) {
			r[i] = getSimpleNucleotideAt(left+i);
		}
		return r;
	}
	
	public String getDisplayString(){
		StringBuilder sb = new StringBuilder();
		for(byte b:sequence){
			sb.append(displayRepresentation[b]);
		}
		return sb.toString();
	}
	
	public NucleotideString reverseComplement(){
		
			NucleotideString rc = new NucleotideString();
			rc.sequence=new byte[getLength()];
			for (int i =0;i<getLength();i++){
				switch (getSimpleNucleotideAt(i)) {
				case a:
					rc.sequence[getLength()-i-1]=(byte) ComplexNucleotide.thymine.ordinal();
					break;
				case t:
					rc.sequence[getLength()-i-1]=(byte) ComplexNucleotide.adenine.ordinal();
					break;
				case c:
					rc.sequence[getLength()-i-1]=(byte) ComplexNucleotide.guanine.ordinal();
					break;
				case g:
					rc.sequence[getLength()-i-1]=(byte) ComplexNucleotide.cytosine.ordinal();
					break;
				}
				
				
			}
			hash= Arrays.hashCode(sequence);
			return rc;
		
	}
	
	/**
	 * Just like calling getSimpleNucleotides(0, getLength());
	 */
	public SimpleNucleotide[] getSimpleNucleotides(){
		return getSimpleNucleotides(0, getLength());
	}
	
	public Collection<Integer> getPositions(SimpleNucleotide[] searchString,boolean circular) {
		if(getLength()==0||searchString.length==0)return new LinkedList<Integer>();
		if(!circular){
			Collection<Integer> positions = getPositions(searchString,true);
			Collection<Integer> r = new LinkedList<Integer>();
			for (Integer integer : positions) {
				if((integer+searchString.length>getLength())){
					break;
				}
				r.add(integer);
			}
			return r;
		}
		
		if(indexDepth==0){
			LinkedList<Integer> r = new LinkedList<Integer>();
outerloop:	for (int i = 0; i<sequence.length;i++){
				 for(int j = 0;j<searchString.length;j++){
					 if(searchString[j]!=getSimpleNucleotideAt(i+j)){
						 continue outerloop; //(in other words, skip r.add(i)
					 }
					 
			}
				 r.add(i);	
			}
			return r;
			
		}
		
		 SearchTreeNode currentNode = searchTreeRoot;
		 for(int depth=0;depth<indexDepth;depth++){
			 SimpleNucleotide n = searchString[depth];

			 if( (currentNode  = currentNode.nextcharacter.get(n)) == null){
				 return new LinkedList<Integer>();
			 }
			 if(depth==searchString.length-1){
				 return currentNode.getAllPositions();
			 }

		 }
		 LinkedList<Integer> r = new LinkedList<Integer>();
outerloop: for(Integer i:currentNode.positions){
			// if(i+searchString.length-indexDepth<sequence.length-1){
			 for(int j = indexDepth;j<searchString.length;j++){
			 if(searchString[j]!=getSimpleNucleotideAt(i+j)){
				 continue outerloop;
			 }}
			 r.add(i);
			// }
		 }
		 return r;
		 
		 
	}
	
	public void makeIndex(int indexDepth){
		
		this.indexDepth=indexDepth;
		if(indexDepth<0) throw new IllegalArgumentException("cannot index with negative depth");
		this.searchTreeRoot = new SearchTreeNode();
		
		if(indexDepth>0)
		for(int i = 0;i<getLength();i++){
			SearchTreeNode currentNode = searchTreeRoot;
			for(int l = 0;l<indexDepth;l++){
				/*if(l+i>=sequence.length){
					break;
				}*/
				SimpleNucleotide currentNucleotide = getSimpleNucleotideAt(i+l);
				if(!currentNode.nextcharacter.containsKey(currentNucleotide)){
					currentNode.nextcharacter.put(currentNucleotide, new SearchTreeNode());
				}
				currentNode =  currentNode.nextcharacter.get(currentNucleotide);

			}
			if(currentNode.positions==null){
				currentNode.positions = new LinkedList<Integer>();
			}
			currentNode.positions.add(i);
		}
		
	}
	
	private static final String[] debugRepresentation= {"A","T","C","G","n4mC","5mC","6mA"};
	private static final String[] displayRepresentation= {"A","T","C","G","C","C","A"};
	
	/**
	 * 
	 * returns the nucleotide at position
	 * @param position cannot be larger than twice the length of the nucleotidestring.
	 * If longer than the nucleotidestring, it subtracts the length of the nucleotidestring
	 * @return the simplenucleotide at this position: either a,t,c,or g
	 */
	public SimpleNucleotide getSimpleNucleotideAt(int position){
		switch(getComplexNucleotideAt(position % getLength())){
		case adenine:
		case C6methyladenine:
			return SimpleNucleotide.a;
		case thymine:
			return SimpleNucleotide.t;
		case guanine:
			return SimpleNucleotide.g;
		case cytosine:
		case N4methylcytosine:
		case C5methylcytosine:
			return SimpleNucleotide.c;
		default:
			return null;
		}
	}
	
	public enum SimpleNucleotide{
		a,t,c,g;
		
		public SimpleNucleotide opposite() {
			switch(this) {
			case a:
				return t;
			case t:
				return a;
			case c:
				return g;
			case g:
				return c;
			default:
				return null;
			}
			
		}
	}
	
	
	/**
	 * 
	 * returns the nucleotide, including modifications, at position
	 * @param position cannot be larger than twice the length of the nucleotidestring.
	 * If longer than the nucleotidestring, it subtracts the length of the nucleotidestring
	 * @return the ComplexNucleotide at this position
	 */
	public ComplexNucleotide getComplexNucleotideAt(int position){
		return ComplexNucleotide.values()[sequence[position%getLength()]];
	}
	
	public enum ComplexNucleotide{
		adenine,thymine,cytosine,guanine,N4methylcytosine,C5methylcytosine,C6methyladenine;
	}
	
	
	
}
