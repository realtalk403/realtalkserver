/**
 * 
 */
package com.realtalkserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.ChatCode;
import com.realtalkserver.util.ChatManager;
import com.realtalkserver.util.ChatResultSet;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.MessageInfo;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;

/**
 * Servlet that gets the recent chat messages after a given time stamp.
 * 
 * @author Colin Kho
 *
 */
public class GetRecentChatServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		// Room Info
		logger.log(Level.INFO, "Retrieving Chat Room Information");
		String stRoomName = req.getParameter(RequestParameters.PARAMETER_ROOM_NAME);
		String stRoomId = req.getParameter(RequestParameters.PARAMETER_ROOM_ID);
		// TODO: Extra Room information may be required.
		ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomId, "", 0, 0, "", 0, null);
		logger.log(Level.INFO, "Retrieval Successful");
		
		// TimeStamp Info
		logger.log(Level.INFO, "Retrieving timeStamp since last message retrieval");
		String stTimeStamp = req.getParameter(RequestParameters.PARAMETER_TIMESTAMP);
		// TODO: Check for invalid long.
		long timeStamp = Long.parseLong(stTimeStamp);
		logger.log(Level.INFO, "Retrieval Successful");
		
		logger.log(Level.INFO, "Processing Get Recent Chat Request to Database"); 
		ChatResultSet chatresultset = ChatManager.cResSetGetRecentChat(chatroominfo, new Timestamp(timeStamp));
		logger.log(Level.INFO, "Request completed");
		
		// Extract ChatResultSet Params
		ChatCode chatcodeGetPost = chatresultset.getChatCode();
		List<MessageInfo> rgMessages = chatresultset.getMessages();
		
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_NAME, stRoomName);
			jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, stRoomId);
			jsonResponse.put(RequestParameters.PARAMETER_TIMESTAMP, String.valueOf(new Timestamp(timeStamp)));
			
			if (ChatCode.SUCCESS == chatcodeGetPost) {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "true");
				
				// Pass Messages
				JSONArray jsonarrayMessages = new JSONArray();
				
				// Sort Messages
				Collections.sort(rgMessages);
				for (MessageInfo message : rgMessages) {
					JSONObject jsonobjectMessage = new JSONObject();
					jsonobjectMessage.put(RequestParameters.PARAMETER_MESSAGE_SENDER, message.getSender());
					jsonobjectMessage.put(RequestParameters.PARAMETER_MESSAGE_TIMESTAMP, 
							message.getTimeStamp().getTime());
					jsonobjectMessage.put(RequestParameters.PARAMETER_MESSAGE_BODY, message.getBody());
					jsonarrayMessages.put(jsonobjectMessage);
				}
				// Add messages to json response
				jsonResponse.put(RequestParameters.PARAMETER_MESSAGE_MESSAGES, jsonarrayMessages);
			} else if (ChatCode.ROOM_ERROR == chatcodeGetPost) {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
				jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
				//jsonResponse.put(ResponseParameters.RESPONSE_ERROR_CODE_MESSAGE, ResponseParameters.)
			}
		} catch (JSONException e) {
			// Exception not thrown because key is not null.
		}
	}
}
