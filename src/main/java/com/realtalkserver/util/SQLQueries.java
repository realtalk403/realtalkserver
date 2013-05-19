package com.realtalkserver.util;

/**
 * This class defines String constants for all necessary SQL queries.
 * In these strings, question marks are replaced by parameters to execute queries.
 * @author Jory Rice
 */
public class SQLQueries {

	// User queries
	public static final String QUERY_ADD_USER = 
			"INSERT " +
			"INTO users " +
			"values(?, ?, ?);";

	public static final String QUERY_LEAVE_ALL_ROOMS = 
			"DELETE " +
			"FROM active_users " +
			"WHERE user_name = ?; ";

	public static final String QUERY_REMOVE_USER =
			"DELETE " +
			"FROM users " +
			"WHERE user_name = ?;";

	public static final String QUERY_CHANGE_PASSWORD = 
			"UPDATE users " +
			"SET password = ? " +
			"WHERE user_name = ?;";

	public static final String QUERY_CHANGE_ID = 
			"UPDATE users " +
			"SET device_id = ? " +
			"WHERE user_name = ?;";

	public static final String QUERY_AUTHENTICATE = 
			"SELECT * " +
			"FROM users " +
			"WHERE user_name = ? " +
			"AND password = ?;";

	// Room queries
	public static final String QUERY_ADD_ROOM = 
			"INSERT " +
			"INTO rooms (room_name, room_desc, time_created, creator_name, latitude, longitude) " +
			"values(?, ?, ?, ?, ?, ?);";

	public static final String QUERY_JOIN_ROOM = 
			"INSERT " +
			"INTO active_users " +
			"values(?, ?);";

	public static final String QUERY_LEAVE_ROOM = 
			"DELETE " +
			"FROM active_users " +
			"WHERE user_name = ? " +
			"AND room_id = ?;";

	public static final String QUERY_POST_MESSAGE = 
			"INSERT " +
			"INTO messages " +
			"values(?, ?, ?, ?);";

	public static final String QUERY_GET_RECENT_MESSAGES = 
			"SELECT user_name, time_sent, content " +
			"FROM messages " +
			"WHERE room_id = ? " +
			"AND time_sent > ? " +
			"ORDER BY time_sent DESC;";

	public static final String QUERY_GET_ALL_MESSAGES = 
			"SELECT user_name, time_sent, content " +
			"FROM messages " +
			"WHERE room_id = ? " +
			"ORDER BY time_sent DESC;";

	public static final String QUERY_GET_ALL_ROOMS = 
			"SELECT *" +
			"FROM rooms;";

	public static final String QUERY_GET_ROOM_USERS = 
			"SELECT DISTINCT " +
			"u.user_name AS u_user_name, " +
			"u.device_id AS u_device_id, " +
			"u.password AS u_password " +
			"FROM active_users a, users u " +
			"WHERE a.room_id = ? " +
			"AND a.user_name = u.user_name;";
	
	public static final String QUERY_GET_JOINED_ROOMS = 
			"SELECT " +
			"r.room_id AS r_room_id, " +
			"r.room_name AS r_room_name, " +
			"r.room_desc AS r_room_desc, " +
			"r.time_created AS r_time_created, " +
			"r.creator_name AS r_creator_name, " +
			"r.latitude AS r_latitude, " +	
			"r.longitude AS r_longitude " +	
			"FROM active_users a, rooms r " +
			"WHERE a.room_id = r.room_id " +
			"AND a.user_name = ?;";
	
}
