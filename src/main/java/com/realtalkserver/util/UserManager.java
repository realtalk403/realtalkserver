package com.realtalkserver.util;

public class UserManager {
	
	/**
	 * Adds a User to the User Database and returns true if user was successfully
	 * added and false if otherwise.
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param regId    User's Google Cloud Messaging Registration ID
	 * @return         true if user was added successfully to database. 
	 *                 false if otherwise.
	 */
	public static boolean fAddUser(String userName, String password, String regId) {
		return false;
	}
	
	/**
	 * Removes a User from the User Database and returns true if the user was 
	 * successfully removed and false if otherwise.
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param regId    User's Google Cloud Messaging Registration ID
	 * @return         true if user was removed successfully from the database. 
	 *                 false if otherwise.
	 */
	public static boolean fRemoveUser(String userName, String password, String regId) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Changes the password of a given User. It checks against both the username
	 * and its Google Cloud Messaging ID before attempting to change the password.
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param regId    User's Google Cloud Messaging Registration ID
	 * @return         true if user's password was successfully changed in the database. 
	 *                 false if otherwise.
	 */
	public static boolean fChangePassword(String userName, String password, String regId) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Changes a User's Google Cloud Messaging ID in the database. Returns true if it
	 * was changed successfully and false if otherwise.
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param regId    User's Google Cloud Messaging Registration ID
	 * @return         true if user's registration ID was successfully changed 
	 * 				   in the database. false if otherwise.
	 */
	public static boolean fChangeId(String userName, String password, String regId) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Authenticates a user based on its username, password and registration ID. It
	 * returns true if user exists and its credentials are correct. Otherwise, it
	 * returns false. 
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param regId    User's Google Cloud Messaging Registration ID
	 * @return         true if user's exists and user's credentials are correct.
	 *                 false if otherwise.
	 */
	public static boolean fAuthenticateUser(String userName, String password, String regId) {
		throw new UnsupportedOperationException();
	}
}
