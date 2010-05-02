package edu.brown.cs32.siliclone.dna;

import static org.junit.Assert.*;
import static edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;

public class NucleotideStringSeachingTest {

	@Test
	public void testSearchShort(){
		NucleotideString ns = new NucleotideString("tatcgatcggctcctc");
		System.out.println(ns);
		for(int i =0;i<10;i++){                   //atatctatccta
		ns.makeIndex(i);
		Collection<Integer> poss = ns.getPositions(new NucleotideString.SimpleNucleotide[]{t,c,g}); //note import static
		Collection<Integer> poss2 =new LinkedList<Integer>();
		poss2.add(2);
		poss2.add(6);
		System.out.println(Arrays.toString(poss.toArray()));
		assertTrue(poss.equals(poss2));
		}
	}

}
