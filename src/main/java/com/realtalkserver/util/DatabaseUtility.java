package com.realtalkserver.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Handles connecting to the database and executing queries.
 * @author Jory Rice
 *
 */
public class DatabaseUtility {

    protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * Establishes a connection to the database.
	 * @return connection	the database connection
	 * @throws URISyntaxException  		if the database url is incorrect 
	 * @throws SQLException 			if a connection error occurs
	 * @throws ClassNotFoundException 	if the driver can not be loaded
	 */
	public static Connection connectionGetConnection() throws URISyntaxException, SQLException, ClassNotFoundException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		System.err.println("db url returned successfully");

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		//String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
		
		System.err.println("db args parsed successfully");
		System.err.println("url: " + dbUrl);
		System.err.println("username: " + username);
		System.err.println("password: " + password);

		// Set up the connection. Make it commit after every statement.
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection(dbUrl, username, password);
		connection.setAutoCommit(true);
		return connection;
	}

	/**
	 * Executes the given query and returns the result.
	 * @param preparedStatementQuery	The query to run		
	 * @return The value returned from the query
	 * @throws SQLException 
	 * @throws URISyntaxException 
	 */
	public static ResultSet resultsetProcessQuery(PreparedStatement preparedStatementQuery) 
			throws SQLException {
		return preparedStatementQuery.executeQuery();
	}

	/**
	 * Closes the connection to the database.
	 * This should be called when the connection is no longer needed.
	 * @param connection 	the database connection to close
	 * @throws SQLException
	 */
	public static void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}
}
