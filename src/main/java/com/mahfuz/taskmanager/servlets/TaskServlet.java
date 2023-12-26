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

			// if user_id is not a number or is not present in the jsonData or is empty
			// then return error
			if (!jsonData.containsKey("user_id") || !(jsonData.get("user_id") instanceof Number)) {
				JSON.respond(response, 400, "Invalid user_id");
				return;
			}

			// now check that the user_id has any existing task with the same title
			// if yes, then return error
			// if no, then create the task
			TaskDAO taskDao = new TaskDAO();
			int userId = Integer.parseInt(jsonData.get("user_id").toString());
			Task existingTask = taskDao.getByUserIdAndTitle(
					userId,
					jsonData.get("title").toString());
			if (existingTask != null) {
				JSON.respond(response, 409, "Task with the same title already exists");
				return;
			}

			// create the task
			Task task = new Task(
					jsonData.get("title").toString(),
					jsonData.get("description").toString(),
					Integer.parseInt(jsonData.get("user_id").toString()));
			JSON.respond(response, 200, "Task created successfully", jsonData);
		} catch (Exception e) {
			// Handle the exception
			e.printStackTrace();
			JSON.respond(response, 500, e.getMessage());
		}
	}
}
