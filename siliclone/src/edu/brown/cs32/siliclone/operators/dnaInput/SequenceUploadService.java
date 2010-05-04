package edu.brown.cs32.siliclone.operators.dnaInput;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;

@RemoteServiceRelativePath("sequenceupload")
public interface SequenceUploadService extends RemoteService {

	
	public SequenceHook getUploadedSequenceHook(String fieldName) throws UploadedFileNotFoundException ;
	
}
