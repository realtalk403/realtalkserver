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

	// These are essentially SQL query templates. Question marks
	// are replaced by parameters in the methods in this class.
	private static final String QUERY_ADD_USER = 
			"INSERT INTO users values('?', '?', '?');";

	private static final String QUERY_REMOVE_USER = 
			"DELETE FROM users WHERE user_name = '?';";

	private static final String QUERY_CHANGE_PASSWORD = 
			"UPDATE users SET password = '?' WHERE user_name = '?';";

	private static final String QUERY_CHANGE_ID = 
			"UPDATE users SET device_id = '?' WHERE user_name = '?';";

	private static final String QUERY_AUTHENTICATE = 
			"SELECT * FROM users WHERE user_name = '?' AND password = '?' AND device_id = '?';";

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
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_USER);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, regId);
			preparedStatement.setString(3, password);

			// Execute the INSERT query
			ResultSet resultSet = DatabaseUtility.resultsetProcessQuery(preparedStatement);
			DatabaseUtility.closeConnection(connection);
			System.err.println(resultSet.toString());
			// Check for correct result
			if (resultSet.first() && resultSet.getInt("count") == 1) {
			    System.err.println("Add success");
				return true;
			} else {
				// User was not added
			    System.err.println("Add failure");
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
		    System.err.print("Connection failed");
			return false;
		} catch (SQLException e) {
		    System.err.print("Query failed");
			// SQL INSERT query failed: user was not added.
			return false;
		}
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
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY_REMOVE_USER);
			preparedStatement.setString(1, userName);

			// Execute the DELETE query
			ResultSet resultSet = DatabaseUtility.resultsetProcessQuery(preparedStatement);
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (resultSet.first() && resultSet.getInt("count") == 1) {
				return true;
			} else {
				// User was not removed
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
			return false;
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
			return false;
		}
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
		try {
			boolean fAuthentication = fAuthenticateUser(userName, oldPassword, regId);
			if (fAuthentication) {
				// Connect to the database and prepare the query
				Connection connection = DatabaseUtility.connectionGetConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CHANGE_PASSWORD);
				preparedStatement.setString(1, newPassword);
				preparedStatement.setString(2, userName);

				// Execute the UPDATE query
				ResultSet resultSet = DatabaseUtility.resultsetProcessQuery(preparedStatement);
				DatabaseUtility.closeConnection(connection);

				// Check for correct result
				if (resultSet.first() && resultSet.getInt("count") == 1) {
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
			return false;
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
			return false;
		}
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
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CHANGE_ID);
			preparedStatement.setString(1, newRegId);
			preparedStatement.setString(2, userName);

			// Execute the UPDATE query
			ResultSet resultSet = DatabaseUtility.resultsetProcessQuery(preparedStatement);
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (resultSet.first() && resultSet.getInt("count") == 1) {
				return true;
			} else {
				// ID was not changed
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
			return false;
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
			return false;
		}
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
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY_AUTHENTICATE);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, regId);

			// Execute the SELECT query
			ResultSet resultSet = DatabaseUtility.resultsetProcessQuery(preparedStatement);
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (resultSet.first()) {
				return true;
			} else {
				// 0 rows: User does not exist or the credentials are incorrect
				return false;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: user was not added.
			return false;
		} catch (SQLException e) {
			// SQL INSERT query failed: user was not added.
			return false;
		}
	}
}
