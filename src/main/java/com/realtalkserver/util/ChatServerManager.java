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
	public static int addRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_ADD_ROOM);
			preparedstatement.setString(1, chatRoomInfo.getName());
			preparedstatement.setString(2, chatRoomInfo.getDescription());
			preparedstatement.setTimestamp(3, chatRoomInfo.getTimeStampCreated());
			preparedstatement.setString(4, chatRoomInfo.getCreator());
			preparedstatement.setDouble(5, chatRoomInfo.getLatitude());
			preparedstatement.setDouble(6, chatRoomInfo.getLongitude());

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// Room was added
				return 0;
			} else {
				// Room was not added
				return -1;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: room was not added.
		} catch (SQLException e) {
			// SQL INSERT query failed: room was not added
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return -2;
	}

	/**
	 * Allows the given user to join a given chat room.
	 * 
	 * @param  userInfo     User's Information
	 * @param  chatRoomInfo ChatRooms's Information
	 * @return              Appropriate Chat Code denoting the result.
	 */
	public static ChatCode chatcodeJoinRoom(UserInfo userInfo, ChatRoomInfo chatRoomInfo, boolean fAnon) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			
			String stTableAlias;
			if (fAnon) {
				// Users's alias will be a numbered anonymous username
				stTableAlias = stGetAnonymousUsername(connection, userInfo, chatRoomInfo);
			} else {
				// User's alias will be their standard username
				stTableAlias = userInfo.getUserName();
			}

			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_JOIN_ROOM);
			preparedstatement.setString(1, userInfo.getUserName());
			preparedstatement.setInt(2, chatRoomInfo.getId());
			preparedstatement.setString(3, stTableAlias);

			// Execute the INSERT query
			int result = preparedstatement.executeUpdate();
			DatabaseUtility.closeConnection(connection);

			// Check for correct result
			if (result == 1) {
				// User joined room
				return ChatCode.SUCCESS;
			} else {
				// User did not join room
				return ChatCode.FAILURE;
			}
		} catch (URISyntaxException e) {
			// Database connection failed: User did not join room
		} catch (SQLException e) {
			// SQL INSERT query failed: User did not join room
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return ChatCode.FAILURE;
	}

	private static String stGetAnonymousUsername(Connection connection, 
			UserInfo userInfo, ChatRoomInfo cri) throws SQLException {
		// Prepare the query
		PreparedStatement preparedstatementSelect = connection.prepareStatement(SQLQueries.QUERY_GET_ROOM_INFO, 
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		preparedstatementSelect.setInt(1, cri.getId());
		
		// Execute the SELECT query
		ResultSet resultsetSelect = preparedstatementSelect.executeQuery();
		
		String stUserAlias = "Anon";
		if (resultsetSelect.next()) {
			// Get the username
			int anonCount = resultsetSelect.getInt("anon_count");
			stUserAlias += (anonCount + 1);
			preparedstatementSelect.close();
			resultsetSelect.close();
			
			// Increment the count of anons for the table
			PreparedStatement preparedstatementUpdate = connection.prepareStatement(
					SQLQueries.QUERY_INCREMENT_ROOM_ANON_COUNT, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatementUpdate.setInt(1, cri.getId());
			preparedstatementUpdate.executeUpdate();
			preparedstatementUpdate.close();
			return stUserAlias;
		} else {
			// 0 rows: query failed
			throw new SQLException();
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
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_LEAVE_ROOM);
			preparedstatement.setString(1, userInfo.getUserName());
			preparedstatement.setInt(2, chatRoomInfo.getId());

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
		} catch (SQLException e) {
			// SQL INSERT query failed: User was not removed from room
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return ChatCode.FAILURE;
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
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_POST_MESSAGE);
			preparedstatement.setInt(1, chatRoomInfo.getId());
			preparedstatement.setString(2, stGetUserAlias(connection, userInfo, chatRoomInfo));
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
		} catch (SQLException e) {
			// SQL query failed: Message was not posted
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return ChatCode.FAILURE;
	}

	private static String stGetUserAlias(Connection connection, 
			UserInfo userInfo, ChatRoomInfo cri) throws SQLException {
		// Prepare the query
		PreparedStatement preparedstatementSelect = connection.prepareStatement(SQLQueries.QUERY_GET_ACTIVE_USER_INFO, 
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		preparedstatementSelect.setString(1, userInfo.getUserName());
		preparedstatementSelect.setInt(2, cri.getId());
		
		// Execute the SELECT query
		ResultSet resultsetSelect = preparedstatementSelect.executeQuery();
		
		if (resultsetSelect.next()) {
			// Get the username
			String stUserAlias = resultsetSelect.getString("user_alias");
			preparedstatementSelect.close();
			resultsetSelect.close();
			return stUserAlias;
		} else {
			// 0 rows: query failed
			throw new SQLException();
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
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_RECENT_MESSAGES, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setInt(1, chatRoomInfo.getId());
			preparedstatement.setTimestamp(2, timestamp);

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();
			
			// Messages retrieved: put all messages into a list
			List<MessageInfo> rgmessageinfo = new ArrayList<MessageInfo>();
			while (resultset.next()) {
				String stUsername = resultset.getString("user_name");
				Timestamp timestampSent = resultset.getTimestamp("time_sent");
				String stContent = resultset.getString("content");
				rgmessageinfo.add(new MessageInfo(stContent, stUsername, timestampSent));
			}

			resultset.close();
			DatabaseUtility.closeConnection(connection);
			return new ChatResultSet(rgmessageinfo, ChatCode.SUCCESS);
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL  query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return new ChatResultSet(ChatCode.FAILURE);
	}

	/**
	 * Retrieves the entire chat log from a given chatroom and returns it in a ChatResultSet.
	 * 
	 * @param chatRoomInfo ChatRoom's Information
	 * @return             ChatResultSet that has the entire chat log if successful(ChatCode), otherwise
	 *                     ChatResultSet's ChatCode reflects the appropriate error.
	 */
	public static ChatResultSet cResSetGetEntireChat(ChatRoomInfo chatRoomInfo) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_ALL_MESSAGES, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setInt(1, chatRoomInfo.getId());

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();

			// Messages retrieved: put all messages into a list
			List<MessageInfo> rgmessageinfo = new ArrayList<MessageInfo>();
			while (resultset.next()) {
				String stUsername = resultset.getString("user_name");
				Timestamp timestampSent = resultset.getTimestamp("time_sent");
				String stContent = resultset.getString("content");
				rgmessageinfo.add(new MessageInfo(stContent, stUsername, timestampSent));
			}

			resultset.close();
			DatabaseUtility.closeConnection(connection);
			return new ChatResultSet(rgmessageinfo, ChatCode.SUCCESS);
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return new ChatResultSet(ChatCode.FAILURE);
	}

	/**
	 * Retrieves all room within the given radius of the given coordinates.
	 * 
	 * @param latitude    latitude of the origin point
	 * @param longitude   longitude of the origin point
	 * @param radiusMeters      radius to search within
	 * @return            A ChatroomResultSet containing rooms and success/error messages 
	 */
	public static ChatroomResultSet crrsNearbyRooms(double latitude, double longitude, double radiusMeters) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_ALL_ROOMS);

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();

			// Rooms retrieved
			List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
			while (resultset.next()) {
				// Check to see if the room is within the given radius 
				double roomLatitude = resultset.getDouble("latitude");
				double roomLongitude = resultset.getDouble("longitude");
				if (LocationLogic.distance(latitude, longitude, roomLatitude, roomLongitude) < radiusMeters) {
					// Add the room with all info about it
					String stName = resultset.getString("room_name");
					int id = resultset.getInt("room_id");
					String stDesc = resultset.getString("room_desc");
					String stCreator = resultset.getString("creator_name");
					Timestamp timestampCreated = resultset.getTimestamp("time_created");
					int numUsers = numUsers(connection, new ChatRoomInfo(
							stName, id, stDesc, roomLatitude, roomLongitude, stCreator, 0, timestampCreated));
					rgcri.add(new ChatRoomInfo(
							stName, id, stDesc, roomLatitude, roomLongitude, stCreator, numUsers, timestampCreated));
				}
			}
			
			// Sort: closest rooms are first
			rgcri = LocationLogic.rgcriSortByProximity(rgcri, latitude, longitude);
			
			resultset.close();
			DatabaseUtility.closeConnection(connection);
			return new ChatroomResultSet(rgcri, ChatCode.SUCCESS);
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return new ChatroomResultSet(null, ChatCode.FAILURE);
	}
	
	/**
	 * Retrieves all rooms that the given user has joined.
	 * 
	 * @param userInfo    The user
	 * @return            A ChatroomResultSet containing rooms and success/error messages 
	 */
	public static ChatroomResultSet crrsJoinedRooms(UserInfo userInfo) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_JOINED_ROOMS,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setString(1, userInfo.getUserName());

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();

			// Rooms retrieved
			List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
			while (resultset.next()) {
				// Add the room with all info about it
				String stName = resultset.getString("r_room_name");
				int id = resultset.getInt("r_room_id");
				String stDesc = resultset.getString("r_room_desc");
				String stCreator = resultset.getString("r_creator_name");
				Timestamp timestampCreated = resultset.getTimestamp("r_time_created");
				double roomLatitude = resultset.getDouble("r_latitude");
				double roomLongitude = resultset.getDouble("r_longitude");
				int numUsers = numUsers(connection, new ChatRoomInfo(
						stName, id, stDesc, roomLatitude, roomLongitude, stCreator, 0, timestampCreated));
				rgcri.add(new ChatRoomInfo(
						stName, id, stDesc, roomLatitude, roomLongitude, stCreator, numUsers, timestampCreated));
			}
			
			resultset.close();
			DatabaseUtility.closeConnection(connection);
			return new ChatroomResultSet(rgcri, ChatCode.SUCCESS);
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return new ChatroomResultSet(null, ChatCode.FAILURE);
	}

	/**
	 * Retrieves a list of all registration IDs associated with active users
	 * in the given chat room.
	 * 
	 * @param cri      The chat room
	 * @return         Registration IDs of all users in a given room
	 */
	public static List<String> rgstGetRegistrationIds(ChatRoomInfo cri) {
		// Retrieve all active users in the room
		List<UserInfo> rgUser = rguserinfoGetRoomUsers(cri);
		
		// Just return a list of registration IDs
		List<String> rgRegIds = new ArrayList<String>();
		for (UserInfo userinfo : rgUser) {
			rgRegIds.add(userinfo.getRegistrationId());
		}
		return rgRegIds;
	}

	/**
	 * Returns the number of active users in a given chat room.
	 * 
	 * @param cri      The chat room
	 * @return         Number of users in that room, or -1 if an error occured
	 */
	public static int numUsers(ChatRoomInfo cri) {
		// Get all users in the room
		List<UserInfo> rgUser = rguserinfoGetRoomUsers(cri);
		if (rgUser == null) {
			return -1;
		}
			
		// Return count of users in room
		return rgUser.size();
	}
	
	/**
	 * Returns the number of active users in a given chat room.
	 * Uses the given database connection.
	 * 
	 * @param connection    DB connection to use
	 * @param cri           The room
	 * @return              Number of uses in the room
	 */
	private static int numUsers(Connection connection, ChatRoomInfo cri) {
		List<UserInfo> rgUser;
		try {
			// Get all users in the room
			rgUser = rguserinfoGetRoomUsers(connection, cri);
			if (rgUser == null) {
				return -1;
			}
				
			// Return count of users in list
			return rgUser.size();
		} catch (SQLException e) {
			return -1;
		}
	}

	/**
	 * Retrieves a list of all active users in the given room.
	 * 
	 * @param cri      The chat room
	 * @return         Users in that room, or null if an error occurred
	 */
	public static List<UserInfo> rguserinfoGetRoomUsers(ChatRoomInfo cri) {
		Connection connection = null;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			List<UserInfo> rgu = rguserinfoGetRoomUsers(connection, cri);
			DatabaseUtility.closeConnection(connection);
			return rgu;
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return null;
	}

	/**
	 * Retrieves all user from the given room. Uses the given database connection.
	 * 
	 * @param connection      DB connection to use
	 * @param cri             The room
	 * @return                Active users in the room
	 * @throws SQLException
	 */
	private static List<UserInfo> rguserinfoGetRoomUsers(Connection connection, ChatRoomInfo cri) throws SQLException {
		// Prepare the query
		PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_ROOM_USERS, 
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		preparedstatement.setInt(1, cri.getId());
		
		// Execute the SELECT query
		ResultSet resultset = preparedstatement.executeQuery();
		
		// Users retrieved: put all users into a list
		List<UserInfo> rguserinfo = new ArrayList<UserInfo>();
		while (resultset.next()) {
			String stUsername = resultset.getString("u_user_name");
			String stPassword = resultset.getString("u_password");
			String stRegId = resultset.getString("u_device_id");
			rguserinfo.add(new UserInfo(stUsername, stPassword, stRegId));
		}

		preparedstatement.close();
		resultset.close();
		return rguserinfo;
	}

	/**
	 * Returns the user's alias in the given room.
	 * 
	 * @param stSenderUserName
	 * @param chatRoomInfo
	 * @return
	 */
	public static String stGetMessageSenderAlias(String stSenderUserName,
			ChatRoomInfo chatRoomInfo) {
		Connection connection = null;
		String stUserAlias = stSenderUserName;
		try {
			// Connect to the database and prepare the query
			connection = DatabaseUtility.connectionGetConnection();
			PreparedStatement preparedstatement = connection.prepareStatement(SQLQueries.QUERY_GET_TABLE_ALIAS, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedstatement.setString(1, stSenderUserName);

			// Execute the SELECT query
			ResultSet resultset = preparedstatement.executeQuery();

			// Get the name
			if (resultset.next()) {
				stUserAlias = resultset.getString("user_alias");
			}

			resultset.close();
		} catch (URISyntaxException e) {
			// Database connection failed: Messages not retrieved
		} catch (SQLException e) {
			// SQL query failed: Messages not retrieved
		} catch (ClassNotFoundException e) {
			// Postgresql driver error
		}
		DatabaseUtility.closeConnection(connection);
		return stUserAlias;
	}
}
