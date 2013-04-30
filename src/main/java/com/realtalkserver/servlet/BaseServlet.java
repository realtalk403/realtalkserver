package com.realtalkserver.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseServlet is an abstract class that provides helper utility methods for other
 * servlets to use. It extends the HttpServlet class. For debugging purposes,
 * use fDEBUG to allow GET calls.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {
    // boolean that allows GET calls for debugging purposes.
	// Should be set to false during production.
    static final boolean fDEBUG = true;
    
    // Sets up a Logger that the servlet can do to log its activity
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
      if (fDEBUG) {
        doPost(req, resp);
      } else {
        super.doGet(req, resp);
      }
    }

    protected String getParameter(HttpServletRequest req, String parameter)
        throws ServletException {
        String value = req.getParameter(parameter);
        if (isEmptyOrNull(value)) {
          if (fDEBUG) {
            StringBuilder parameters = new StringBuilder();
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Enumeration names = req.getParameterNames();
            while (names.hasMoreElements()) {
              String name = (String) names.nextElement();
              String param = req.getParameter(name);
              parameters.append(name).append("=").append(param).append("\n");
            }
            logger.fine("parameters: " + parameters);
          }
          throw new ServletException("Parameter " + parameter + " not found");
        }
        return value.trim();
    }

    protected String getParameter(HttpServletRequest req, String parameter,
        String defaultValue) {
        String value = req.getParameter(parameter);
        if (isEmptyOrNull(value)) {
            value = defaultValue;
        }
        return value.trim();
    }
    
    /**
     * Sets a HttpServletResponse to reflect success. 
     * 
     * @param resp HttpServletResponse to be set
     */
    protected void setSuccess(HttpServletResponse resp) {
    	resp.setStatus(HttpServletResponse.SC_OK);
    }
    
    /**
     * Checks to see if the given String is null or empty.
     * 
     * @param value String to be examined
     * @return true if it is empty or null, false if otherwise.
     */
    protected boolean isEmptyOrNull(String value) {
        return value == null || value.trim().length() == 0;
    }
}
