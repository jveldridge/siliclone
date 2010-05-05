package edu.brown.cs32.siliclone.operators.dnaInput;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;

@RemoteServiceRelativePath("sequenceupload")
public interface SequenceUploadService extends RemoteService {

	public SequenceHook getUploadedSequenceHook(String fieldName) throws UploadedFileNotFoundException;
	
	public void setUploadedFileAttributes(String filePath, String name, String format) throws UploadedFileNotFoundException;
	
}
