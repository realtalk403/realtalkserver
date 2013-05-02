/**
 * 
 */
package com.realtalkserver.util;

import java.sql.Timestamp;

/**
 * ChatRoomInfo is an immutable container class used to represent a chat room.
 * 
 * @author Colin Kho
 *
 */
public class ChatRoomInfo {
    private String name;
    private String id;
    private String location; //TODO : May want to be coordinates
    private String creator;
    private int numUsers;
    private Timestamp timeStampCreated;
    
    /**
     * @param name         Chat Room Name
     * @param id           Chat Room Id
     * @param location     Chat Room Location
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timeStamp    Timestamp of when room was created.
     */
    public ChatRoomInfo(String name, String id, String location,
            String creator, int numUsers, Timestamp timeStampCreated) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.creator = creator;
        this.numUsers = numUsers;
        this.timeStampCreated = timeStampCreated;
    }
    
    /**
     * @param name         Chat Room Name
     * @param id           Chat Room Id
     * @param location     Chat Room Location
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timeStamp    Timestamp of when room was created in the
     *                     form of a long where it is the milliseconds 
     *                     since January 1, 1970, 00:00:00 GMT
     */
    public ChatRoomInfo(String name, String id, String location,
            String creator, int numUsers, long timeStampCreated) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.creator = creator;
        this.numUsers = numUsers;
        this.timeStampCreated = new Timestamp(timeStampCreated);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @return the numUsers
     */
    public int getNumUsers() {
        return numUsers;
    }

	/**
	 * @return the timeStampCreated
	 */
	public Timestamp getTimeStampCreated() {
		return new Timestamp(timeStampCreated.getTime());
	}

}
