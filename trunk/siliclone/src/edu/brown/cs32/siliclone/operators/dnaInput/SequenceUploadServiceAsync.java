package edu.brown.cs32.siliclone.operators.dnaInput;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

public interface SequenceUploadServiceAsync {

	void getUploadedSequenceHook(String fieldName,
			AsyncCallback<SequenceHook> callback) ;

}
