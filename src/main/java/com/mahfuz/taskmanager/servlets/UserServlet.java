package com.mahfuz.taskmanager.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mahfuz.taskmanager.daos.UserDAO;
import com.mahfuz.taskmanager.models.User;
import com.mahfuz.taskmanager.utils.CustomHttpException;
import com.mahfuz.taskmanager.utils.JSON;
import com.mahfuz.taskmanager.utils.Request;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = { "/users", "/users/*" })
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletConfig config;
    private UserDAO userDao;
    private String baseURL = "/taskmanager/users";

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config); // Call the init method of the parent class
            this.config = config;
            this.userDao = new UserDAO();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error initializing servlet");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HashMap<String, Object> jsonData = JSON.read(request);

            if (jsonData == null) {
                throw new CustomHttpException(400, "Invalid JSON data");
            }

            // lets validate the input data

            // name
            if (!jsonData.containsKey("name")) {
                JSON.respond(response, 400, "Please provide name");
                return;
            }
            if (!(jsonData.get("name") instanceof String) || jsonData.get("name").toString().trim().isEmpty()) {
                JSON.respond(response, 400, "name must be a string and cannot be empty");
                return;
            }

            // email
            if (!jsonData.containsKey("email")) {
                JSON.respond(response, 400, "Please provide email");
                return;
            }
            if (!(jsonData.get("email") instanceof String) || jsonData.get("email").toString().trim().isEmpty()) {
                JSON.respond(response, 400, "email must be a string and cannot be empty");
                return;
            }

            // password
            if (!jsonData.containsKey("password")) {
                JSON.respond(response, 400, "Please provide password");
                return;
            }
            if (!(jsonData.get("password") instanceof String) || jsonData.get("password").toString().trim().isEmpty()) {
                JSON.respond(response, 400, "password must be a string and cannot be empty");
                return;
            }

            // check if email already exists
            User existingUser = userDao.getUserByEmail(jsonData.get("email").toString());
            if (existingUser != null) {
                throw new CustomHttpException(409, "Email already exists");
            }

            User user = new User(
                    jsonData.get("name").toString(),
                    jsonData.get("email").toString(),
                    jsonData.get("password").toString());

            user = userDao.create(user);
            JSON.respond(response, 200, "User registered successfully", user);
        } catch (CustomHttpException e) {
            e.printStackTrace();
            JSON.respond(response, e.getStatusCode(), e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            System.out.println("Request URI: " + requestURI);

            // possible URI patterns:
            /*
             * 1) users/user/:id
             * 2) users
             */

            // users
            if (requestURI.equals(baseURL)) {
                // all users
                ArrayList<User> users = this.userDao.index();
                JSON.respond(response, 200, "All users", users);
                return;
            }

            // users/:id
            if (requestURI.startsWith(baseURL + "/")) {
                // a single user
                String[] urParams = Request.getUriParams(request, 1, baseURL);
                int id = 0;
                try {
                    id = Integer.parseInt(urParams[0]);
                } catch (Exception e) {
                    throw new CustomHttpException(400, "Invalid user  id");
                }

                User user = this.userDao.get(id);
                if (user == null) {
                    throw new CustomHttpException(404, "No such user  found");
                }

                HashMap<String, Object> userWithTasks = new HashMap<String, Object>();

                userWithTasks.put("id", user.getId());
                userWithTasks.put("name", user.getName());
                userWithTasks.put("email", user.getEmail());
                userWithTasks.put("status", user.getStatus());
                userWithTasks.put("created_at", user.getCreatedAt());
                userWithTasks.put("updated_at", user.getUpdatedAt());
                userWithTasks.put("tasks", user.getTasks());

                JSON.respond(response, 200, "User fetched successfully", userWithTasks);
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
}
