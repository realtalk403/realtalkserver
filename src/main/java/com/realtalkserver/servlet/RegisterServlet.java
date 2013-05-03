package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;
import com.realtalkserver.util.UserManager;
/**
 * RegisterServlet is a servlet used for registering a user to the database.
 * 
 * @author Colin Kho
 *
 */

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
    /**
     * doPost handles a post request from to the server. This adds a user
     * to the server and returns an appropriate response in JSON.
     * 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
		// User Info
		logger.log(Level.INFO, "Retrieving User Information");
		String stUserName = req.getParameter(RequestParameters.PARAMETER_USER);
		String stRegId = req.getParameter(RequestParameters.PARAMETER_REG_ID);
		String stPwd = req.getParameter(RequestParameters.PARAMETER_PWORD);
		logger.log(Level.INFO, "Retrieval Successful");
        
        // Add the User and generate response to indicate if successful
		logger.log(Level.INFO, "Processing Add User Request to Database");
        boolean fAddUserSuccess = UserManager.fAddUser(stUserName, stPwd, stRegId);
        logger.log(Level.INFO, "Request completed");

        // Generate JSON response
        JSONObject jsonResponse = new JSONObject();
        try {
        	String stSuccessMsg = fAddUserSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);
        	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_USER);
        	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_USER_ERROR);
        } catch (JSONException e) {
        	// Exception will never be thrown as key is not null.
        }
        logger.log(Level.INFO, "Setting up response successful");
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
        logger.log(Level.INFO, "POST Request to RegisterServlet completed");
    }
}
