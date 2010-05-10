package edu.brown.cs32.siliclone.server.operators.ligation;

import java.util.Collection;
import java.util.Collections;
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
import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;
import edu.brown.cs32.siliclone.tasks.Task;

public class LigationTask implements Task {

	private Collection<SequenceHook> output;
	private Collection<SequenceHook>[] input;
	private Map properties;
	
	
	public LigationTask(Collection<SequenceHook>[] input, Map properties) {

		this.input = input;
		this.properties = properties;
		output = Collections.synchronizedList(new LinkedList<SequenceHook>());
	}

	public void compute() {
		try{
			Collection<Thread> saveThreads = new LinkedList<Thread>();
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
					if(rightOne == null) {
						one.properties.put("rightOverhang", 0);
						rightOne = 0;
					}
					Integer leftOne = (Integer)one.properties.get("leftOverhang");
					if(leftOne == null) {
						one.properties.put("leftOverhang", 0);
						leftOne = 0;
					}
					for(Sequence two : second)
					{	
						Boolean circleTwo = (Boolean)two.properties.get("isCircular");
						System.out.println(circleTwo);
						if(circleTwo == null || circleTwo == false) {
							Integer rightTwo = (Integer)two.properties.get("rightOverhang");
							if(rightTwo == null) {
								two.properties.put("rightOverhang", 0);
								rightTwo = 0;
							}
							Integer leftTwo = (Integer)two.properties.get("leftOverhang");
							if(leftTwo == null) {
								two.properties.put("leftOverhang", 0);
								leftTwo = 0;
							}
							final Sequence forward = new Sequence(null, new HashMap<String, Object>());
							final Sequence reverse = new Sequence(null, new HashMap<String, Object>());
							
							//First check if the right side of sequence one ligates to anything:							
							if(leftTwo == rightOne) {
								boolean overlap = true;
								for(int i = 0; i < Math.abs(rightOne); i++)
								{
									if(one.str.getSimpleNucleotideAt(one.str.getLength() - Math.abs(rightOne) + i) != two.str.getSimpleNucleotideAt(i).opposite())
										overlap = false;
								}
								if(overlap == true) {
									forward.str = new NucleotideString(one.str, two.str, one.str.getLength() - Math.abs(rightOne));
									forward.properties.put("isCircular", false);
									forward.properties.put("rightOverhang", rightTwo);
									forward.properties.put("leftOverhang", leftOne);
								}
							}
							if(rightTwo == rightOne) {
								boolean overlap = true;
								NucleotideString rc = two.str.reverseComplement();
								for(int i = 0; i < Math.abs(rightOne); i++) {
									if(one.str.getSimpleNucleotideAt(one.str.getLength() - Math.abs(rightOne) + i) != rc.getSimpleNucleotideAt(i).opposite())
										overlap = false;
								}
								if(overlap == true) {
									reverse.str = new NucleotideString(one.str, rc, one.str.getLength() - Math.abs(rightOne));
									reverse.properties.put("isCircular", false);
									reverse.properties.put("rightOverhang", leftTwo);
									reverse.properties.put("leftOverhang", leftOne);
								}
							}
							
							//Then if the left side of sequence one ligates

							if(leftOne == rightTwo) {
								boolean overlap = true;
								for(int i = 0; i < Math.abs(leftOne); i++)
									if(one.str.getSimpleNucleotideAt(i) != two.str.getSimpleNucleotideAt(two.str.getLength() - Math.abs(leftOne) + i))
										overlap = false;
								if(overlap == true)
									if(forward.str == null) {
										forward.str = new NucleotideString(two.str, one.str, two.str.getLength() - Math.abs(leftOne));
										forward.properties.put("isCircular", false);
										forward.properties.put("rightOverhang", rightOne);
										forward.properties.put("leftOverhang", leftTwo);
									}
									//If forward != null, forward was created in the first ligation - now must circularize
									else {
										if(leftOne != 0) forward.str = new NucleotideString(forward.str, 0, forward.str.getLength() - Math.abs(leftOne));
										forward.properties.put("isCircular", true);
										forward.properties.put("rightOverhang", 0);
										forward.properties.put("leftOverhang", 0);
									}
							}
							if(leftOne == leftTwo) {
								boolean overlap = true;
								NucleotideString rc = two.str.reverseComplement();
								for(int i = 0; i < Math.abs(leftOne); i++)
								if(one.str.getSimpleNucleotideAt(i) != rc.getSimpleNucleotideAt(rc.getLength() - Math.abs(leftOne) + i))
									overlap = false;
								if(overlap == true) {
									if(reverse.str == null)
									{
										reverse.str = new NucleotideString(rc, one.str, rc.getLength() - Math.abs(leftOne));
										reverse.properties.put("isCircular", false);
										reverse.properties.put("rightOverhang", rightOne);
										reverse.properties.put("leftOverhang", rightTwo);
									}
									else {
										if(leftOne != 0) reverse.str = new NucleotideString(reverse.str, 0, reverse.str.getLength() - Math.abs(leftOne));
										reverse.properties.put("isCircular", true);
										reverse.properties.put("rightOverhang", 0);
										reverse.properties.put("leftOverhang", 0);										
									}
								}
							}
							
							final Map<String, Collection<Feature>> fMap = new HashMap<String, Collection<Feature>>();
							final Map<String, Collection<Feature>> rMap = new HashMap<String, Collection<Feature>>();
							Random rng = new Random();
							if(forward.str != null) {
								final String fName = Integer.toString(forward.str.hashCode() + rng.nextInt());
								Thread t = new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											output.add(SequenceServiceImpl.saveSequence(forward.str, fMap, fName, forward.properties,false));
										} catch (DataServiceException e) {
											e.printStackTrace();
										}
										
									}
								});
								saveThreads.add(t);
								t.start();

							}
							if(reverse.str != null) {
								final String rName = Integer.toString(reverse.str.hashCode() + rng.nextInt());
								
								saveThreads.add(new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											output.add(SequenceServiceImpl.saveSequence(reverse.str, rMap, rName, reverse.properties,false));
										} catch (DataServiceException e) {
											e.printStackTrace();
										}
										
									}
								}));
								
								
								
							}
						}//end of check that two isn't circular
					}//end of loop through input 2
				}//end of check that one isn't circular
			}//end of loop through input 1
			
			for (Thread thread : saveThreads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}//end of try
		catch (DataServiceException e) {
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
