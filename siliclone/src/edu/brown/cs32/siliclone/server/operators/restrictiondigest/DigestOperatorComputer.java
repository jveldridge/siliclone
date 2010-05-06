package edu.brown.cs32.siliclone.server.operators.restrictiondigest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.client.utilities.enzymes.EnzymeListGenerator;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.server.operators.abstractoperator.OperatorComputer;
import static edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide.*;
import static edu.brown.cs32.siliclone.dna.NucleotideString.*;

public class DigestOperatorComputer implements OperatorComputer {

	
	private int progress;
	private boolean cancelled = false;
	private static final HashMap<String,RestrictionEnzyme> ENZYMES = new HashMap<String, RestrictionEnzyme>() {{
	    put("NdeI",     new RestrictionEnzyme(new SimpleNucleotide[]{c,a,t}, -1));
	    put("XhoI",     new RestrictionEnzyme(new SimpleNucleotide[]{c,t,c}, -2));
	    put("SmaI",     new RestrictionEnzyme(new SimpleNucleotide[]{c,c,c}, 0));
	    put("HindIII",   new RestrictionEnzyme(new SimpleNucleotide[]{a,a,g}, -2));
	    put("PvuII",   new RestrictionEnzyme(new SimpleNucleotide[]{c,a,g}, 0));
	 }};

	
	
	
	public Collection<SequenceHook> computeOutput(
			Collection<SequenceHook>[] input, Map properties) {
		Collection<NucleotideString> r_sh = new LinkedList<NucleotideString>();
		LinkedList<Feature> r_f = new LinkedList<Feature>();
		LinkedList<Map<String, Object>> r_pr = new LinkedList<Map<String, Object>>();
		
		RestrictionEnzyme re = ENZYMES.get((String)properties.get("enzyme"));
		Collection<SequenceHook> r = new LinkedList<SequenceHook>();
		
		int progint = 0;
		for (SequenceHook sequenceHook : input[0]) {
			progress=50/r_sh.size()*progint;
			progint++;
			
			try {
				re.cut(SequenceServiceImpl.getSequence(sequenceHook),null,SequenceServiceImpl.getProperties(sequenceHook),r_sh,r_f,r_pr);
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(cancelled) break;
			Iterator<NucleotideString> nsi = r_sh.iterator();
			Iterator<Map<String,Object>> pri = r_pr.iterator();
			assert(r_sh.size()==r_pr.size());
			progint=0;
			Random random = new Random();
			while (nsi.hasNext()) {
				progress=50+50/r_sh.size()*progint;
				progint++;
				NucleotideString ns = nsi.next();
				Map<String,Object> pr = pri.next();
				try {
					r.add(SequenceServiceImpl.saveSequence(ns, new HashMap<String,Collection<Feature>>(),"cutname"+random.nextInt(10000000),pr));
				} catch (DataServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(cancelled) break;
			
			
		
		}
		
		progress=100;
		
		
		
		
		return r;
	}

	public int getProgress() {
		return progress;
	}


	public void cancel() {
		cancelled = true;
		
	}
	
	private static class RestrictionEnzyme{
		int cutoffset;
		int halfway;
		NucleotideString.SimpleNucleotide[] fullSearchString;
		
		
		public RestrictionEnzyme(NucleotideString.SimpleNucleotide[] halfSearchString, int cutoffset){
			this.cutoffset=cutoffset;
			this.halfway=halfSearchString.length;
			this.fullSearchString= new NucleotideString.SimpleNucleotide[halfway*2];
			for (int i = 0;i<halfway;i++){
				fullSearchString[i]=halfSearchString[i];
				switch(halfSearchString[i]){
				case a:
					fullSearchString[halfway*2-i-1]=NucleotideString.SimpleNucleotide.t;
					break;
				case t:
					fullSearchString[halfway*2-i-1]=NucleotideString.SimpleNucleotide.a;
					break;
				case c:
					fullSearchString[halfway*2-i-1]=NucleotideString.SimpleNucleotide.g;
					break;
				case g:
					fullSearchString[halfway*2-i-1]=NucleotideString.SimpleNucleotide.c;
					break;
				}
			}
			
		}
		
		public void cut(NucleotideString sequence, Collection<Feature> features, Map<String,Object> dnaProperties,
			Collection<NucleotideString> r_ns,	Collection<Feature> r_f, Collection<Map<String, Object>> r_pr){
			
			System.out.println("this is the properties object:" +dnaProperties+"::"+dnaProperties.get("isCircular"));
			

			if(!(dnaProperties.get("isCircular")==null)&&(Boolean) dnaProperties.get("isCircular")){
				Collection<Integer> ints = sequence.getPositions(fullSearchString, true);
				System.out.println("ic:"+ints);
				if(ints.isEmpty()){
				r_ns.add(sequence);
				r_pr.add(dnaProperties);
				}else{
				
				Integer firstElement = ints.iterator().next();
				Iterator<Integer> it = ints.iterator();
				Integer previousPosition = it.next();
				while (it.hasNext()) {
					Integer currentPosition =  it.next();
					r_ns.add(new NucleotideString(sequence,previousPosition-cutoffset+halfway,currentPosition-previousPosition+cutoffset*2));
					previousPosition = currentPosition;
				}
				r_ns.add(new NucleotideString(sequence,previousPosition-cutoffset+halfway,firstElement+sequence.getLength()-previousPosition+cutoffset*2));

				}
				for(int i = 0;i<ints.size();i++){
					Map<String, Object> currentProperties = new HashMap<String,Object>();
					currentProperties.put("isCircular", false);
					currentProperties.put("leftOverhang", cutoffset);
					currentProperties.put("rightOverhang", cutoffset);
					r_pr.add(currentProperties);
				}
				
			}else{
				Collection<Integer> ints = sequence.getPositions(fullSearchString, false);
				System.out.println("nc:"+ints);
				if(ints.isEmpty()){
				r_ns.add(sequence);
				r_pr.add(dnaProperties);
				}else{
				
				Iterator<Integer> it = ints.iterator();
				Integer previousPosition = 0;
				Map<String, Object> previousProperties = new HashMap<String,Object>();
				previousProperties.put("isCircular", false);
				previousProperties.put("leftOverhang", dnaProperties.get("leftOverhang"));
				while (it.hasNext()) {
					Integer currentPosition =  it.next();
					r_ns.add(new NucleotideString(sequence,previousPosition-cutoffset+halfway,currentPosition-previousPosition+cutoffset*2));
					previousProperties.put("rightOverhang", cutoffset);
					Map<String, Object> currentProperties = new HashMap<String,Object>();
					currentProperties.put("isCircular", false);
					currentProperties.put("leftOverhang", cutoffset);
					r_pr.add(currentProperties);
					previousPosition = currentPosition;
				}
				r_ns.add(new NucleotideString(sequence,previousPosition-cutoffset+halfway,sequence.getLength()-previousPosition+cutoffset*2));
				Map<String, Object> currentProperties = new HashMap<String,Object>();
				previousProperties.put("leftOverhang", cutoffset);
				previousProperties.put("rightOverhang", dnaProperties.get("rightOverhang"));
				r_pr.add(previousProperties);
				}
				
			}
			
		}
		
		
		
		
	}

}
