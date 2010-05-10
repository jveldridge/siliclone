package edu.brown.cs32.siliclone.servlets;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.server.FastaParser;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

public class UploadSequenceFileServlet extends UploadAction {
	private static final long serialVersionUID = 3L;
	
	HashMap<String, SequenceHook> receivedFiles = new HashMap<String, SequenceHook>();
	
	@SuppressWarnings("unchecked")
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		System.out.println("executing upload action");  
			for (FileItem item : sessionFiles) {
				if (false == item.isFormField()) {
					try {
						HttpSession thisSession = getThreadLocalRequest().getSession();
						String seqName = ((Map<String,String>) thisSession.getAttribute("sequenceNames")).get(item.getFieldName());
						String seqFormat = ((Map<String,String>) thisSession.getAttribute("sequenceFormats")).get(item.getFieldName());
		    		  
						InputStream stream = item.getInputStream();
						String sequence = null;
		    		  
						if (seqFormat.equalsIgnoreCase("FASTA")) {
							sequence = new FastaParser().parseFastaFile(stream);
						}
		    		  
						SequenceHook hook = SequenceServiceImpl.saveSequence(sequence, seqName, thisSession, false);
						if (thisSession.getAttribute("uploadedSequences") == null) {
							thisSession.setAttribute("uploadedSequences", new HashMap<String,Collection<SequenceHook>>());
						}
						Collection<SequenceHook> hookcollection = new LinkedList<SequenceHook>();
						hookcollection.add(hook);
						((HashMap<String, Collection<SequenceHook>>)thisSession.getAttribute("uploadedSequences")).put(item.getFieldName(), hookcollection);
					} catch (Exception e) {
						e.printStackTrace();
						throw new UploadActionException(e.getMessage());
					}
				}
				
				removeSessionFileItems(request);
		  }
		  
			return null;
	  }

	  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  System.out.println(">>> get uploaded file called");
		  ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
		  oos.writeObject(receivedFiles.get(request.getParameter(PARAM_SHOW)));
		  oos.close();
	  }
	  

	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {}
}
