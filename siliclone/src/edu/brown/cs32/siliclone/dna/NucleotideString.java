package edu.brown.cs32.siliclone.dna;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;


/**
 * Represents the actual string of nucleotides that make
 * up a DNA sequence.  This class contains a variety of
 * special methods to permit fast queries that determine
 * whether a particular substring is in the nucleotide
 * sequence.
 * 
 * @author jeldridg
 */
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
	public NucleotideString(NucleotideString ns){
		this.sequence = ns.sequence;
		this.searchTreeRoot = new SearchTreeNode(ns.searchTreeRoot);
		this.hash = ns.hash;
		this.indexDepth=ns.indexDepth;
		
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
	
	public byte[] getSequenceBytes(){
		return sequence;
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
	
	private byte[] parseSearchNucleotides(SimpleNucleotide[] searchNucleotideArray, int startPos){
		byte[] r = new byte[searchNucleotideArray.length-startPos];
		for (int i = startPos; i<searchNucleotideArray.length;i++){
			r[i-startPos]=(byte) searchNucleotideArray[i].ordinal();
		}
	return r;
	}
	
	
	public Collection<Integer> getPositions(SimpleNucleotide[] searchString) {
		if(indexDepth<0) throw new IllegalArgumentException("cannot index with negative depth");
		if(indexDepth==0){
			LinkedList<Integer> r = new LinkedList<Integer>();
outerloop:	for (int i = 0; i<=sequence.length-searchString.length;i++){
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
			 if(i+searchString.length-indexDepth<sequence.length-1){
			 for(int j = indexDepth;j<searchString.length;j++){
			 if(searchString[j]!=getSimpleNucleotideAt(i+j)){
				 continue outerloop;
			 }}
			 r.add(i);
			 }
		 }
		 return r;
		 
		 
	}
	
	public void makeIndex(int indexDepth){
		
		this.indexDepth=indexDepth;
		this.searchTreeRoot = new SearchTreeNode();
		
		if(indexDepth>0)
		for(int i = 0;i<sequence.length;i++){
			SearchTreeNode currentNode = searchTreeRoot;
			for(int l = 0;l<indexDepth;l++){
				if(l+i>=sequence.length){
					break;
				}
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
	
	private static final String[] debugRepresentation= {"a","T","C","G","n4mC","5mC","6mA"};
	
	public SimpleNucleotide getSimpleNucleotideAt(int position){
		switch(getComplexNucleotideAt(position)){
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
	}
	
	public ComplexNucleotide getComplexNucleotideAt(int position){
		return ComplexNucleotide.values()[sequence[position]];
	}
	
	public enum ComplexNucleotide{
		adenine,thymine,cytosine,guanine,N4methylcytosine,C5methylcytosine,C6methyladenine;
	}
	
	
	
}
