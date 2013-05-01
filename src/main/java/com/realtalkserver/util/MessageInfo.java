/**
 * 
 */
package com.realtalkserver.util;

import java.sql.Time;
import java.sql.Date;

/**
 * Immutable class that holds a information about a chat message.
 * 
 * @author Colin Kho
 *
 */
public class MessageInfo {
    private String body;
    private String sender;
    private Date date;
    private Time time;
    
    

    /**
     * Constructor
     * 
     * @param body
     * @param sender
     * @param date
     * @param time
     */
    public MessageInfo(String body, String sender, Date date, Time time) {
        this.body = body;
        this.sender = sender;
        this.date = date;
        this.time = time;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    
}
