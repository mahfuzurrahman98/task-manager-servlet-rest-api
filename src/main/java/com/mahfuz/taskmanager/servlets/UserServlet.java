package com.mahfuz.taskmanager.servlets;

import java.io.IOException;
import java.util.HashMap;

import com.mahfuz.taskmanager.daos.UserDAO;
import com.mahfuz.taskmanager.models.User;
import com.mahfuz.taskmanager.utils.CustomHttpException;
import com.mahfuz.taskmanager.utils.JSON;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletConfig config;
    private UserDAO userDao;

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
}
