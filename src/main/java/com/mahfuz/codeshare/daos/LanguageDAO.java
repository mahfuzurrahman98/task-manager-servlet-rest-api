package com.mahfuz.codeshare.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mahfuz.codeshare.models.Language;
import com.mahfuz.codeshare.services.DBConnection;

public class LanguageDAO {
	private Connection conn = null;

	public LanguageDAO() throws IOException {
		conn = DBConnection.getConnection();
	}

	public ArrayList<Language> getAllLanguages() {
		ArrayList<Language> list = new ArrayList<Language>();

		String sql = "SELECT * from languages order by name";
		try {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				list.add(new Language(rs.getInt("id"), rs.getString("name")));
			}
		} catch (SQLException e) {
			System.out.println("DAO Error");
			e.printStackTrace();
		}
		return list;

	}
}
