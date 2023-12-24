package com.mahfuz.codeshare.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.mahfuz.codeshare.models.SourceCode;
import com.mahfuz.codeshare.services.DBConnection;

public class SourceCodeDAO {
	private Connection conn = null;

	public SourceCodeDAO() throws IOException {
		conn = DBConnection.getConnection();
	}

	public void addSourceCode(String title, int language, int visibility, String source, int created_by,
			String created_by_alt, String created_at, String expire_at, int status, String[] share_with)
			throws IOException {
		String sql = "INSERT INTO source_codes(title, language_id, visibility, code, created_by, created_by_alt, created_at, expire_at, status) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, title);
			stmt.setInt(2, language);
			stmt.setInt(3, visibility);
			stmt.setString(4, source);
			stmt.setInt(5, created_by);
			stmt.setString(6, created_by_alt);
			stmt.setString(7, created_at);
			if (expire_at != null) {
				stmt.setString(8, expire_at);
			} else {
				stmt.setNull(8, Types.NULL);
			}
			stmt.setInt(9, status);
			int res = stmt.executeUpdate();
			System.out.println("res: " + res);

			if (visibility == 3) {
				SharedWithDAO shared_with_dao = new SharedWithDAO();
				ResultSet generatedKeys = stmt.getGeneratedKeys();

				if (generatedKeys.next()) {
					int last_inserted_id = generatedKeys.getInt(1);
					for (String user_id : share_with) {
						shared_with_dao.addSharedWith(last_inserted_id, Integer.parseInt(user_id));
					}
				}
			}
			System.out.println("inserted...");
		} catch (SQLException e) {
			System.out.println("DAO Error at source code");
			e.printStackTrace();
		}
	}

	public int getVisibilityByID(int id) throws IOException {
		int visibility = 0;
		String sql = "SELECT visibility WHERE source_codes.id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			visibility = rs.getInt("visibility");
		} catch (SQLException e) {
			System.out.println("DAO Error at source code...");
			e.printStackTrace();
		}
		return visibility;
	}

	public SourceCode getDetailsByID(int id) throws IOException {
		SourceCode details = null;
		String sql = "SELECT source_codes.*, users.name AS created_by_name, languages.name AS language, GROUP_CONCAT(shared_with.shared_user_id) AS shared_persons FROM source_codes JOIN languages ON source_codes.language_id = languages.id LEFT JOIN users ON source_codes.created_by = users.id LEFT JOIN shared_with ON source_codes.id = shared_with.source_id WHERE source_codes.id = ? AND source_codes.status = 1 GROUP BY source_codes.id";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
			rs.next();

			int[] sharedPersons = new int[] {};
			if (rs.getObject("Shared_Persons") != null) {
				String[] sharedPersonsStr = rs.getString("Shared_Persons").split(",");
				sharedPersons = new int[sharedPersonsStr.length];

				for (int i = 0; i < sharedPersonsStr.length; i++) {
					sharedPersons[i] = Integer.parseInt(sharedPersonsStr[i]);
				}
			}

			details = new SourceCode(
					rs.getInt("Id"),
					rs.getString("Title"),
					rs.getString("Language"),
					rs.getString("Code"), rs.getInt("Visibility"), rs.getInt("createdBy"),
					rs.getObject("created_by_name") != null ? rs.getString("created_by_name") : "",
					rs.getObject("created_by_alt") != null ? rs.getString("created_by_alt") : "",
					sharedPersons,
					rs.getString("created_at"),
					rs.getString("expire_at"),
					rs.getInt("Status"));
		} catch (SQLException e) {
			System.out.println("DAO Error at source code...");
			e.printStackTrace();
		}
		return details;
	}

	public ArrayList<SourceCode> getLibraryByUser(int user_id) throws IOException {
		ArrayList<SourceCode> library = new ArrayList<SourceCode>();
		String sql = "SELECT source_codes.*, users.name AS created_by_name, languages.name AS language, GROUP_CONCAT(shared_with.shared_user_id) AS shared_persons FROM source_codes JOIN languages ON source_codes.language_id = languages.id LEFT JOIN users ON source_codes.created_by = users.id LEFT JOIN shared_with ON source_codes.id = shared_with.Source_Id WHERE source_codes.created_by = ? AND source_codes.status != 3 GROUP BY source_codes.id";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, user_id);
			ResultSet rs = stmt.executeQuery();
			System.out.println("fetchsize: " + stmt.getFetchSize());
			while (rs.next()) {
				int[] sharedPersons = new int[] {};
				if (rs.getObject("Shared_Persons") != null) {
					String[] sharedPersonsStr = rs.getString("shared_persons").split(",");
					sharedPersons = new int[sharedPersonsStr.length];
					for (int i = 0; i < sharedPersonsStr.length; i++) {
						sharedPersons[i] = Integer.parseInt(sharedPersonsStr[i]);
					}
				}
				library.add(
						new SourceCode(
								rs.getInt("id"),
								rs.getString("title"),
								rs.getString("language"),
								rs.getString("code"),
								rs.getInt("visibility"),
								rs.getInt("createdBy"),
								rs.getObject("created_by_name") != null ? rs.getString("created_by_name") : "",
								rs.getObject("created_by_alt") != null ? rs.getString("created_by_alt") : "",
								sharedPersons,
								rs.getString("created_at"), rs.getString("expire_at"), rs.getInt("status")));
			}
		} catch (SQLException e) {
			System.out.println("DAO Error at source code...");
			e.printStackTrace();
		}
		return library;
	}

	public ArrayList<SourceCode> getSharedSourceByUser(int user_id) throws IOException {
		ArrayList<SourceCode> source_list = new ArrayList<SourceCode>();
		String sql = "SELECT source_codes.*, languages.name AS language, users.name AS created_by_name FROM shared_with JOIN source_codes ON shared_with.source_id = source_codes.id JOIN users ON source_codes.created_by = users.id JOIN languages ON source_codes.language_id = languages.id WHERE shared_with.shared_user_id = ? AND source_codes.status = 1";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, user_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				source_list.add(new SourceCode(
						rs.getInt("id"),
						rs.getString("title"),
						rs.getString("language"),
						rs.getString("code"),
						rs.getInt("visibility"),
						rs.getInt("created_by"),
						rs.getObject("created_by_name") != null ? rs.getString("created_by_name") : "",
						rs.getObject("created_by_alt") != null ? rs.getString("created_by_alt") : "", new int[] {},
						rs.getString("created_at"),
						rs.getString("expire_at"),
						rs.getInt("status")));
			}
		} catch (SQLException e) {
			System.out.println("DAO Error at source code...");
			e.printStackTrace();
		}

		return source_list;
	}

	public void changeStatus(int id) {
		String sql = "UPDATE source_codes set status= CASE WHEN status=1 then 2 else 1 END where source_codes.id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DAO Error at source code...");
			e.printStackTrace();
		}
	}
}
