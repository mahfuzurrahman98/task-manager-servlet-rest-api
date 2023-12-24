package com.mahfuz.codeshare.servlets;

import java.io.IOException;
import java.util.ArrayList;

import com.mahfuz.codeshare.daos.SourceCodeDAO;
import com.mahfuz.codeshare.models.SourceCode;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/library")
public class Library extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("library controller");
		HttpSession session = request.getSession();
		SourceCodeDAO source_code_dao = new SourceCodeDAO();

		int cur_user_id = (int) session.getAttribute("id");
		ArrayList<SourceCode> library = source_code_dao.getLibraryByUser(cur_user_id);
		System.out.println("Size_x: " + library.size());
//		for (int i = 0; i < library.size(); i++) {
//			System.out.println(library.get(i).getLanguage());
//		}
		request.setAttribute("library", library);

		RequestDispatcher rd = request.getRequestDispatcher("views/library.jsp");
		rd.forward(request, response);
	}

}
