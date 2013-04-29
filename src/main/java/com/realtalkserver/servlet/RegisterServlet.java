package com.realtalkserver.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import com.realtalkserver.util.UserManager;

@SuppressWarnings("serial")
public class RegisterServlet extends BaseServlet {
    private static final String PARAMETER_REG_ID = "PARAMETER_REG_ID";
    private static final String PARAMETER_USER = "PARAMETER_USER";
    private static final String PARAMETER_PWORD = "PARAMENTER_PWORD";
    private static final String PARAMETER_SUCCESS = "success";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String stRegId = getParameter(req, PARAMETER_REG_ID);
        String stUser = getParameter(req, PARAMETER_USER);
        String stPwd = getParameter(req, PARAMETER_PWORD);
        
        // Add the User and generate response to indicate if successful
        boolean fAddUserSuccess = UserManager.addUser(stUser, stPwd, stRegId);
        
        // Generate JSON response
        JSONObject jsonResponse = new JSONObject();
        try {
        	String stSuccessMsg = fAddUserSuccess ? "true" : "false";
        	jsonResponse.put(PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(PARAMETER_USER, stUser);
        	jsonResponse.put(PARAMETER_REG_ID, stRegId);
        	jsonResponse.put(PARAMETER_PWORD, stPwd);
        } catch (JSONException e) {
        	// Exception will never be thrown as key is not null.
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());
        setSuccess(resp);
    }
}
