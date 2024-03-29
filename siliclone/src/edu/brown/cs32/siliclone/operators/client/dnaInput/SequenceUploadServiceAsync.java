package edu.brown.cs32.siliclone.operators.client.dnaInput;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

public interface SequenceUploadServiceAsync {

	void getUploadedSequenceHook(String fieldName,
			AsyncCallback<Collection<SequenceHook>> callback) ;

	void setUploadedFileAttributes(String filePath, String name, String format,
			AsyncCallback<Void> callback) throws UploadedFileNotFoundException;

}
