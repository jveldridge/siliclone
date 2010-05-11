package edu.brown.cs32.siliclone.utilities.ezyme;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.NucleotideString.SimpleNucleotide;

abstract public class PCREnzyme {
	
	static private PCREnzymeTaq taq;
	static private PCREnzymePfu pfu;
	
	private PCREnzyme() {}
	
	static public PCREnzyme getEnzyme(String enzyme) {
		System.out.println(enzyme);
		if(enzyme.equals("Taq"))
		{
			if(taq == null)
				taq = new PCREnzymeTaq();
			return taq;
		}
		else if(enzyme.equals("Pfu"))
		{
			if(pfu == null)
				pfu = new PCREnzymePfu();
			return pfu;
		}
		else {
			System.out.println("mismatched enzyme name");
			if(taq == null)
				taq = new PCREnzymeTaq();
			return taq;
		}
			 
	}
	
	
	abstract public String getOverhangSequence();
	abstract public int getOverhang();
	
	private static class PCREnzymeTaq extends PCREnzyme{
		
		@Override
		public String getOverhangSequence() {
			return "A";
		}

		@Override
		public int getOverhang() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
	
	private static class PCREnzymePfu extends PCREnzyme {

		@Override
		public String getOverhangSequence() {
			return "";
		}

		@Override
		public int getOverhang() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

}
