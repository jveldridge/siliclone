package edu.brown.cs32.siliclone.server.operators.pcr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import edu.brown.cs32.siliclone.tasks.Task;
import edu.brown.cs32.siliclone.utilities.ezyme.PCREnzyme;

public class PCRTask implements Task {

	
	private Collection<SequenceHook> output;
	private Collection<SequenceHook>[] input;
	private Map properties;
	
	public PCRTask(Collection<SequenceHook>[] input, Map properties) {
		this.input = input;
		this.properties = properties;
		output = Collections.synchronizedList(new LinkedList<SequenceHook>());
	}

	public void compute() {
		try {
			String enzymeName = (String)properties.get("enzyme");
			System.out.println(enzymeName);
			PCREnzyme enzyme = PCREnzyme.getEnzyme(enzymeName);
			System.out.println(enzyme);
			NucleotideString overhangSeq = new NucleotideString(enzyme.getOverhangSequence());
			int overhang = enzyme.getOverhang();
			Integer matchLength = (Integer) properties.get("match");
			Collection<NucleotideString> templates = new LinkedList<NucleotideString>();
			Collection<NucleotideString> firstPrimers = new LinkedList<NucleotideString>();
			Collection<NucleotideString> secondPrimers = new LinkedList<NucleotideString>();
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
				firstPrimers.add(seq);
			}
			//Input[2] is the reverse primer
			for(SequenceHook sh : input[2])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				secondPrimers.add(seq);
			}
			
			for(NucleotideString template : templates)
			{
				
				for(NucleotideString forward : firstPrimers)
				{
					int fLength = (matchLength > forward.getLength()) ? forward.getLength() : matchLength;
					SimpleNucleotide[] forwardCheck = forward.getSimpleNucleotides(forward.getLength() - fLength, fLength);
					Collection<Integer> forwardMatches = template.getPositions(forwardCheck, true);
					for(NucleotideString reverse : secondPrimers)
					{
						reverse = reverse.reverseComplement();
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
									//Then concatenate with output overhang
									if(overhang != 0)
									{
										product = new NucleotideString(product, overhangSeq);
										product = new NucleotideString(overhangSeq.reverseComplement(), product);
									}
									outputStrings.add(product);
								}
							}
						}
					}
				}
				
				//Also try with the forward and reverse primers switched
				for(NucleotideString forward : secondPrimers)
				{

					int fLength = (matchLength > forward.getLength()) ? forward.getLength() : matchLength;
					SimpleNucleotide[] forwardCheck = forward.getSimpleNucleotides(forward.getLength() - fLength, fLength);
					Collection<Integer> forwardMatches = template.getPositions(forwardCheck, true);
					for(NucleotideString reverse : firstPrimers)
					{
						reverse = reverse.reverseComplement();
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
									//Then concatenate with enzyme overhang
									if(overhang != 0)
									{
										product = new NucleotideString(product, overhangSeq);
										product = new NucleotideString(overhangSeq.reverseComplement(), product);
									}
									outputStrings.add(product);
								}
							}
						}
					}
				}
			}
			
			System.out.println("Inside of PCRTask1, output number:"+outputStrings.size());
			
			Thread[] savingThreads = new Thread[outputStrings.size()];
			{
			int i=0;
			for(final NucleotideString out : outputStrings)
			{
				final Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("isCircular", false);
				properties.put("rightOverhang", overhang);
				properties.put("leftOverhang", overhang);
				final String name = Integer.toString(out.hashCode() + new Random().nextInt(100000));
				final Map<String, Collection<Feature>> features = new HashMap<String, Collection<Feature>>();
				savingThreads[i]=new Thread(new Runnable() {
					
					public void run() {
						try {
							output.add(SequenceServiceImpl.saveSequence(out, features, name, properties,false));
							System.out.println("Save now!");
						} catch (DataServiceException e) {
							e.printStackTrace();
						}
						
					}
				});
				savingThreads[i].start();
				
				i++;
			}
			}
			System.out.println("Inside of PCRTask2, output number:"+savingThreads.length);
			for (int i = 0; i<savingThreads.length;i++){
				try {
					savingThreads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Inside of PCRTask3, output number:"+output.size());
						
			
			
			
		} catch (DataServiceException e) {
			e.printStackTrace();	
		}
	}
	
	public Collection<SequenceHook> getOutput() {
		return output;
		
	}

}
