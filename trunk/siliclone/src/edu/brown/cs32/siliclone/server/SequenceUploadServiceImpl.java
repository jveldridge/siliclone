package edu.brown.cs32.siliclone.server;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.client.dnaInput.SequenceUploadService;
import edu.brown.cs32.siliclone.operators.client.dnaInput.UploadedFileNotFoundException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SequenceUploadServiceImpl extends RemoteServiceServlet implements
		SequenceUploadService {

	@SuppressWarnings("unchecked")
	public SequenceHook getUploadedSequenceHook(String fieldName) throws UploadedFileNotFoundException {
        HttpSession thissession = this.getThreadLocalRequest().getSession();
        Object uploadedSequences = thissession.getAttribute("uploadedSequences");
        if(uploadedSequences==null||!(uploadedSequences instanceof HashMap)){
        	throw new UploadedFileNotFoundException();
        }
        HashMap<String, SequenceHook> castedUploadedSequences = ((HashMap<String, SequenceHook>)uploadedSequences);
        SequenceHook sh = castedUploadedSequences.get(fieldName);
        if(sh== null){
        	throw new UploadedFileNotFoundException();
        }
        return sh;
	}
	
	@SuppressWarnings("unchecked")
	public void setUploadedFileAttributes(String key, String name, String format) throws UploadedFileNotFoundException {
		HttpSession thisSession = this.getThreadLocalRequest().getSession();
        
		if(thisSession.getAttribute("sequenceNames")==null) {
			thisSession.setAttribute("sequenceNames", new HashMap<String, String>());
		}
		((HashMap<String, String>)thisSession.getAttribute("sequenceNames")).put(key, name);

		if(thisSession.getAttribute("sequenceFormats")==null) {
			thisSession.setAttribute("sequenceFormats", new HashMap<String, String>());
		}
		((HashMap<String, String>)thisSession.getAttribute("sequenceFormats")).put(key, format);
	}


}
