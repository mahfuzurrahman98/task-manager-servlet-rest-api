package com.mahfuz.codeshare.services;

import java.io.IOException;

import com.mahfuz.codeshare.daos.UserDAO;
import com.mahfuz.codeshare.models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginService")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		// PrintWriter out = response.getWriter();
		UserDAO user_dao = new UserDAO();
		// JSONObject jo = new JSONObject();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User loggedInUser = user_dao.checkLogin(username, password);

		if (loggedInUser == null) {
			// jo.put("login", false);
		} else {
			session.setAttribute("id", loggedInUser.getId());
			session.setAttribute("name", loggedInUser.getName());
			session.setAttribute("username", loggedInUser.getUsername());
			session.setAttribute("email", loggedInUser.getEmail());
			// jo.put("login", true);
		}
		// out.print(jo);
	}
}
