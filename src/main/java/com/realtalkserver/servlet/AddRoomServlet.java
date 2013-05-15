package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.ChatServerManager;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;
/**
 * AddRoomServlet is a servlet used for adding a chat room.
 * 
 * @author Taylor Williams
 *
 */

@SuppressWarnings("serial")
public class AddRoomServlet extends BaseServlet {
    /**
     * doPost handles a post request from to the server. This adds a chat room
     * to the server and returns an appropriate response in JSON.
     * 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    	logger.log(Level.INFO, "Retrieving User Information");
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        logger.log(Level.INFO, "Retrieval Successful");
        
        logger.log(Level.INFO, "Retrieving room info");
        String stRoomName = getParameter(req, RequestParameters.PARAMETER_ROOM_NAME);
        String stRoomDescription = getParameter(req, RequestParameters.PARAMETER_ROOM_DESCRIPTION);
        logger.log(Level.INFO, "Retrieval Successful");
        
        UserInfo userinfo = new UserInfo(stUser, stPwd, stRegId);
        ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomDescription, 0, 0, stUser, 1, null);
        // Add room and generate response to indicate if successful
        logger.log(Level.INFO, "Sending query to database");
        int iChatroom = ChatServerManager.addRoom(userinfo, chatroominfo);
        logger.log(Level.INFO, "Query complete");
        
        // Generate JSON response
        JSONObject jsonResponse = new JSONObject();
        try {
        	boolean fSucceeded = iChatroom >= 0;
        	String stSuccessMsg = fSucceeded ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);
        	if (fSucceeded) {
            	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
            	jsonResponse.put(RequestParameters.PARAMETER_REG_ID, stRegId);
            	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
            	jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, iChatroom);
        	}
        	else {
            	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
            	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_ERROR);
        	}
        } catch (JSONException e) {
        	// Exception will never be thrown as key is not null.
        }
        logger.log(Level.INFO, "Setting up response successful");
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse.toString());
        setSuccess(resp);
        logger.log(Level.INFO, "POST Request to Add Room completed");
    }
}
