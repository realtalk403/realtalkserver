/**
 * 
 */
package com.realtalkserver.util;

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
    private String dateCreated;
    private String timeCreated;
    
    /**
     * @param name         Chat Room Name
     * @param id           Chat Room Id
     * @param location     Chat Room Location
     * @param creator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param dateCreated  Date Chat Room created
     * @param timeCreated  Time Chat Room created
     */
    public ChatRoomInfo(String name, String id, String location,
            String creator, int numUsers, String dateCreated, String timeCreated) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.creator = creator;
        this.numUsers = numUsers;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
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
     * @return the dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * @return the timeCreated
     */
    public String getTimeCreated() {
        return timeCreated;
    }
}
