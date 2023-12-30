package com.mahfuz.taskmanager.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {

	private Database() {
	}

	private static Connection conn = null;

	public static Connection getConnection() throws IOException {
		try {
			if (conn == null || conn.isClosed()) {
				Dotenv dotenv = Dotenv.configure().load();
				String dbUrl = dotenv.get("DB_URL");
				String dbUsername = dotenv.get("DB_USERNAME");
				String dbPassword = dotenv.get("DB_PASSWORD");

				Class.forName("com.mysql.cj.jdbc.Driver");

				conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Db errors\n");
			throw new CustomHttpException(500, e.getMessage());
		}
		return conn;
	}
}
