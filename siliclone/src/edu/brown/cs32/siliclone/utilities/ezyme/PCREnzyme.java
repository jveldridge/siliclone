package edu.brown.cs32.siliclone.utilities.ezyme;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;

abstract public class PCREnzyme {
	
	static private PCREnzymeTaq taq;
	static private PCREnzymePfu pfu;
	
	private PCREnzyme() {}
	
	static public PCREnzyme getEnzyme(String enzyme) {
		if(enzyme == "Taq")
		{
			if(taq == null)
				taq = new PCREnzymeTaq();
			return taq;
		}
		else if(enzyme == "Pfu")
		{
			if(pfu == null)
				pfu = new PCREnzymePfu();
			return pfu;
		}
		else
			return null; 
	}
	
	
	abstract public SimpleNucleotide[] getOverhang(); 
	
	private static class PCREnzymeTaq extends PCREnzyme{
		
		@Override
		public SimpleNucleotide[] getOverhang() {
			SimpleNucleotide[] overhang = {SimpleNucleotide.a};
			return overhang;
		}
	}
	
	private static class PCREnzymePfu extends PCREnzyme {

		@Override
		public SimpleNucleotide[] getOverhang() {
			return null;
		}
	}

}
