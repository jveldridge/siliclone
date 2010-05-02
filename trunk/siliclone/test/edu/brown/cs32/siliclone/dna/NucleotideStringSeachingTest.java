package edu.brown.cs32.siliclone.dna;

import static org.junit.Assert.*;
import static edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import sun.instrument.InstrumentationImpl;

import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;

public class NucleotideStringSeachingTest {

	@Test
	public void testSearchShort(){
		
		//NucleotideString ns = new NucleotideString("tatcgatcggctcctc");
		
		
		//load a file
		
		
		File file = new File("/home/tderond/sequence");
		       StringBuffer contents = new StringBuffer();
		       BufferedReader reader = null;
		
		        try
		      {
		           reader = new BufferedReader(new FileReader(file));
		           String text = null;
		
		           // repeat until all lines is read
		            while ((text = reader.readLine()) != null)
		{
		contents.append(text)
		.append(System.getProperty(
		"line.separator"));
		}
		} catch (FileNotFoundException e)
		{
		e.printStackTrace();
		} catch (IOException e)
		{
e.printStackTrace();
        } finally
        {
            try
{
if (reader != null)
{
reader.close();
}
} catch (IOException e)
{
e.printStackTrace();
}
}
		
		
		
		
		
		
		
		
		
		
		
		
		System.out.println("done loading");
		NucleotideString ns = new NucleotideString(contents.toString());
		System.out.println("done making class");
		for(int i =0;i<10;i++){   
			System.out.println("for "+i);
		long start = System.nanoTime();
		ns.makeIndex(i);
		long point = System.nanoTime();
		ns.getPositions(new NucleotideString.SimpleNucleotide[]{t,c,g,a,g,c}); //note import static
		long point2 = System.nanoTime();
		ns.getPositions(new NucleotideString.SimpleNucleotide[]{a,a,a,a,a,a}); //note import static
		System.out.println(i+":Index:"+(point-start)/1000000);
		System.out.println(i+":Search1:"+(point2-point)/1000000);
		System.out.println(i+":Search2:"+(System.nanoTime()-point2)/1000000);
//		try {
//			FileOutputStream bais = new FileOutputStream(new File("/home/tderond/file"+i));
//			new ObjectOutputStream(bais).writeObject(ns);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}
	}

}
