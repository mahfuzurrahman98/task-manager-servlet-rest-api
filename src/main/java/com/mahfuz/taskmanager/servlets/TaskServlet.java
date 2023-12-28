package com.mahfuz.taskmanager.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mahfuz.taskmanager.daos.TaskDAO;
import com.mahfuz.taskmanager.models.Task;
import com.mahfuz.taskmanager.utils.CustomHttpException;
import com.mahfuz.taskmanager.utils.JSON;
import com.mahfuz.taskmanager.utils.Request;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TaskServlet", urlPatterns = { "/tasks", "/tasks/*", "/tasks/user/*" })
public class TaskServlet extends HttpServlet {
    private String baseURL = "/taskmanager/tasks";
    private static final long serialVersionUID = 1L;
    private TaskDAO taskDao;
    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config); // Call the init method of the parent class
            this.config = config;
            this.taskDao = new TaskDAO();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error initializing servlet");
        }
    }

    // get tasks
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();

            // possible URI patterns:
            /*
             * 1) tasks/user/:id
             * 2) tasks/:id
             * 3) tasks/?q=abc
             * 4) tasks
             */

            // tasks/?q=abc&status=1&so=on&xyz=abc
            if (requestURI.startsWith(baseURL + "/") && request.getQueryString() != null) {
                // tasks with query params
                HashMap<String, String> queryParams = Request.getQueryParams(request);
                ArrayList<Task> tasks = this.taskDao.index(queryParams);
                JSON.respond(response, 200, "All tasks with query params", tasks);
                return;
            }

            // tasks
            if (requestURI.equals(baseURL)) {
                // all tasks
                ArrayList<Task> tasks = this.taskDao.index();
                JSON.respond(response, 200, "All tasks", tasks);
                return;
            }

            // tasks/user/:id
            if (requestURI.startsWith(baseURL + "/user/")) {
                // all tasks of a user
                String[] urParams = Request.getUriParams(request, 1, baseURL + "/user");
                int userId = 0;

                try {
                    userId = Integer.parseInt(urParams[0]);
                } catch (Exception e) {
                    throw new CustomHttpException(404, "The requsted resource was not found");
                }

                ArrayList<Task> tasks = this.taskDao.getByUserId(userId);
                JSON.respond(response, 200, "All tasks of user with id: " + userId, tasks);
                return;
            }

            // tasks/:id
            if (requestURI.startsWith(baseURL + "/")) {
                // a single task
                String[] urParams = Request.getUriParams(request, 1, baseURL);
                int id = 0;
                try {
                    id = Integer.parseInt(urParams[0]);
                } catch (Exception e) {
                    throw new CustomHttpException(400, "Invalid task id");
                }

                Task task = this.taskDao.get(id);
                if (task == null) {
                    throw new CustomHttpException(404, "No such task found");
                }

                JSON.respond(response, 200, "Task fetched successfully", task);
                return;
            }

            // if none of the above, then the requested resource was not found
            throw new CustomHttpException(404, "The requsted resource was not found");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        } catch (CustomHttpException e) {
            e.printStackTrace();
            JSON.respond(response, e.getStatusCode(), e.getMessage());
        }
    }

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
            System.out.println("Type:" + jsonData.get("user_id").getClass().getName());

            // lets validate the input data

            // title
            if (!jsonData.containsKey("title")) {
                JSON.respond(response, 400, "Please provide title");
                return;
            }
            if (!(jsonData.get("title") instanceof String) || jsonData.get("title").toString().trim().isEmpty()) {
                JSON.respond(response, 400, "Title must be a string and cannot be empty");
                return;
            }

            // description
            if (!jsonData.containsKey("description")) {
                JSON.respond(response, 400, "Please provide description");
                return;
            }
            if (!(jsonData.get("description") instanceof String)
                    || jsonData.get("description").toString().trim().isEmpty()) {
                JSON.respond(response, 400, "Description must be a string and cannot be empty");
                return;
            }

            // user_id
            if (!jsonData.containsKey("user_id")) {
                JSON.respond(response, 400, "Please provide user_id");
                return;
            }
            if (!(jsonData.get("user_id") instanceof Number)) {
                JSON.respond(response, 400, "user_id must be a number");
                return;
            }
            if (((Number) jsonData.get("user_id")).doubleValue() != ((Number) jsonData.get("user_id")).intValue()) {
                JSON.respond(response, 400, "user_id must be an integer");
                return;
            }

            int userId = ((Number) jsonData.get("user_id")).intValue();

            Task existingTask = this.taskDao.getByUserIdAndTitle(userId, jsonData.get("title").toString());
            if (existingTask != null) {
                JSON.respond(response, 409, "Task with the same title already exists");
                return;
            }

            // create the task
            Task task = new Task(
                    jsonData.get("title").toString(),
                    jsonData.get("description").toString(),
                    userId);
            task = this.taskDao.create(task);
            task.setTitle(null);
            System.out.println(
                    "Task created with id: " + task.getId() + " and title: " + task.getTitle() + "at: "
                            + task.getCreatedAt()
                            + " and updated at: " + task.getUpdatedAt());
            JSON.respond(response, 200, "Task created successfully", task);
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            JSON.respond(response, 500, e.getMessage());
        }
    }

    // update a task
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] urParams = Request.getUriParams(request, 1, baseURL);
            int id = 0;
            try {
                id = Integer.parseInt(urParams[0]);
            } catch (Exception e) {
                throw new CustomHttpException(404, "The requsted resource was not found");
            }
            System.out.println("id: " + id);

            // Get the JSON data from the request body
            HashMap<String, Object> jsonData = JSON.read(request);

            if (jsonData == null) {
                JSON.respond(response, 400, "Invalid JSON data");
                return;
            }

            // lets validate the input data
            // title
            if (jsonData.containsKey("title")) {
                if (!(jsonData.get("title") instanceof String) || jsonData.get("title").toString().trim().isEmpty()) {
                    JSON.respond(response, 400, "Title must be a string and cannot be empty");
                    return;
                }
            }
            // description
            if (jsonData.containsKey("description")) {
                if (!(jsonData.get("description") instanceof String)
                        || jsonData.get("description").toString().trim().isEmpty()) {
                    JSON.respond(response, 400, "Description must be a string and cannot be empty");
                    return;
                }
            }
            // status
            if (jsonData.containsKey("status")) {
                if (!(jsonData.get("status") instanceof Number)) {
                    JSON.respond(response, 400, "status must be a number");
                    return;
                }
                if (((Number) jsonData.get("status")).doubleValue() != ((Number) jsonData.get("status")).intValue()) {
                    JSON.respond(response, 400, "status must be an integer");
                    return;
                }
            }

            // update the task
            Task task = this.taskDao.get(id);
            if (task == null) {
                throw new CustomHttpException(404, "No task found with id: " + id);
            }

            if (jsonData.containsKey("title")) {
                task.setTitle(jsonData.get("title").toString());
            }
            if (jsonData.containsKey("description")) {
                task.setDescription(jsonData.get("description").toString());
            }
            if (jsonData.containsKey("status")) {
                task.setStatus(((Number) jsonData.get("status")).intValue());
            }

            Task conflictingTask = this.taskDao.getByUserIdAndTitle(task.getUserId(), task.getTitle());
            if (conflictingTask != null && conflictingTask.getId() != task.getId()) {
                JSON.respond(response, 409, "Task with the same title already exists");
                return;
            }
            
            task = this.taskDao.update(id, task);
            JSON.respond(response, 200, "Task updated successfully", task);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        } catch (CustomHttpException e) {
            e.printStackTrace();
            JSON.respond(response, e.getStatusCode(), e.getMessage());
        }
    }

    // delete a task
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] urParams = Request.getUriParams(request, 1, baseURL);
            int id = 0;
            try {
                id = Integer.parseInt(urParams[0]);
            } catch (Exception e) {
                throw new CustomHttpException(404, "The requsted resource was not found");
            }

            // delete the task
            Task task = this.taskDao.get(id);
            if (task == null) {
                throw new CustomHttpException(404, "No task found with id: " + id);
            }

            boolean isDeleted = this.taskDao.delete(id);
            if (!isDeleted) {
                throw new CustomHttpException(500, "Task could not be deleted");
            }

            JSON.respond(response, 200, "Task deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomHttpException(500, e.getMessage());
        } catch (CustomHttpException e) {
            e.printStackTrace();
            JSON.respond(response, e.getStatusCode(), e.getMessage());
        }
    }
}
