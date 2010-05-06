package edu.brown.cs32.siliclone.plugins;

import edu.brown.cs32.siliclone.client.operators.restrictiondigest.DigestTemplate;
import edu.brown.cs32.siliclone.client.operators.pcr.PCROperatorTemplate;
import edu.brown.cs32.siliclone.client.operators.slowoperator.SlowOperatorTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.DataVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.SequenceVisualizerTemplate;
import edu.brown.cs32.siliclone.client.visualizers2.VisualizerAdder;
import edu.brown.cs32.siliclone.client.visualizers2.translation.TranslationVisualizerTemplate;
import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.anothertestop.AnotherTestOpFactory;
import edu.brown.cs32.siliclone.operators.client.dnaInput.DNAInputTemplate;

public class Plugins {

	public static void defineOperators(OperatorAdder operatorAdder){
		operatorAdder.addOperator(new AnotherTestOpFactory());
		operatorAdder.addOperator(new DNAInputTemplate());
		operatorAdder.addOperator(new DigestTemplate());
		operatorAdder.addOperator(new SlowOperatorTemplate());
		operatorAdder.addOperator(new PCROperatorTemplate());
	}
	
	public static void defineVisualizers(VisualizerAdder visualizerAdder){
		visualizerAdder.addVisualizer(new SequenceVisualizerTemplate());
		visualizerAdder.addVisualizer(new DataVisualizerTemplate());
		visualizerAdder.addVisualizer(new TranslationVisualizerTemplate());
	}
	
}
