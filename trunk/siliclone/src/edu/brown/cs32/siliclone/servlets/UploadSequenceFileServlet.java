package edu.brown.cs32.siliclone.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import edu.brown.cs32.siliclone.dna.SequenceHook;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

public class UploadSequenceFileServlet extends UploadAction {
	  private static final long serialVersionUID = 1L;
	
	HashMap<String, SequenceHook> receivedFiles = new HashMap<String, SequenceHook>();
	
	  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		    for (FileItem item : sessionFiles) {
		      if (false == item.isFormField()) {
		        try {
		          item.getInputStream();
		          receivedFiles.put(item.getFieldName(), new SequenceHook(2, 6, item.getName()));
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
		  
		  File f = new File("/home/jeldridg/Desktop/quiz3.gif");
		  FileInputStream is = new FileInputStream(f);
		  copyFromInputStreamToOutputStream(is, response.getOutputStream());
		  
		  
	  }
	  

	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {}
}
