/**
 * 
 */
package com.realtalkserver.util;

/**
 * UserInfo is an immutable class that contains meta information about the user.
 * 
 * @author Colin Kho
 *
 */
public class UserInfo {
    private String name;
    private String password;
    private String registrationId;
    
    /**
     * @param name           User's Name
     * @param password       User's Password
     * @param registrationId User's Google Cloud Messaging Registration ID
     */
    public UserInfo(String userName, String password, String registrationId) {
        this.name = userName;
        this.password = password;
        this.registrationId = registrationId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the registrationId
     */
    public String getRegistrationId() {
        return registrationId;
    }
}
