package edu.brown.cs32.siliclone.client.visualizers2.translation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.client.DataServiceException;

@RemoteServiceRelativePath("translation")
public interface TranslationService extends RemoteService {

	public String getForwardTranslationOne(SequenceHook seq) throws DataServiceException;
	
	public String getForwardTranslationTwo(SequenceHook seq) throws DataServiceException;
	
	public String getForwardTranslationThree(SequenceHook seq) throws DataServiceException;
	
	public String getReverseTranslationOne(SequenceHook seq) throws DataServiceException;
	
	public String getReverseTranslationTwo(SequenceHook seq) throws DataServiceException;
	
	public String getReverseTranslationThree(SequenceHook seq) throws DataServiceException;
}
