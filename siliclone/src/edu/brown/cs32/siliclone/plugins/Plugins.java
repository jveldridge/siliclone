package edu.brown.cs32.siliclone.plugins;

import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.testop.AnotherTestOpFactory;
import edu.brown.cs32.siliclone.operators.testop.TestOpFactory;

public class Plugins {

	public static void defineOperators(OperatorAdder operatorAdder){
		operatorAdder.addOperator(new TestOpFactory());
		operatorAdder.addOperator(new AnotherTestOpFactory());
	}
	
}
