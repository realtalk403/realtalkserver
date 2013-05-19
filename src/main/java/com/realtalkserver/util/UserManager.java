package com.realtalkserver.util;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserManager manages the user database that stores the user information for RealTalk
 * 
 * @author Jory Rice
 *
 */

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
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_ADD_USER);
			preparedstatement.setString(1, userName);
			preparedstatement.setString(2, regId);
			preparedstatement.setString(3, password);

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// User was added
				return true;
			} else {
				// User was not added
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
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
		Connection connection = null;
		try {
			if (fAuthenticateUser(userName, password)) {
				// Connect to the database and prepare the query
				connection = DatabaseUtility.connectionGetConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_LEAVE_ALL_ROOMS);
				preparedstatement.setString(1, userName);
	
				// Execute the DELETE query
				int result = preparedstatement.executeUpdate();
				DatabaseUtility.closeConnection(connection);
				
				
				PreparedStatement preparedstatement2 = connection.prepareStatement(SQLQueries.QUERY_REMOVE_USER);
				preparedstatement.setString(1, userName);
	
				// Execute the DELETE query
				result = preparedstatement2.executeUpdate();
				DatabaseUtility.closeConnection(connection);
	
				// Check for correct result
				if (result == 1) {
					return true;
				} else {
					// User was not removed
					return false;
				}
			} else {
				// Authentication failed: user not removed
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return false;
	}

	/**
	 * Changes the password of a given User. It checks against both the username
	 * and its Google Cloud Messaging ID before attempting to change the password.
	 * 
	 * @param userName    User's username
	 * @param oldPassword User's old password
	 * @param newPassword User's new password (if successful)
	 * @param regId       User's Google Cloud Messaging Registration ID
	 * @return            true if user's password was successfully changed in the database. 
	 *                    false if otherwise.
	 */
	public static boolean fChangePassword(String userName, String oldPassword, String newPassword, String regId) {
		Connection connection = null;
		try {
			if (fAuthenticateUser(userName, oldPassword)) {
				// Connect to the database and prepare the query
				connection = DatabaseUtility.connectionGetConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_CHANGE_PASSWORD);
				preparedstatement.setString(1, newPassword);
				preparedstatement.setString(2, userName);

				// Execute the UPDATE query
				int result = preparedstatement.executeUpdate();
				DatabaseUtility.closeConnection(connection);

				// Check for correct result
				if (result == 1) {
					return true;
				} else {
					// Password was not changed
					return false;
				}
			} else {
				// Authentication failed: password does not change.
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return false;
	}

	/**
	 * Changes a User's Google Cloud Messaging ID in the database. Returns true if it
	 * was changed successfully and false if otherwise.
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @param newRegId User's Google Cloud Messaging Registration ID
	 * @return         true if user's registration ID was successfully changed 
	 * 				   in the database. false if otherwise.
	 */
	public static boolean fChangeId(String userName, String password, String newRegId) {
		Connection connection = null;
		try {
			if (fAuthenticateUser(userName, password)) {
				// Connect to the database and prepare the query
				connection = DatabaseUtility.connectionGetConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_CHANGE_ID);
				preparedstatement.setString(1, newRegId);
				preparedstatement.setString(2, userName);
	
				// Execute the UPDATE query
				int result = preparedstatement.executeUpdate();
				DatabaseUtility.closeConnection(connection);
	
				// Check for correct result
				if (result == 1) {
					return true;
				} else {
					// ID was not changed
					return false;
				}
			} else {
				// Authentication failed: ID does not change.
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return false;
	}

	/**
	 * Authenticates a user based on its username, password and registration ID. It
	 * returns true if user exists and its credentials are correct. Otherwise, it
	 * returns false. 
	 * 
	 * @param userName User's username
	 * @param password User's password
	 * @return         true if user's exists and user's credentials are correct.
	 *                 false if otherwise.
	 */
	public static boolean fAuthenticateUser(String userName, String password) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_AUTHENTICATE);
			preparedstatement.setString(1, userName);
			preparedstatement.setString(2, password);

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();
			
			// Check for correct result
			if (resultset.next()) {
				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return true;
			} else {
				// 0 rows: User does not exist or the credentials are incorrect
				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return false;
	}
}
