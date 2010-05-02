package edu.brown.cs32.siliclone.dna.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Pete
 * 
 */
public class ProjectServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(ProjectServlet.class);

	public ProjectServlet() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doPost called");
		process(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doGet called");
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("process called");
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				processFiles(request, response);
			} else {
				processQuery(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processQuery(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("processQuery called");
		try {
//			String contextName = request.getParameter("context");
//			if (contextName == null) {
//				ProjectError.report(response, Status.MISSING_CONTEXT);
//				return;
//			}
//			ProjectContext context = ContextService.get().getContext(
//					contextName);
//			assert (context != null);
//			ProjectState state = (ProjectState) request.getSession()
//					.getAttribute(contextName);
//			Request req = new Request(request, response);
//			if (state != null)
//				state.request(req);
//			else {
				response.sendRedirect("/upload");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

	}

	private void processFiles(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("processFiles called");
		HashMap<String, String> args = new HashMap<String, String>();
		boolean isGWT = true;
		try {
			if (log.isDebugEnabled())
				log.debug(request.getParameterMap());
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			// pick up parameters first and note actual FileItem
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				if (item.isFormField()) {
					args.put(name, Streams.asString(item.openStream()));
				} else {
					args.put("contentType", item.getContentType());
					String fileName = item.getName();
					int slash = fileName.lastIndexOf("/");
					if (slash < 0)
						slash = fileName.lastIndexOf("\\");
					if (slash > 0)
						fileName = fileName.substring(slash + 1);
					args.put("fileName", fileName);
					// upload requests can come from smartGWT (args) or
					// FCKEditor (request)
					String contextName = args.get("context");
					String model = args.get("model");
					String path = args.get("path");
					if (contextName == null) {
						isGWT = false;
						contextName = request.getParameter("context");
						model = request.getParameter("model");
						path = request.getParameter("path");
						if (log.isDebugEnabled())
							log.debug("query=" + request.getQueryString());
					} else if (log.isDebugEnabled())
						log.debug(args);
//					ProjectContext context = ContextService.get().getContext(
//							contextName);
//					ProjectState state = (ProjectState) request.getSession()
//							.getAttribute(contextName);
//					InputStream in = null;
//					try {
//						in = item.openStream();
//						state.getFileManager().storeFile(
//								context.getModel(model), path + fileName, in);
//					} catch (Exception e) {
//						e.printStackTrace();
//						log.error("Fail to upload " + fileName + " to " + path);
//					} finally {
//						if (in != null)
//							try {
//								in.close();
//							} catch (Exception e) {
//							}
//					}
				}
			}
			// TODO: need to handle conversion options and error reporting
			response.setContentType("text/html");
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			if (isGWT) {
				out.println("<script type=\"text/javascript\">");
				out
						.println("if (parent.uploadComplete) parent.uploadComplete('"
								+ args.get("fileName") + "');");
				out.println("</script>");
			} else
				out.println(getEditorResponse());
			out.println("</body>");
			out.println("</html>");
			out.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private String getEditorResponse() {
		StringBuffer sb = new StringBuffer(400);
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("(function(){var d=document.domain;while (true){try{var A=window.parent.document.domain;break;}catch(e) {};d=d.replace(/.*?(?:\\.|$)/,'');if (d.length==0) break;try{document.domain=d;}catch (e){break;}}})();\n");
		sb.append("window.parent.OnUploadCompleted(0");
		sb.append(");\n");
		sb.append("</script>");
		return sb.toString();
	}
}