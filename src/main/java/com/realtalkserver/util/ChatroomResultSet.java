/**
 * 
 */
package com.realtalkserver.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a container containing a payload and a ChatCode. It is used mainly for
 * returning results from ChatManager in a compact format.
 * 
 * @author Colin Kho
 *
 */
public class ChatRoomResultSet {
    private List<ChatRoomInfo> rooms;
    private ChatCode chatCode;
    
    /**
     * @param messages
     * @param chatCode
     */
    public ChatRoomResultSet(List<ChatRoomInfo> rooms, ChatCode chatCode) {
    	if (rooms == null) {
    		rooms = new ArrayList<ChatRoomInfo>();
    	}
        this.rooms = rooms;
        this.chatCode = chatCode;
    }

    /**
     * @return the messages
     */
    public List<ChatRoomInfo> getRooms() {
        return new ArrayList<ChatRoomInfo>(rooms);
    }

    /**
     * @return the chatCode
     */
    public ChatCode getChatCode() {
        return chatCode;
    }
    
    /**
     * Inserts a ChatRoomInfo object into the ChatroomResultSet
     *
     * @param roomInfo
     */
    public void addRoomInfo(ChatRoomInfo roomInfo) {
        rooms.add(roomInfo);
    }
}
