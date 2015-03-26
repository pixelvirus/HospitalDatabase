package database;

import java.sql.*;

import java.util.Observable;

/**
 * Database connection used by the GUI as a Singleton
 * @author devinli
 *
 */
public class DatabaseConnection extends Observable {
	
	private static DatabaseConnection instance;
	private Connection con;
	private String sqlQuery = "";
	private String tableName;
	private ResultSet resultSet;

	/*
	 * constructs login window and loads JDBC driver
	 */
	private DatabaseConnection() {
		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
	}
	
	public static DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		
		return instance;
	}

	public void quit() throws SQLException {
		if (con == null)
			return;
		con.close();
	}

	/*
	 * connects to Oracle database named ug using user supplied username and password
	 */
	public boolean connect(String username, String password) throws SQLException {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
		con = DriverManager.getConnection(connectURL, username, password);
		System.out.println("\nConnected to Oracle!");
		return true;
	}

	/**
	 * Send the SQL query to Oracle database
	 * @param query is the complete SQL as String
	 */
	public void shipSQLtoOracle(String query) {
		// execute query
		ResultSet resultSet = null;
		try {
			Statement statement = con.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			System.err.println(e.getErrorCode() + " " + e.getMessage());
		}

		this.resultSet = resultSet;
		if (resultSet != null) {
			setChanged();
			notifyObservers(resultSet);	
		}
	}
	
	public void addTableNameToQuery(String name) {
		tableName = name;
	}
	
	/**
	 * Show all in the table
	 */
	public void addSelectAllToQuery() {
		if (tableName == null)
			return;
		sqlQuery = "SELECT * FROM " + tableName;
		shipSQLtoOracle(sqlQuery);
		sqlQuery = "";
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}

}

