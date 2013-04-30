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
 * Servlet that handles the request to change a users password.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class PasswordServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Retrieve the parameter information.
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        
        String stNewPwd = getParameter(req, RequestParameters.PARAMETER_NEW_PWORD);
        
        boolean fChangePwdSuccess = UserManager.fChangePassword(stUser, stPwd, stPwd, stNewPwd);
        
        // Generate JSON Response to the user.
        JSONObject jsonResponse = new JSONObject();
        
        try {
        	String stSuccessMsg = fChangePwdSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
        	jsonResponse.put(RequestParameters.PARAMETER_REG_ID, stRegId);
        	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
        	jsonResponse.put(RequestParameters.PARAMETER_NEW_PWORD, stNewPwd);
        } catch (JSONException e) {
        	// Exception will never be throw because keys are not null
        }
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
	}
}
