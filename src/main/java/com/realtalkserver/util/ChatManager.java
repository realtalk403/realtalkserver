/**
 * 
 */
package com.realtalkserver.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * ChatManager is a class that manages the chat logs within the server. It provides
 * methods that allow the manipulation of the files within the chat server.
 * 
 * @author Colin Kho
 *
 */
public class ChatManager {
    /**
     * Adds new a room to the chat server given the params and returns the chatId.
     * 
     * @param  userInfo     User's Information
     * @param  chatRoomInfo ChatRooms's Information
     * @return ChatId       if room was successfully added. -1 if room already exists and -2 if otherwise.
     */
    public static int iAddRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
        // Check if user exists
        // Add Room ( Two Parts - ChatLogXMLManager and Database)
        // Add Room to users joined list
        // return ChatCode.FAILURE;
        throw new UnsupportedOperationException();
    }
    
    /**
     * Allows the given user to join a given chat room.
     * 
     * @param  userInfo     User's Information
     * @param  chatRoomInfo ChatRooms's Information
     * @return              Appropriate Chat Code denoting the result.
     */
    public static ChatCode chatcodeJoinRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Removes a given user from a given chat room.
     * 
     * @param  userInfo     User's Information
     * @param  chatRoomInfo ChatRooms's Information
     * @return              Appropriate Chat Code denoting the result.
     */
    public static ChatCode chatcodeLeaveRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Posts a message to a chat room from a user.
     * 
     * @param  userInfo     User's Information
     * @param  chatRoomInfo ChatRooms's Information
     * @param  MessageInfo  Message's Information
     * @return              Appropriate Chat Code denoting the result.
     */
    public static ChatCode chatcodePostMessage(UserInfo userInfo, ChatRoomInfo chatRoomInfo, MessageInfo msgInfo) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Retrieves the most recent chat messages from a given chatroom's chatlog since
     * the given time and date.
     * 
     * 
     * @param chatRoomInfo ChatRoom's Information
     * @param timeStamp    Messages to retrieve after the indicated timeStamp
     * @return             ChatResultSet that has the most recent messages if successful(ChatCode), otherwise
     *                     ChatResultSet's ChatCode reflects the appropriate error.
     */
    public static ChatResultSet cResSetGetRecentChat(ChatRoomInfo chatRoomInfo, Timestamp timeStamp) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Retrieves the entire chat log from a given chatroom and returns it in a ChatResultSet.
     * 
     * @param chatRoomInfo ChatRoom's Information
     * @return             ChatResultSet that has the entire chat log if successful(ChatCode), otherwise
     *                     ChatResultSet's ChatCode reflects the appropriate error.
     */
    public static ChatResultSet cResSetGetEntireChat(ChatRoomInfo chatRoomInfo) {
        throw new UnsupportedOperationException();
    }
}
