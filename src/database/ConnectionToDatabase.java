package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDatabase {
	public static Connection getConnectionDB() throws ClassNotFoundException {
		Connection conn = null;
    	String urlMySQL = "jdbc:mySQL://localhost:3306/quanlychungcumini";
    	String user = "root";
    	String password = "Lamcudo2206@";
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		conn = DriverManager.getConnection(urlMySQL, user, password);
    		return conn;
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
	}
	public static void closeConnection(Connection connection) {
		try {
			if(connection != null) {
				connection.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
