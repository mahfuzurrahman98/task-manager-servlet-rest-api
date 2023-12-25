package com.mahfuz.codeshare.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mahfuz.codeshare.models.Task;
import com.mahfuz.codeshare.utils.Database;

public class TaskDAO {
  private Connection conn = null;

  public TaskDAO() throws IOException {
    conn = Database.getConnection();
  }

  public ArrayList<Task> getAllTasks() {

    ArrayList<Task> tasks = new ArrayList<Task>();
    String sql = "SELECT * from tasks";
    try {
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        tasks.add(
            new Task(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getInt("status")));
      }
    } catch (SQLException e) {
      // System.out.println("DAO Error");
      e.printStackTrace();
    }

    return tasks;

  }

  // public ArrayList<Task> getTasksByTaskId(int userId) {
  // }

}
