package allianceToolDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import allianceTool.errorHandler.DbException;

public class DbConnection {
	private static String HOST = "localhost";
	private static String PASSWORD = "stfc_alliance_tool";
	private static int PORT = 3306;
	private static String SCHEMA  = "stfc_alliance_tool";
	private static String USER  = "stfc_alliance_tool";

	public static java.sql.Connection getConnection(){
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);
		
		try {
			Connection conn = DriverManager.getConnection(uri);
		//	System.out.println("Connection to schema '" + SCHEMA + "' is successful.");
			return conn;
		} catch (SQLException e) {
			System.out.println("Unable to get connection at" + uri);
			throw new DbException("Unable to get connection at" + uri);
		}		
	}
	
	
}
