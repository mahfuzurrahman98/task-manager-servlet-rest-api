package com.mahfuz.taskmanager.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mahfuz.taskmanager.models.Task;
import com.mahfuz.taskmanager.utils.Database;

public class TaskDAO implements DAO<Task> {
  private Connection conn = null;

  public TaskDAO() throws IOException {
    conn = Database.getConnection();
  }

  @Override
  public Task create(Task task) throws SQLException {
    try {
      String sql = "INSERT INTO tasks (title, description, status, user_id) VALUES (?, ?, ?, ?)";

      // Use PreparedStatement.RETURN_GENERATED_KEYS to get the auto-generated ID
      PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
      stmt.setString(1, task.getTitle());
      stmt.setString(2, task.getDescription());
      stmt.setInt(3, task.getStatus());
      stmt.setInt(4, task.getUserId());
      stmt.executeUpdate();

      // Retrieve the generated keys (ID)
      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        // Set the generated ID, created_at, and updated_at values to the Task object
        int taskId = generatedKeys.getInt(1);
        // taskCreatedAt is a timestamp in the database, we need it as a string
        String taskCreatedAt = generatedKeys.getTimestamp("created_at").toString();

        task.setId(taskId);
        task.setCreatedAt(taskCreatedAt);
        task.setUpdatedAt("");
      }

    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return task;
  }

  @Override
  public ArrayList<Task> all() throws SQLException {
    ArrayList<Task> tasks = new ArrayList<Task>();
    try {
      String sql = "SELECT * FROM tasks";
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Task curTask = new Task(
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("user_id"));
        curTask.setId(rs.getInt("id"));
        curTask.setStatus(rs.getInt("status"));
        curTask.setCreatedAt(rs.getTimestamp("created_at").toString());
        curTask.setUpdatedAt(rs.getTimestamp("updated_at").toString());
        tasks.add(curTask);
      }
    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return tasks;

  }

  @Override
  public Task get(int id) throws SQLException {
    Task task = null;
    try {
      String sql = "SELECT * FROM tasks WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        task = new Task(
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("user_id"));
        task.setId(rs.getInt("id"));
        task.setStatus(rs.getInt("status"));
        task.setCreatedAt(rs.getTimestamp("created_at").toString());
        task.setUpdatedAt(rs.getTimestamp("updated_at").toString());
      }
    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return task;
  }

  @Override
  public Task update(int id, Task task) throws SQLException {
    Task updatedTask = null;
    try {
      String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, user_id = ? WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
      stmt.setString(1, task.getTitle());
      stmt.setString(2, task.getDescription());
      stmt.setInt(3, task.getStatus());
      stmt.setInt(4, task.getUserId());
      stmt.setInt(5, id);
      stmt.executeUpdate();

      updatedTask = this.get(id);

    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return updatedTask;
  }

  @Override
  public boolean delete(int id) throws SQLException {
    try {
      String sql = "DELETE FROM tasks WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, id);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return false;
  }

  // get tasks by user id
  public ArrayList<Task> getByUserId(int userId) throws SQLException {
    ArrayList<Task> tasks = new ArrayList<Task>();
    try {
      String sql = "SELECT * FROM tasks WHERE user_id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Task curTask = new Task(
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("user_id"));
        curTask.setId(rs.getInt("id"));
        curTask.setStatus(rs.getInt("status"));
        curTask.setCreatedAt(rs.getTimestamp("created_at").toString());
        curTask.setUpdatedAt(rs.getTimestamp("updated_at").toString());
        tasks.add(curTask);
      }
    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return tasks;
  }

  // get task by user id and title
  public Task getByUserIdAndTitle(int userId, String title) throws SQLException {
    Task task = null;
    try {
      String sql = "SELECT * FROM tasks WHERE user_id = ? AND title = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      stmt.setString(2, title);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        task = new Task(
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("user_id"));
        task.setId(rs.getInt("id"));
        task.setStatus(rs.getInt("status"));
        task.setCreatedAt(rs.getTimestamp("created_at").toString());
        task.setUpdatedAt(rs.getTimestamp("updated_at").toString());
      }
    } catch (SQLException e) {
      System.out.println("DAO Error");
      e.printStackTrace();
    }
    return task;
  }
}
