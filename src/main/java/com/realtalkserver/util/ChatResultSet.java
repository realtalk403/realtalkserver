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
public class ChatResultSet {
    private List<MessageInfo> messages;
    private int chatId;
    private ChatCode chatCode;
    
    /**
     * @param messages
     * @param chatCode
     */
    public ChatResultSet(List<MessageInfo> messages, ChatCode chatCode, int chatId) {
    	if (messages == null) {
    		messages = new ArrayList<MessageInfo>();
    	}
        this.messages = messages;
        this.chatCode = chatCode;
        this.chatId = chatId;
    }
    
    /**
     * @param chatCode
     */
    public ChatResultSet(ChatCode chatCode, int chatId) {
        this(new ArrayList<MessageInfo>(), chatCode, chatId);
    }
    
    /**
     * @param chatCode
     */
    public ChatResultSet(List<MessageInfo> messages, ChatCode chatCode) {
        this(messages, chatCode, 0);
    }
    
    /**
     * @param chatCode
     */
    public ChatResultSet(ChatCode chatCode) {
        this(new ArrayList<MessageInfo>(), chatCode, 0);
    }

    /**
     * @return the messages
     */
    public List<MessageInfo> getMessages() {
        return new ArrayList<MessageInfo>(messages);
    }

    /**
     * @return the chatCode
     */
    public ChatCode getChatCode() {
        return chatCode;
    }
    
    /**
     * 
     * @return the chatId
     */
    public int getChatId() {
        return chatId;
    }
    
    /**
     * Inserts a MessageInfo object into the ChatResultSet
     * 
     * @param msgInfo MessageInfo
     */
    public void addMessageInfo(MessageInfo msgInfo) {
        messages.add(msgInfo);
    }
}
