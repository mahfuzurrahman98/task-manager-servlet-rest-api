package com.mahfuz.codeshare.servlets;

import java.io.IOException;
import java.util.ArrayList;

import com.mahfuz.codeshare.daos.LanguageDAO;
import com.mahfuz.codeshare.models.Language;
import com.mahfuz.codeshare.utils.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LanguageDAO lang_dao = new LanguageDAO();
		ArrayList<Language> languages = lang_dao.getAllLanguages();

		JSON.respond(response, 200, "Languages fetched successfully", languages);
	}
}
