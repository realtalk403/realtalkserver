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

import com.realtalkserver.util.ChatCode;
import com.realtalkserver.util.ChatManager;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;

/**
 * Servlet that handles a user leaving a chat room.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class LeaveRoomServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// User Info
		logger.log(Level.INFO, "Retrieving User Information");
		String stUserName = req.getParameter(RequestParameters.PARAMETER_USER);
		String stRegId = req.getParameter(RequestParameters.PARAMETER_REG_ID);
		String stPwd = req.getParameter(RequestParameters.PARAMETER_PWORD);
		UserInfo userInfo = new UserInfo(stUserName, stRegId, stPwd);
		logger.log(Level.INFO, "Retrieval Successful");
		
		// Room Info
		logger.log(Level.INFO, "Retrieving Chat Room Information");
		String stRoomName = req.getParameter(RequestParameters.PARAMETER_ROOM_NAME);
		String stRoomId = req.getParameter(RequestParameters.PARAMETER_ROOM_ID);
		// TODO: Extra Room information may be required.
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo(stRoomName, stRoomId, "", 0, 0, "", 0, null);
		logger.log(Level.INFO, "Retrieval Successful");
		
		logger.log(Level.INFO, "Processing Leave Request to Database");
		ChatCode chatCodeJoinSuccess = ChatManager.chatcodeLeaveRoom(userInfo, chatRoomInfo);
		logger.log(Level.INFO, "Request completed");
		
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put(RequestParameters.PARAMETER_USER, stUserName);
			jsonResponse.put(RequestParameters.PARAMETER_NEW_REG_ID, stRegId);
			jsonResponse.put(RequestParameters.PARAMETER_PWORD, stPwd);
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_NAME, stRoomName);
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, stRoomId);
			
			if (chatCodeJoinSuccess == ChatCode.SUCCESS) {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "true");
			} else {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
				// Set error code
				if (ChatCode.USER_ERROR == chatCodeJoinSuccess) {
					// User Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_USER);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_USER_ERROR);
				} else if (ChatCode.ROOM_ERROR == chatCodeJoinSuccess) {
					// Room Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_ERROR);
			    } else {
					// Generic Error
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR);
					jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, "");
				}
			}
		} catch (JSONException e) {
			// Exception will never get thrown if keys not null.
		}
		logger.log(Level.INFO, "Setting up response successful");
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.write(jsonResponse.toString());
		setSuccess(resp);
		logger.log(Level.INFO, "POST Request to Leave Room completed");
	}
}
