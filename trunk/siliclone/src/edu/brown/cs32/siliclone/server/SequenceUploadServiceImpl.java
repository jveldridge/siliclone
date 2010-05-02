package edu.brown.cs32.siliclone.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.operators.anothertestop.SquareService;
import edu.brown.cs32.siliclone.operators.dnaInput.SequenceUploadService;
import edu.brown.cs32.siliclone.operators.dnaInput.UploadedFileNotFoundException;
import edu.brown.cs32.siliclone.tasks.SquareTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;
import edu.brown.cs32.siliclone.tasks.client.TaskTimedOutException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SequenceUploadServiceImpl extends RemoteServiceServlet implements
		SequenceUploadService {

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

}
