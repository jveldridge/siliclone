package edu.brown.cs32.siliclone.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;

public class PCRTask implements Task {

	
	private Collection<SequenceHook> output;
	private Collection<SequenceHook>[] input;
	private Map<String, String> properties;
	
	public PCRTask(Collection<SequenceHook>[] input, Map<String, String> properties) {
		this.input = input;
		this.properties = properties;
	}

	public void compute() {
		output = new LinkedList<SequenceHook>();
		try {
			String s = properties.get("match");
			System.out.println(s);
			Integer matchLength = Integer.parseInt(properties.get("match"));
			Collection<NucleotideString> templates = new LinkedList<NucleotideString>();
			Collection<NucleotideString> forwardPrimers = new LinkedList<NucleotideString>();
			Collection<NucleotideString> reversePrimers = new LinkedList<NucleotideString>();
			Collection<NucleotideString> outputStrings = new LinkedList<NucleotideString>();
			//Input[0] is the templates
			for(SequenceHook sh : input[0])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				templates.add(seq);
			}
			//First try with input[1] as forward primer
			for(SequenceHook sh : input[1])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				//Only look for matches with the last (matchLength) nucleotides of the sequence
				forwardPrimers.add(seq);
			}
			//Input[2] is the reverse primer
			for(SequenceHook sh : input[2])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				seq = seq.reverseComplement();
				reversePrimers.add(seq);
			}
			
			for(NucleotideString template : templates)
			{
				
				for(NucleotideString forward : forwardPrimers)
				{
					int fLength = (matchLength > forward.getLength()) ? forward.getLength() : matchLength;
					SimpleNucleotide[] forwardCheck = forward.getSimpleNucleotides(forward.getLength() - fLength, fLength);
					Collection<Integer> forwardMatches = template.getPositions(forwardCheck, true);
					for(NucleotideString reverse : reversePrimers)
					{
						int rLength = (matchLength > reverse.getLength()) ? reverse.getLength() : matchLength;
						SimpleNucleotide[] reverseCheck = reverse.getSimpleNucleotides(0, rLength);
						Collection<Integer> reverseMatches = template.getPositions(reverseCheck, true);
						
						for(Integer f : forwardMatches)
						{
							for(Integer r : reverseMatches)
							{
								//First get the part of the template between the primers
								Integer primerSeparation = r - (f + fLength);
								if(primerSeparation >= 0)
								{
									NucleotideString product = new NucleotideString(template, f + fLength, primerSeparation);
									//Concatenate with forward primer
									product = new NucleotideString(forward, product);
									//Then concatenate with reverse primer
									product = new NucleotideString(product, reverse);
									outputStrings.add(product);
								}
							}
						}
					}
				}
			}
			
			for(NucleotideString out : outputStrings)
			{
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("Circular", false);
				String name = Integer.toString(out.hashCode() + new Random().nextInt(100000));
				Map<String, Collection<Feature>> features = new HashMap<String, Collection<Feature>>();
				SequenceServiceImpl.saveSequence(out, features, name, properties);
				output.add(SequenceServiceImpl.findDNASequence(name));
			}
			
			
			
		} catch (DataServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	
	public Collection<SequenceHook> getOutput() {
		return output;
		
	}

}
