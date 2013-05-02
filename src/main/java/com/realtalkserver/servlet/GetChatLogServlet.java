/**
 * 
 */
package com.realtalkserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.realtalkserver.util.ChatServerManager;
import com.realtalkserver.util.ChatResultSet;
import com.realtalkserver.util.ChatRoomInfo;
import com.realtalkserver.util.MessageInfo;
import com.realtalkserver.util.RequestParameters;
import com.realtalkserver.util.ResponseParameters;

/**
 * Servlet that processes a request to retrieve the entire chat log for a given chat room.
 * 
 * @author Colin Kho
 *
 */
@SuppressWarnings("serial")
public class GetChatLogServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Room Info
        logger.log(Level.INFO, "Retrieving Chat Room Information");
        String stRoomName = getParameter(req, RequestParameters.PARAMETER_ROOM_NAME);
        String stRoomId = getParameter(req, RequestParameters.PARAMETER_ROOM_ID);
        // TODO: Extra Room information may be required.
        ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomId, "", 0, 0, "", 0, null);
        logger.log(Level.INFO, "Retrieval Successful");
        
        logger.log(Level.INFO, "Processing Get Chat Log Request to Database");
        ChatResultSet chatresultset = ChatServerManager.cResSetGetEntireChat(chatroominfo);
        logger.log(Level.INFO, "Request completed");
        
        // Extract ChatResultSet Params
        ChatCode chatcodeGetPost = chatresultset.getChatCode();
        List<MessageInfo> rgMessages = chatresultset.getMessages();
        
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put(RequestParameters.PARAMETER_ROOM_NAME, stRoomName);
            jsonResponse.put(RequestParameters.PARAMETER_ROOM_ID, stRoomId);
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
                // Encountered Room Error due to params related to Chat Room Info.
                jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_ERROR);
            } else if (ChatCode.ROOM_ERROR_NAME_INVALID == chatcodeGetPost) {
                // Encountered an invalid Room Name
                jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_NAME_INVALID);
            } else if (ChatCode.ROOM_ERROR_ID_INVALID == chatcodeGetPost) {
                // Encountered an invalid Room ID.
                jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_ROOM);
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_ROOM_ROOMID_INVALID);
            } else if (ChatCode.MESSAGE_ERROR == chatcodeGetPost) {
                // Encountered a message error.
                jsonResponse.put(RequestParameters.PARAMETER_SUCCESS, "false");
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_CODE, ResponseParameters.RESPONSE_ERROR_CODE_MESSAGE);
                jsonResponse.put(ResponseParameters.PARAMETER_ERROR_MSG, ResponseParameters.RESPONSE_MESSAGE_MESSAGE_ERROR);
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
        logger.log(Level.INFO, "POST Request to Pull Chat Log Messages completed");
    }
}
