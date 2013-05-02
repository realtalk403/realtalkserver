/**
 * 
 */
package com.realtalkserver.util;


import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatManager is a class that manages the chat logs within the server. It provides
 * methods that allow the manipulation of the files within the chat server.
 * 
 * @author Colin Kho and Jory Rice
 *
 */
public class ChatServerManager {

	/**
	 * Adds new a room to the chat server given the params and returns the chatId.
	 * 
	 * @param  userInfo     User's Information
	 * @param  chatRoomInfo ChatRooms's Information
	 * @return              0 if room was successfully added. -1 if room already exists and -2 if otherwise.
	 */
	public static int iAddRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_ADD_ROOM);
			preparedstatement.setString(1, chatRoomInfo.getId());
			preparedstatement.setString(2, chatRoomInfo.getName());
			preparedstatement.setString(3, chatRoomInfo.getDescription());
			preparedstatement.setTimestamp(4, chatRoomInfo.getTimeStampCreated());
			preparedstatement.setString(5, chatRoomInfo.getCreator());
			preparedstatement.setDouble(6, chatRoomInfo.getLatitude());
			preparedstatement.setDouble(7, chatRoomInfo.getLongitude());

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// Room was added
				return 0;
			} else {
				// User was not added
				return -1;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: room was not added.
			e.printStackTrace();
			return -2;
		} catch (SQLException e) {
			// SQL INSERT query failed: room was not added
			e.printStackTrace();
			return -2;
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return -2;
		}
	}

	/**
	 * Allows the given user to join a given chat room.
	 * 
	 * @param  userInfo     User's Information
	 * @param  chatRoomInfo ChatRooms's Information
	 * @return              Appropriate Chat Code denoting the result.
	 */
	public static ChatCode chatcodeJoinRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_JOIN_ROOM);
			preparedstatement.setString(1, userInfo.getUserName());
			preparedstatement.setString(2, chatRoomInfo.getId());

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// Uesr joined room
				return ChatCode.SUCCESS;
			} else {
				// User did not join room
				return ChatCode.FAILURE;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: User did not join room
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (SQLException e) {
			// SQL INSERT query failed: User did not join room
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return ChatCode.FAILURE;
		}
	}

	/**
	 * Removes a given user from a given chat room.
	 * 
	 * @param  userInfo     User's Information
	 * @param  chatRoomInfo ChatRooms's Information
	 * @return              Appropriate Chat Code denoting the result.
	 */
	public static ChatCode chatcodeLeaveRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_LEAVE_ROOM);
			preparedstatement.setString(1, userInfo.getUserName());
			preparedstatement.setString(2, chatRoomInfo.getId());

			// Execute the DELETE query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// User was removed from room
				return ChatCode.SUCCESS;
			} else {
				// User was not removed from room
				return ChatCode.FAILURE;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: User was not removed from room
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (SQLException e) {
			// SQL INSERT query failed: User was not removed from room
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return ChatCode.FAILURE;
		}
	}

	/**
	 * Posts a message to a chat room from a user.
	 * 
	 * @param  userInfo     User's Information
	 * @param  chatRoomInfo ChatRooms's Information
	 * @param  MessageInfo  Message's Information
	 * @return              Appropriate Chat Code denoting the result.
	 */
	public static ChatCode chatcodePostMessage(UserInfo userInfo, ChatRoomInfo chatRoomInfo, MessageInfo msgInfo) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_POST_MESSAGE);
			preparedstatement.setString(1, chatRoomInfo.getId());
			preparedstatement.setString(2, userInfo.getUserName());
			preparedstatement.setTimestamp(3, msgInfo.getTimeStamp());
			preparedstatement.setString(4, msgInfo.getBody());

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// Message was posted
				return ChatCode.SUCCESS;
			} else {
				// Message was not posted
				return ChatCode.FAILURE;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: Message was not posted
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (SQLException e) {
			// SQL query failed: Message was not posted
			e.printStackTrace();
			return ChatCode.FAILURE;
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return ChatCode.FAILURE;
		}
	}

	/**
	 * Retrieves the most recent chat messages from a given chatroom's chatlog since
	 * the given time and date.
	 * 
	 * 
	 * @param chatRoomInfo ChatRoom's Information
	 * @param timestamp    Messages to retrieve after the indicated timeStamp
	 * @return             ChatResultSet that has the most recent messages if successful(ChatCode), otherwise
	 *                     ChatResultSet's ChatCode reflects the appropriate error.
	 */
	public static ChatResultSet cResSetGetRecentChat(ChatRoomInfo chatRoomInfo, Timestamp timestamp) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_RECENT_MESSAGES, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setString(1, chatRoomInfo.getId());
			preparedstatement.setTimestamp(2, timestamp);

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();
			
			// Check for correct result
			if (resultset.next()) {
				// Messages retrieved: put all messages into a list
				List<MessageInfo> messages = new ArrayList<MessageInfo>();
				resultset.beforeFirst();
				while (resultset.next()) {
					String stUsername = resultset.getString("user_name");
					Timestamp timestampSent = resultset.getTimestamp("time_sent");
					String stContent = resultset.getString("content");
					messages.add(new MessageInfo(stContent, stUsername, timestampSent));
				}

				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return new ChatResultSet(messages, ChatCode.SUCCESS);
			} else {
				// Messages not retrieved
				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return new ChatResultSet(ChatCode.FAILURE);
			}
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		} catch (SQLException e) {
			// SQL  query failed: Messages not retrieved
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		}
	}

	/**
	 * Retrieves the entire chat log from a given chatroom and returns it in a ChatResultSet.
	 * 
	 * @param chatRoomInfo ChatRoom's Information
	 * @return             ChatResultSet that has the entire chat log if successful(ChatCode), otherwise
	 *                     ChatResultSet's ChatCode reflects the appropriate error.
	 */
	public static ChatResultSet cResSetGetEntireChat(ChatRoomInfo chatRoomInfo) {
		try {
			// Connect to the database and prepare the query
			Connection connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_ALL_MESSAGES, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setString(1, chatRoomInfo.getId());

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();

			// Check for correct result
			if (resultset.next()) {
				// Messages retrieved: put all messages into a list
				List<MessageInfo> messages = new ArrayList<MessageInfo>();
				resultset.beforeFirst();
				while (resultset.next()) {
					String stUsername = resultset.getString("user_name");
					Timestamp timestampSent = resultset.getTimestamp("time_sent");
					String stContent = resultset.getString("content");
					messages.add(new MessageInfo(stContent, stUsername, timestampSent));
				}

				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return new ChatResultSet(messages, ChatCode.SUCCESS);
			} else {
				// Messages not retrieved
				resultset.close();
				DatabaseUtility.closeConnection(connection);
				return new ChatResultSet(ChatCode.FAILURE);
			}
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
			e.printStackTrace();
			return new ChatResultSet(ChatCode.FAILURE);
		}
	}
}