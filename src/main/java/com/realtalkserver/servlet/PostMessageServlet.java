/**
 * 
 */
package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.ChatCode;
import com.realtalkserver.util.ChatServerManager;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.GCMSendMessages;
import com.realtalkserver.util.MessageInfo;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;

/**
 * Servlet that handles an incoming message to a given chatroom from a given user 
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class PostMessageServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// User Info
		logger.log(Level.INFO, "Retrieving User Information");
		String stUserName = getParameter(req, RequestParameters.PARAMETER_USER);
		String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
		String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
		UserInfo userInfo = new UserInfo(stUserName, stRegId, stPwd);
		logger.log(Level.INFO, "Retrieval Successful");
		
		// Room Info
		logger.log(Level.INFO, "Retrieving Chat Room Information");
		String stRoomName = getParameter(req, RequestParameters.PARAMETER_ROOM_NAME);
		String stRoomId = getParameter(req, RequestParameters.PARAMETER_ROOM_ID);
		// TODO: Extra Room information may be required.
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo(stRoomName, Integer.parseInt(stRoomId), "", 0, 0, "", 0, null);
		logger.log(Level.INFO, "Retrieval Successful");
		
		// Message Info
		logger.log(Level.INFO, "Retrieving Message Info");
		String stTimeStamp = getParameter(req, RequestParameters.PARAMETER_MESSAGE_TIMESTAMP);
		String stMessageBody = getParameter(req, RequestParameters.PARAMETER_MESSAGE_BODY);
		String stMessageSender = getParameter(req, RequestParameters.PARAMETER_MESSAGE_SENDER);
		// Convert Time Stamp to Long
		long timeStamp = Long.parseLong(stTimeStamp);
		MessageInfo messageInfo = new MessageInfo(stMessageBody, stMessageSender, timeStamp);
		logger.log(Level.INFO, "Retrieval Successful");
		
		logger.log(Level.INFO, "Processing Post Message Request to Database");
		ChatCode chatCodePostSuccess = ChatServerManager.chatcodePostMessage(userInfo, chatRoomInfo, messageInfo);
		logger.log(Level.INFO, "Request completed");
				
		JSONObject jsonResponse = new JSONObject();
		
		try {
			jsonResponse.put(RequestParameters.PARAMETER_USER, stUserName);
			jsonResponse.put(RequestParameters.PARAMETER_NEW_REG_ID, stRegId);
			jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_NAME, stRoomName);
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, stRoomId);
			
			if (chatCodePostSuccess == ChatCode.SUCCESS) {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "true");
			} else {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
				// Set error code response
				if (ChatCode.USER_ERROR == chatCodePostSuccess) {
					// User Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_USER);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_USER_ERROR);
				} else if (ChatCode.ROOM_ERROR == chatCodePostSuccess) {
					// Room Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_ERROR);
			    } else if (ChatCode.MESSAGE_ERROR == chatCodePostSuccess) {
			    	// Message Error
			    	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_MESSAGE);
			    	jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_MESSAGE_ERROR);
				} else {
					// Generic Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, "");
				}
			}
		} catch (JSONException e) {
			// Exception will never get thrown if keys not null.
		}
		logger.log(Level.INFO, "Setting up response successful");
		
		// If posting of message was successful, push message to other devices for an update.
		if (ChatCode.SUCCESS == chatCodePostSuccess) {
		    logger.log(Level.INFO, "Sending push notifications thru GCM");
		    List<UserInfo> rguserinfo = ChatServerManager.rguserinfoGetRoomUsers(chatRoomInfo);
		    String stMessageSenderAlias = ChatServerManager.stGetMessageSenderAlias(stMessageSender, chatRoomInfo);
		    MessageInfo messageinfoAnon = new MessageInfo(stMessageBody, stMessageSenderAlias, timeStamp);
		    GCMSendMessages.sendMulticastMessage(rguserinfo, messageinfoAnon, chatRoomInfo);
		    logger.log(Level.INFO, "Sending to GCM completed");
		}
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.write(jsonResponse.toString());
		setSuccess(resp);
		logger.log(Level.INFO, "POST Request to Post Message completed");
	}
}
