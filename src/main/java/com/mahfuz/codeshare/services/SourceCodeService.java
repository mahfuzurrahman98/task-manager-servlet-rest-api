package com.mahfuz.codeshare.services;

import java.io.IOException;

import com.mahfuz.codeshare.DateProcessing;
import com.mahfuz.codeshare.daos.SourceCodeDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SourceCodeService")
public class SourceCodeService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		SourceCodeDAO source_code_dao = new SourceCodeDAO();
		if (request.getParameterMap().containsKey("add")) {
			System.out.println("here in add.....");
			String title = request.getParameter("title");
			int language = Integer.parseInt(request.getParameter("language"));
			int poster = Integer.parseInt(request.getParameter("poster"));
			String poster_name = request.getParameter("poster_name");
			String source = request.getParameter("source");
			System.out.println("poster name: " + poster_name);
			int expire = Integer.parseInt(request.getParameter("expire"));
			DateProcessing dp = new DateProcessing();
			java.sql.Timestamp curTimestamp = dp.getCurTimestamp();
			String expTimestamp = null;
			int visibility = 1;
			String[] share_with = null;

			if (expire == 1) {
				expTimestamp = dp.getExpTimestamp(1).toString();
			} else if (expire == 2) {
				expTimestamp = dp.getExpTimestamp(24).toString();
			} else if (expire == 3) {
				expTimestamp = dp.getExpTimestamp(7 * 24).toString();
			} else if (expire == 4) {
				expTimestamp = dp.getExpTimestamp(30 * 24).toString();
			}

			if (session.getAttribute("id") != null) {
				share_with = request.getParameterValues("share_with");
				visibility = Integer.parseInt(request.getParameter("visibility"));
			}
			source_code_dao.addSourceCode(title, language, visibility, source, poster, poster_name,
					curTimestamp.toString(), expTimestamp, 1, share_with);
		}

		if (request.getParameterMap().containsKey("change_status")) {
			System.out.println("here.....");
			int id = Integer.parseInt(request.getParameter("id"));
			source_code_dao.changeStatus(id);
		}
	}
}