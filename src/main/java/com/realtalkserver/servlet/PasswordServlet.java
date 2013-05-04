/**
 * 
 */
package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
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
		logger.log(Level.INFO, "Retrieving User Information");
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        String stNewPwd = getParameter(req, RequestParameters.PARAMETER_NEW_PWORD);
        logger.log(Level.INFO, "Retrieval Successful");
        
        logger.log(Level.INFO, "Processing PwdChange Request to Database");
        boolean fChangePwdSuccess = UserManager.fChangePassword(stUser, stPwd, stNewPwd, stRegId);
        logger.log(Level.INFO, "Request completed");
        
        // Generate JSON Response to the user.
        JSONObject jsonResponse = new JSONObject();
        
        try {
        	String stSuccessMsg = fChangePwdSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);
        	if (!fChangePwdSuccess) {
        		jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_USER);
        		jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_USER_ERROR);
        	}
        } catch (JSONException e) {
        	// Exception will never be throw because keys are not null
        }
        logger.log(Level.INFO, "Setting up response successful");
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
        logger.log(Level.INFO, "POST Request to Change Password completed");
	}
}
