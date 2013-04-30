/**
 * 
 */
package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.UserManager;

/**
 * Servlet that handles a request to change the registration ID of a user. It
 * requires a password for authentication purposes.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class RegistrationIdServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Retrieve the parameter information.
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        
        String stNewRegId = getParameter(req, RequestParameters.PARAMETER_NEW_REG_ID);
        
        boolean fChangeIdSuccess = UserManager.fChangeId(stUser, stPwd, stNewRegId);
        
        // Generate JSON Response
        JSONObject jsonResponse = new JSONObject();
        try {
        	String stSuccessMsg = fChangeIdSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
        	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
        	jsonResponse.put(RequestParameters.PARAMETER_NEW_REG_ID, stNewRegId);
        } catch (JSONException e) {
        	// Exception will never be throw because keys are not null
        }
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
	}
}
