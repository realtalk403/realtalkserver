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
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        
        // Unregister user from the server and get response
        boolean fRemoveSuccess = UserManager.fRemoveUser(stUser, stPwd, stRegId);
        
        // Create JSON response from server
        JSONObject jsonResponse = new JSONObject();
        
        try {
        	String stSuccessMsg = fRemoveSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
        	jsonResponse.put(RequestParameters.PARAMETER_REG_ID, stRegId);
        	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
        } catch (JSONException e){
        	// Exception never thrown because key is never null.
        }
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
    }
}
