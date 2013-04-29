package com.realtalkserver.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles connecting to the database and executing queries.
 * @author Jory Rice
 *
 */
public class DatabaseUtility {
	
	private Connection connection;
	
	/**
	 * Establishes a connection to the database.
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
	public DatabaseUtility() throws URISyntaxException, SQLException {
		this.connection = connectionGetConnection();
	}
	
	/**
	 * Returns a connection to the database.
	 * @return
	 * @throws URISyntaxException 
	 * @throws SQLException 
	 */
	private Connection connectionGetConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));
	    
	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + "/" + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
	/**
	 * Executes the given query and returns the result.
	 * @param stQuery
	 * @return result
	 * @throws SQLException 
	 * @throws URISyntaxException 
	 */
	public ResultSet resultsetProcessQuery(String stQuery) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(stQuery);
		return result;
	}
	
	/**
	 * Closes the connection to the database.
	 * This should be called when the connection is no longer needed.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		connection.close();
	}
}
