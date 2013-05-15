/**
 * 
 */
package com.realtalkserver.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ChatRoomInfo is an immutable container class used to represent a chat room.
 * 
 * @author Colin Kho
 *
 */
public class ChatRoomInfo {
    private String name;
    private int id;
    private String description;
    private double latitude;
    private double longitude;
    private String creator;
    private int numUsers;
    private Timestamp timestampCreated;
    
    /**
     * @param name         Chat Room Name
     * @param id           Chat Room ID
     * @param description  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created.
     */
    public ChatRoomInfo(String name, int id, String description, double latitude,
            double longitude, String creator, int numUsers, Timestamp timestampCreated) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.numUsers = numUsers;
        if (timestampCreated == null) {
        	// Stamp it here
        	Date date = new Date();
        	timestampCreated = new Timestamp(date.getTime());
        }
        this.timestampCreated = timestampCreated;
    }
    
    /**
     * @param name         Chat Room Name
     * @param description  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created.
     */
    public ChatRoomInfo(String name, String description, double latitude,
            double longitude, String creator, int numUsers, Timestamp timestampCreated) {
        this.name = name;
        this.id = -1;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.numUsers = numUsers;
        if (timestampCreated == null) {
        	// Stamp it here
        	Date date = new Date();
        	timestampCreated = new Timestamp(date.getTime());
        }
        this.timestampCreated = timestampCreated;
    }
    
    /**
     * @param name         Chat Room Name
     * @param id           Chat Room Id
     * @param description  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created in the
     *                     form of a long where it is the milliseconds 
     *                     since January 1, 1970, 00:00:00 GMT
     */
    public ChatRoomInfo(String name, int id, String description, double latitude,
            double longitude, String creator, int numUsers, long timestampCreated) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.numUsers = numUsers;
        this.timestampCreated = new Timestamp(timestampCreated);
    }

    /**
     * @param name         Chat Room Name
     * @param description  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created in the
     *                     form of a long where it is the milliseconds 
     *                     since January 1, 1970, 00:00:00 GMT
     */
    public ChatRoomInfo(String name, String description, double latitude,
    		double longitude, String creator, int numUsers, long timestampCreated) {
    	this.name = name;
    	this.id = -1;
    	this.description = description;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.creator = creator;
    	this.numUsers = numUsers;
    	this.timestampCreated = new Timestamp(timestampCreated);
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
    public int getId() {
        return id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
    	return description;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
    	return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
    	return longitude;
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
		return new Timestamp(timestampCreated.getTime());
	}

}
