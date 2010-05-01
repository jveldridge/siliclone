package edu.brown.cs32.siliclone.plugins;

import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.anothertestop.AnotherTestOpFactory;
import edu.brown.cs32.siliclone.operators.dnaInput.DNAInputTemplate;
import edu.brown.cs32.siliclone.operators.pcr.PCRTemplate;

public class Plugins {

	public static void defineOperators(OperatorAdder operatorAdder){
		operatorAdder.addOperator(new AnotherTestOpFactory());
		operatorAdder.addOperator(new PCRTemplate());
		operatorAdder.addOperator(new DNAInputTemplate());
	}
	
}
