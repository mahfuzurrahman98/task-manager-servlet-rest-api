package com.mahfuz.taskmanager.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mahfuz.taskmanager.models.User;
import com.mahfuz.taskmanager.utils.CustomHttpException;
import com.mahfuz.taskmanager.utils.Database;
import com.mahfuz.taskmanager.utils.Hash;

public class UserDAO implements DAO<User> {
    private Connection conn = null;

    public UserDAO() throws IOException {
        conn = Database.getConnection();
    }

    // public User checkLogin(String username, String password) {
    // User user = null;
    // String sql = "SELECT * FROM users WHERE username = ? and password = ?";
    // try {
    // PreparedStatement stmt = conn.prepareStatement(sql);
    // stmt.setString(1, username);
    // stmt.setString(2, password);
    // ResultSet rs = stmt.executeQuery();

    // while (rs.next()) {
    // user = new User(
    // rs.getInt("id"),
    // rs.getString("name"),
    // rs.getString("email"),
    // rs.getInt("status"));
    // }
    // } catch (SQLException e) {
    // // System.out.println("DAO Error");
    // e.printStackTrace();
    // }
    // return user;
    // }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("email"));
                user.setEmail(email);
                user.setPassword(null);
                user.setStatus(rs.getInt("status"));
                user.setCreatedAt(
                        rs.getTimestamp("created_at") == null ? "" : rs.getTimestamp("created_at").toString());
                user.setUpdatedAt(
                        rs.getTimestamp("updated_at") == null ? "" : rs.getTimestamp("updated_at").toString());
            }
        } catch (SQLException e) {
            System.out.println("DAO Error");
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        }
        return user;
    }

    @Override
    public User create(User user) {
        try {
            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, Hash.make(user.getPassword()));
            stmt.executeUpdate();

            // Retrieve the generated keys (ID)
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                user.setId(userId);
            }
            user = this.get(user.getId());
        } catch (SQLException e) {
            System.out.println("DAO Error");
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        }
        return user;
    }

    @Override
    public User get(int id) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            // if (!rs.next()) {
            //     throw new CustomHttpException(404, "User not found");
            // }

            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("email"));
                System.out.println("user-dao0:" + user);
                user.setId(id);
                user.setPassword(null);
                user.setStatus(rs.getInt("status"));
                user.setCreatedAt(
                        rs.getTimestamp("created_at") == null ? "" : rs.getTimestamp("created_at").toString());
                user.setUpdatedAt(
                        rs.getTimestamp("updated_at") == null ? "" : rs.getTimestamp("updated_at").toString());
            }
            System.out.println("user-dao1:" + user);
        } catch (SQLException e) {
            System.out.println("DAO Error");
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        }
        System.out.println("user-dao2:" + user);
        return user;
    }

    public ArrayList<User> index() throws SQLException {
        try {
            return this.index(new HashMap<String, String>());
        } catch (Exception e) {
            System.out.println("DAO Error");
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        }
    }

    @Override
    public ArrayList<User> index(HashMap<String, String> data) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        try {
            String sql = "SELECT * FROM users";
            if (data.containsKey("q")) {
                sql += " WHERE title LIKE ?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            if (data.containsKey("q")) {
                stmt.setString(1, "%" + data.get("q") + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setStatus(rs.getInt("status"));
                user.setCreatedAt(
                        rs.getTimestamp("created_at") == null ? "" : rs.getTimestamp("created_at").toString());
                user.setUpdatedAt(
                        rs.getTimestamp("updated_at") == null ? "" : rs.getTimestamp("updated_at").toString());
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("DAO Error");
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        }
        return users;
    }

    @Override
    public User update(int id, User t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
