package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import com.realtalkserver.util.ChatManager;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.UserInfo;
import com.realtalkserver.util.UserManager;
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
        String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
        
        String stRoomId = getParameter(req, RequestParameters.PARAMETER_ROOM_ID);
        String stRoomName = getParameter(req, RequestParameters.PARAMETER_ROOM_NAME);
        String stRoomDescription = getParameter(req, RequestParameters.PARAMETER_ROOM_DESCRIPTION);
        
        UserInfo userinfo = new UserInfo(stUser, stPwd, stRegId);
        ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomId, stRoomDescription, 0, 0, stUser, 1, null);
        // Add room and generate response to indicate if successful
        int iChatroom = ChatManager.iAddRoom(userinfo, chatroominfo);
        
        // Generate JSON response
        JSONObject jsonResponse = new JSONObject();
        try {
        	String stSuccessMsg = iChatroom >= 0 ? "true" : "false";
        	jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, stSuccessMsg);	
        	jsonResponse.put(RequestParameters.PARAMETER_USER, stUser);
        	jsonResponse.put(RequestParameters.PARAMETER_REG_ID, stRegId);
        	jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
        	jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, iChatroom);
        } catch (JSONException e) {
        	// Exception will never be thrown as key is not null.
        }
        
        resp.setContentType("application/json");
        
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse.toString());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
