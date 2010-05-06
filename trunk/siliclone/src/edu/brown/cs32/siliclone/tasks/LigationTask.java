package edu.brown.cs32.siliclone.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.dna.NucleotideString;

public class LigationTask implements Task {

	private Collection<SequenceHook> output;
	private Collection<SequenceHook>[] input;
	private Map properties;
	
	
	public LigationTask(Collection<SequenceHook>[] input, Map properties) {

		this.input = input;
		this.properties = properties;
		output = new LinkedList<SequenceHook>();
	}

	public void compute() {
		try{
			Collection<Sequence> first = new LinkedList<Sequence>();
			Collection<Sequence> second = new LinkedList<Sequence>();
			for(SequenceHook sh : input[0])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				Map<String, Object> props = SequenceServiceImpl.getProperties(sh);
				first.add(new Sequence(seq, props));
			}
			for(SequenceHook sh : input[1])
			{
				NucleotideString seq = SequenceServiceImpl.getSequence(sh);
				Map<String, Object> props = SequenceServiceImpl.getProperties(sh);
				second.add(new Sequence(seq, props));
			}
			
			for(Sequence one : first)
			{
				Boolean circleOne = (Boolean)one.properties.get("isCircular");
				System.out.println(circleOne);
				if(circleOne == null || circleOne == false)
				{
					Integer rightOne = (Integer)one.properties.get("rightOverhang");
					Integer leftOne = (Integer)one.properties.get("leftOverhang");
					for(Sequence two : second)
					{	
						Boolean circleTwo = (Boolean)two.properties.get("isCircular");
						System.out.println(circleTwo);
						if(circleTwo == null || circleTwo == false) {
							Integer rightTwo = (Integer)two.properties.get("rightOverhang");
							Integer leftTwo = (Integer)two.properties.get("leftOverhang");
							Sequence forward = new Sequence(null, new HashMap<String, Object>());
							Sequence reverse = new Sequence(null, new HashMap<String, Object>());
							//First check if the right side of sequence one ligates to anything:
							if(rightOne == null || rightOne == 0)
							{
								if(leftTwo == null || leftTwo == 0) {
									forward.str = new NucleotideString(one.str, two.str);
									forward.properties.put("isCircular", false);
									forward.properties.put("rightOverhang", rightTwo);
									forward.properties.put("leftOverhang", leftOne);
								}
								if(rightTwo == null || rightTwo == 0) {
									reverse.str = new NucleotideString(one.str, two.str.reverseComplement());
									reverse.properties.put("isCircular", false);
									reverse.properties.put("rightOverhang", leftTwo);
									reverse.properties.put("leftOverhang", leftOne);
								}
							}
							
							//Then if the left side of sequence one ligates
							if(leftOne == null || leftOne == 0)
							{
								if(rightTwo == null || rightTwo == 0) {
									if(forward.str == null) {
										forward.str = new NucleotideString(two.str, one.str);
										forward.properties.put("isCircular", false);
										forward.properties.put("rightOverhang", rightOne);
										forward.properties.put("leftOverhang", leftTwo);
									}
									//If forward != null, forward was created in the first ligation - now must circularize
									else {
										forward.properties.put("isCircular", true);
										forward.properties.put("rightOverhang", 0);
										forward.properties.put("leftOverhang", 0);
									}
									if(leftTwo == null || leftTwo == 0) {
										if(reverse.str == null)
										{
											reverse.str = new NucleotideString(two.str.reverseComplement(), one.str);
											reverse.properties.put("isCircular", false);
											reverse.properties.put("rightOverhang", rightOne);
											reverse.properties.put("leftOverhang", rightTwo);
										}
										else {
											reverse.properties.put("isCircular", true);
											reverse.properties.put("rightOverhang", 0);
											reverse.properties.put("leftOverhang", 0);										}
									}
										
								}
							}
							Map<String, Collection<Feature>> fMap = new HashMap<String, Collection<Feature>>();
							Map<String, Collection<Feature>> rMap = new HashMap<String, Collection<Feature>>();
							Random rng = new Random();
							String fName = Integer.toString(forward.str.hashCode() + rng.nextInt());
							String rName = Integer.toString(reverse.str.hashCode() + rng.nextInt());
							if(forward.str != null) {
								SequenceServiceImpl.saveSequence(forward.str, fMap, fName, forward.properties);
								output.add(SequenceServiceImpl.findDNASequence(fName));
							}
							if(reverse.str != null) {
								SequenceServiceImpl.saveSequence(reverse.str, rMap, rName, reverse.properties);
								output.add(SequenceServiceImpl.findDNASequence(rName));
							}
						}
					}
				}
			}
		}
		catch (DataServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}

	}
				
	
	public Collection<SequenceHook> getOutput() {
		return output;
	}
	
	private class Sequence {
		
		public NucleotideString str;
		public Map<String, Object> properties;
		
		public Sequence(NucleotideString seq, Map<String, Object> properties) {
			this.str = seq;
			this.properties = properties;
		}
	}

}
