package edu.brown.cs32.siliclone.servlets;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;
import edu.brown.cs32.siliclone.server.FastaParser;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UploadSequenceFileServlet extends UploadAction {
	private static final long serialVersionUID = 2L;

	HashMap<String, SequenceHook> receivedFiles = new HashMap<String, SequenceHook>();
	
	  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		  System.out.println("executing upload action");  
		  for (FileItem item : sessionFiles) {
		      if (false == item.isFormField()) {
		    	  try {
		    		  InputStream stream = item.getInputStream();
		    		  FastaParser parser = new FastaParser();
		    		  String sequence = parser.parseFastaFile(stream);
		    		  System.out.println(sequence);
		    		  receivedFiles.put(item.getFieldName(), new SequenceHook(2, 6, item.getName()));
		    		  HttpSession thissession = getThreadLocalRequest().getSession();
		    		  System.out.println("names: " + thissession.getAttribute("sequenceNames"));
		    		  System.out.println("formats: " + thissession.getAttribute("sequenceFormats"));
		    		  if(thissession.getAttribute("uploadedSequences")==null) {
		    			  thissession.setAttribute("uploadedSequences", new HashMap<String, SequenceHook>() );
		    		  }

					String seqName = ((Map<String,String>) thissession.getAttribute("sequenceNames")).get(item.getFieldName());
					
					SequenceHook hook = new SequenceServiceImpl().saveSequence(sequence, new HashMap<String, Collection<Feature>>(), seqName, new HashMap<String, IsSerializable>());
		    		  //SequenceHook hook = new SequenceHook(2, 6, item.getName());
		    		  ((HashMap<String, SequenceHook>)thissession.getAttribute("uploadedSequences")).put(item.getFieldName(), hook);
		    		  System.out.println("BOOHAAAA"+item.getName());
		        } catch (Exception e) {
		          throw new UploadActionException(e.getMessage());
		        }
		      }
		      removeSessionFileItems(request);
		    }
		    return null;
		  }

	  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  System.out.println("get uploaded file called");
		  ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
		  oos.writeObject(receivedFiles.get(request.getParameter(PARAM_SHOW)));
		  oos.close();
	  }
	  

	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {}
}
