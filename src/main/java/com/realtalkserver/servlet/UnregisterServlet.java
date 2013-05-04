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
 * Servlet that unregisters a user and subsequently also a device, 
 * whose registration id is identified by Google Clouds Messaging Service
 * 
 * @author Colin Kho
 */
@SuppressWarnings("serial")
public class UnregisterServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	logger.log(Level.INFO, "Retrieving User Information");
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        logger.log(Level.INFO, "Retrieval Successful");
        
        // Unregister user from the server and get response
        logger.log(Level.INFO, "Processing Unregister Request to Database");
        boolean fRemoveSuccess = UserManager.fRemoveUser(stUser, stPwd, stRegId);
        logger.log(Level.INFO, "Request completed");
        
        // Create JSON response from server
        JSONObject jsonResponse = new JSONObject();
        
        try {
        	String stSuccessMsg = fRemoveSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);
        	if (!fRemoveSuccess) {
        		jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_USER);
        		jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_USER_ERROR);
        	}
        } catch (JSONException e){
        	// Exception never thrown because key is never null.
        }
        logger.log(Level.INFO, "Setting up response successful");
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
        logger.log(Level.INFO, "POST Request to UnregisterServlet completed");
    }
}
