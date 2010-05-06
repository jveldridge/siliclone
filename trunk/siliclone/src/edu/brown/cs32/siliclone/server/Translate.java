package edu.brown.cs32.siliclone.server;

import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.*;
 
public class Translate {
 
  public static void main(String[] args) {
    try {
      //create a DNA SymbolList
      SymbolList symL = DNATools.createDNA("tggccattgaatgag");
 
      //transcribe to RNA (after biojava 1.4 use this method instead)
      symL = DNATools.toRNA(symL);
 
      //translate to protein
      symL = RNATools.translate(symL);
 
      //prove that it worked
           System.out.println(symL.seqString());
     }catch (IllegalAlphabetException ex) {
 
 
      /* 
       * this will occur if you try and transcribe a non DNA sequence or translate
       * a sequence that isn't a triplet view on a RNA sequence.
       */
       ex.printStackTrace();
     }catch (IllegalSymbolException ex) {
      // this will happen if non IUB characters are used to create the DNA SymbolList
       ex.printStackTrace();
     }
   }
}