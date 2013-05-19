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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realtalkserver.util.ChatCode;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.ChatServerManager;
import com.realtalkserver.util.ChatroomResultSet;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;
import com.realtalkserver.util.UserInfo;

/**
 * Servlet that gets the rooms a user has joined.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class UsersRoomsServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Room Info
		logger.log(Level.INFO, "Retrieving Location Information");
		String stRegId = getParameter(req, RequestParameters.PARAMETER_REG_ID);
        String stUser = getParameter(req, RequestParameters.PARAMETER_USER);
        String stPwd = getParameter(req, RequestParameters.PARAMETER_PWORD);
		logger.log(Level.INFO, "Retrieval Successful");
		
		logger.log(Level.INFO, "Processing Get User's Rooms Request to Database");
		UserInfo userinfo = new UserInfo(stUser, stPwd, stRegId);
		ChatroomResultSet crrs = ChatServerManager.crrsJoinedRooms(userinfo);
		logger.log(Level.INFO, "Request completed");
		
		// Extract ChatResultSet Params
		ChatCode chatcodeGetPost = crrs.getChatCode();
		List<ChatRoomInfo> rgcri = crrs.getRooms();
		
		JSONObject jsonResponse = new JSONObject();
		try {
			if (ChatCode.SUCCESS == chatcodeGetPost) {
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "true");
				
				// Pass Messages
				JSONArray jsonarrayRooms = new JSONArray();
				
				// Sort Messages
				for (ChatRoomInfo room : rgcri) {
					JSONObject jsonobjectRoom = new JSONObject();
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_NAME, room.getName());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_ID, room.getId());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_DESCRIPTION, room.getDescription());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_LATITUDE, room.getLatitude());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_LONGITUDE, room.getLongitude());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_CREATOR, room.getCreator());
					jsonobjectRoom.put(RequestParameters.PARAMETER_ROOM_NUM_USERS, room.getNumUsers());
					jsonobjectRoom.put(RequestParameters.PARAMETER_TIMESTAMP, room.getTimeStampCreated().getTime());
					jsonarrayRooms.put(jsonobjectRoom);
				}
				// Add messages to json response
				jsonResponse.put(RequestParameters.PARAMETER_ROOM_ROOMS, jsonarrayRooms);
			} else {
				// Encountered Failure with unknown error
				jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
				jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE);
				jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ERROR);				
			}
		} catch (JSONException e) {
			// Exception not thrown because key is not null.
		}
		
		logger.log(Level.INFO, "Setting up response successful");
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.write(jsonResponse.toString());
		setSuccess(resp);
		logger.log(Level.INFO, "POST Request to get user's rooms completed");
	}
}
