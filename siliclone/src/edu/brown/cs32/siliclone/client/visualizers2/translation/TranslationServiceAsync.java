package edu.brown.cs32.siliclone.client.visualizers2.translation;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface TranslationServiceAsync {

	void getForwardTranslationOne(SequenceHook seq,
			AsyncCallback<String> callback);

	void getForwardTranslationThree(SequenceHook seq,
			AsyncCallback<String> callback);

	void getForwardTranslationTwo(SequenceHook seq,
			AsyncCallback<String> callback);

	void getReverseTranslationOne(SequenceHook seq,
			AsyncCallback<String> callback);

	void getReverseTranslationTwo(SequenceHook seq,
			AsyncCallback<String> callback);

	void getReverseTranslationThree(SequenceHook seq,
			AsyncCallback<String> callback);

}