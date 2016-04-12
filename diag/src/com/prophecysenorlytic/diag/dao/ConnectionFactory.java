package com.prophecysenorlytic.diag.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public static void main(String[] args) {
		System.out.println(getConnection());
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://104.130.225.224:3306/heartbeat", "avijit", "Gr@p3v1n3");
			System.out.println("Connected...");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection connection) {
		try {
			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
