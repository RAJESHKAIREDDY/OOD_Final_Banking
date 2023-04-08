package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import enums.DBConnectionStatus;

public class DBInstance {
	private static Connection connection;
	private static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/safebankDB";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "HARSHperi@96";

	protected static void getConnection() {

		try {
			Class.forName(MYSQL_JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException classNotFoundException) {
			// TODO: handle exception
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, classNotFoundException);
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, sqlException);
		}
	}

	protected static void closeConnection() {
		try {
			connection.close();

		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, sqlException);
		}
	}

	protected static void testConnection() {
		if(connection != null) 
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, DBConnectionStatus.DB_CONNECTION_SUCCESSFUL);
		else
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, DBConnectionStatus.DB_CONNECTION_FAILED);
	}
	
	protected static boolean executeUpdate(String query) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			getConnection();
			preparedStatement = connection.prepareStatement(query);
			result = preparedStatement.executeUpdate();
		} 
		catch (SQLException sqlException) {
			// TODO: handle exception
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		catch (Exception exception) {
			// TODO: handle exception
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, exception);
		}
		finally {
			closeConnection();
		}
		if(result == 0)
			return false;
		return true;
	}
	
	protected static ResultSet executeQuery(String query) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
		} 
		catch (SQLException sqlException) {
			// TODO: handle exception
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, sqlException);
		}
		catch (Exception exception) {
			// TODO: handle exception
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, exception);
		}
		return resultSet; 
	}
}
