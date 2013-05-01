package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import com.realtalkserver.util.RequestParameters;
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
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        
        // Add the User and generate response to indicate if successful
        boolean fAddUserSuccess = UserManager.fAddUser(stUser, stPwd, stRegId);

        // Generate JSON response
        JSONObject jsonResponse = new JSONObject();
        try {
        	String stSuccessMsg = fAddUserSuccess ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
        	jsonResponse.put(RequestParameters.PARAMETER_REG_ID, stRegId);
        	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
        } catch (JSONException e) {
        	// Exception will never be thrown as key is not null.
        }
        
        resp.setContentType("application/json");
        
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse.toString());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
