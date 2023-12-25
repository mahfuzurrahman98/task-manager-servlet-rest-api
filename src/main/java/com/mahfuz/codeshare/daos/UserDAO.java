package com.mahfuz.codeshare.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mahfuz.codeshare.models.User;
import com.mahfuz.codeshare.utils.Database;

public class UserDAO {
	private Connection conn = null;

	public UserDAO() throws IOException {
		conn = Database.getConnection();
	}

	public User checkLogin(String username, String password) {
		User user = null;
		String sql = "SELECT * from users where username = ? and password = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				user = new User(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("email"),
						rs.getInt("status"));
			}
		} catch (SQLException e) {
			// System.out.println("DAO Error");
			e.printStackTrace();
		}
		return user;
	}

	public ArrayList<User> getAllUsers(int user_id) {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * from users where Id != ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, user_id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				users.add(
						new User(
								rs.getInt("id"),
								rs.getString("name"),
								rs.getString("email"),
								rs.getInt("status")));
			}
		} catch (SQLException e) {
			// System.out.println("DAO Error");
			e.printStackTrace();
		}

		return users;
	}
}
