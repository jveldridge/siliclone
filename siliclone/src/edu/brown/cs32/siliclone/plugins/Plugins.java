package edu.brown.cs32.siliclone.plugins;

import edu.brown.cs32.siliclone.client.operators.slowoperator.SlowOperatorTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.DataVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.SequenceVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerAdder;
import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.anothertestop.AnotherTestOpFactory;
import edu.brown.cs32.siliclone.operators.client.dnaInput.DNAInputTemplate;
import edu.brown.cs32.siliclone.operators.pcr.PCRTemplate;
import edu.brown.cs32.siliclone.operators.rd.DigestTemplate;

public class Plugins {

	public static void defineOperators(OperatorAdder operatorAdder){
		operatorAdder.addOperator(new AnotherTestOpFactory());
		operatorAdder.addOperator(new PCRTemplate());
		operatorAdder.addOperator(new DNAInputTemplate());
		operatorAdder.addOperator(new DigestTemplate());
		operatorAdder.addOperator(new SlowOperatorTemplate());
	}
	
	public static void defineVisualizers(VisualizerAdder visualizerAdder){
		visualizerAdder.addVisualizer(new SequenceVisualizerTemplate());
		visualizerAdder.addVisualizer(new DataVisualizerTemplate());
		
	}
	
}
