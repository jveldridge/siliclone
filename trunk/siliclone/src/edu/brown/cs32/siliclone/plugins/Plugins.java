package edu.brown.cs32.siliclone.plugins;

import edu.brown.cs32.siliclone.client.visualizers.DataVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers.SequenceVisualizer;
import edu.brown.cs32.siliclone.client.visualizers.SequenceVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers.VisualizerAdder;
import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.anothertestop.AnotherTestOpFactory;
import edu.brown.cs32.siliclone.operators.dnaInput.DNAInputTemplate;
import edu.brown.cs32.siliclone.operators.pcr.PCRTemplate;
import edu.brown.cs32.siliclone.operators.rd.DigestTemplate;

public class Plugins {

	public static void defineOperators(OperatorAdder operatorAdder){
		operatorAdder.addOperator(new AnotherTestOpFactory());
		operatorAdder.addOperator(new PCRTemplate());
		operatorAdder.addOperator(new DNAInputTemplate());
		operatorAdder.addOperator(new DigestTemplate());
	}
	
	public static void defineVisualizers(VisualizerAdder visualizerAdder){
		visualizerAdder.addVisualizer(new SequenceVisualizerTemplate());
		visualizerAdder.addVisualizer(new DataVisualizerTemplate());
		
	}
	
}
