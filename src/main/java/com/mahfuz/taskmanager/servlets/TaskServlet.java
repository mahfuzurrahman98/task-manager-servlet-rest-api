package com.mahfuz.taskmanager.servlets;

import java.io.IOException;
import java.util.HashMap;

import com.mahfuz.taskmanager.daos.TaskDAO;
import com.mahfuz.taskmanager.models.Task;
import com.mahfuz.taskmanager.utils.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// create a new task
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get the JSON data from the request body
			HashMap<String, Object> jsonData = JSON.read(request);

			if (jsonData == null) {
				JSON.respond(response, 400, "Invalid JSON data");
				return;
			}

			// if title is not a string or is not present in the jsonData or is empty
			// string, then return error
			if (!jsonData.containsKey("title") || !(jsonData.get("title") instanceof String)
					|| jsonData.get("title").toString().trim().isEmpty()) {
				JSON.respond(response, 400, "Invalid title");
				return;
			}

			// if description is not a string or is not present in the jsonData or is
			// empty string, then return error
			if (!jsonData.containsKey("description") || !(jsonData.get("description") instanceof String)
					|| jsonData.get("description").toString().trim().isEmpty()) {
				JSON.respond(response, 400, "Invalid description");
				return;
			}

			if (!jsonData.containsKey("user_id") || !(jsonData.get("user_id") instanceof Number)) {
				JSON.respond(response, 400, "Invalid user_id");
				return;
			}

			TaskDAO taskDao = new TaskDAO();
			// print the type of jsonData.get("user_id")
			System.out.println("Type:" + jsonData.get("user_id").getClass().getName()); // so its a java.lang.Double and not a
																																									// java.lang.Integer so we need to
																																									// cast it to int
			int userId = ((Number) jsonData.get("user_id")).intValue();

			Task existingTask = taskDao.getByUserIdAndTitle(userId,
					jsonData.get("title").toString());
			if (existingTask != null) {
				JSON.respond(response, 409, "Task with the same title already exists");
				return;
			}

			// create the task
			Task task = new Task(
					jsonData.get("title").toString(),
					jsonData.get("description").toString(),
					userId);
			task = taskDao.create(task);
			task.setTitle(null);
			System.out.println(
					"Task created with id: " + task.getId() + " and title: " + task.getTitle() + "at: " + task.getCreatedAt()
							+ " and updated at: " + task.getUpdatedAt());
			JSON.respond(response, 200, "Task created successfully", task);
		} catch (Exception e) {
			// Handle the exception
			e.printStackTrace();
			JSON.respond(response, 500, e.getMessage());
		}
	}
}
