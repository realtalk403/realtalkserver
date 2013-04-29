package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.realtalkserver.util.Datastore;

@SuppressWarnings("serial")
public class HelloServlet extends BaseServlet {
    
    public static final String ATTRIBUTE_STATUS = "status";
    
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
	    PrintWriter out = resp.getWriter();

	    out.print("<html><body>");
	    out.print("<head>");
	    out.print("  <title>ChatRealTalk Google Cloud Messaging Server</title>");
	    out.print("</head>");
	    String status = (String) req.getAttribute(ATTRIBUTE_STATUS);
	    if (status != null) {
	        out.print(status);
	    }
	    
	    List<String> devices = Datastore.getDevices();
	    if (devices.isEmpty()) {
	        out.print("<h2>No Devices Registered!</h2>");
	    } else {
	        out.print("<h2>" + devices.size() + " device(s) registered!</h2>");
	    }
	    
	    out.print("</body></html>");
	    resp.setStatus(HttpServletResponse.SC_OK);
    }
	
	@Override 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	    doGet(req, resp);
	}
}
