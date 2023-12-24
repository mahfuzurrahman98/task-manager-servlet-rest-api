package com.mahfuz.codeshare.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mahfuz.codeshare.services.DBConnection;

public class SharedWithDAO {
	private Connection conn = null;

	public SharedWithDAO() throws IOException {
		conn = DBConnection.getConnection();
	}

	public void addSharedWith(int source_id, int shared_user_id) {

		String sql = "INSERT INTO shared_with(source_id, shared_user_id) VALUES(?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, source_id);
			stmt.setInt(2, shared_user_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			 System.out.println("DAO Error at share with");
			e.printStackTrace();
		}
	}
}