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
	private String tableName;
	private ResultSet resultSet;

	/*
	 * constructs login window and loads JDBC driver
	 */
	private DatabaseConnection() throws SQLException {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}

	public static DatabaseConnection getInstance() throws SQLException {
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
	public boolean connect(String username, String password) 
			throws SQLException {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
		con = DriverManager.getConnection(connectURL, username, password);
		return true;
	}

	/**
	 * Send the SQL query to Oracle database
	 * @param query is the complete SQL as String
	 */
	public void shipSQLtoOracle(String query)  throws SQLException {
		// execute query
		ResultSet resultSet = null;
		Statement statement = con.createStatement();
		resultSet = statement.executeQuery(query);

		this.resultSet = resultSet;
		if (resultSet != null) {
			setChanged();
			notifyObservers(resultSet);	
		} else {
			throw new SQLException("Received null!");
		}
	}

	public void addTableNameToQuery(String name) {
		tableName = name;
	}

	/**
	 * Show all in the table
	 * @throws SQLException 
	 */
	public void addSelectAllToQuery() throws SQLException {
		if (tableName == null)
			return;
		String query = "SELECT * FROM " + tableName;
		shipSQLtoOracle(query);
	}

	public void billPatient(int pid) throws SQLException {
		String query = "SELECT Sum(P.cost) " 
				+ "FROM Procedures P, Performs F "
				+ "WHERE pt_id='" + pid + "' AND P.proc_id=F.proc_id";
		shipSQLtoOracle(query);
	}

	public void findSpec(String spec) throws SQLException {
		String query = "SELECT do_name FROM Doctors WHERE specialization='" 
				+ spec + "'";
		shipSQLtoOracle(query);
	}

	public void findAveMedCost() throws SQLException {
		String query = "SELECT AVG(M.cost) FROM Medications M, Prescribes P " 
				+ "WHERE P.med_id=M.med_id " 
				+ "GROUP BY P.med_id " 
				+ "HAVING 4<COUNT(DISTINCT P.pt_id)";
		shipSQLtoOracle(query);
	}

	public void findAllORDocs() throws SQLException {
		String query = "SELECT do_name FROM Doctors D WHERE NOT EXISTS "
				+ "(SELECT O.oproom_id FROM OperatingRooms O WHERE NOT EXISTS "
				+ "(SELECT O.oproom_id FROM Performs P WHERE P.do_id=D.do_id AND P.oproom_id=O.oproom_id))";
		shipSQLtoOracle(query);
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

}

