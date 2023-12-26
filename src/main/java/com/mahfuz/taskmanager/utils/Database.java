package com.mahfuz.taskmanager.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

	private Database() {
	}

	private static Connection conn = null;

	public static Connection getConnection() throws IOException {
		try {
			if (conn == null || conn.isClosed()) {
				// Properties props = new Properties();
				// try (InputStream input =
				// Database.class.getClassLoader().getResourceAsStream("config.properties")) {
				// props.load(input);
				// }

				Class.forName("com.mysql.cj.jdbc.Driver");
				// conn = DriverManager.getConnection(
				// props.getProperty("db.url"),
				// props.getProperty("db.username"),
				// props.getProperty("db.password"));
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/taskmanager", "root", "pass9859");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Db errors\n");
		}
		return conn;
	}
}
