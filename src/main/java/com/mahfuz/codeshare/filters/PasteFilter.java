package com.mahfuz.codeshare.filters;

import java.io.IOException;
import java.util.Arrays;

import com.mahfuz.codeshare.daos.SourceCodeDAO;
import com.mahfuz.codeshare.models.SourceCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/paste")
public class PasteFilter extends HttpFilter {

	private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("paste filter");
		HttpServletRequest _request = (HttpServletRequest) request;
		HttpServletResponse _response = (HttpServletResponse) response;
		SourceCodeDAO source_code_dao = new SourceCodeDAO();
		HttpSession session = _request.getSession();

		int id = Integer.parseInt(_request.getParameter("i"));
		SourceCode source_code_details = source_code_dao.getDetailsByID(id);

		if (session.getAttribute("id") != null) { // user logged in, all access
			if (source_code_details.getVisibility() == 1) { // public
				System.out.println("public");
				chain.doFilter(request, response); // go
			} else if (source_code_details.getVisibility() == 2) { // protected
				if (Arrays.asList(source_code_details.getShared_persons()).contains((int) session.getAttribute("id"))) {
					System.out.println("protected");
					chain.doFilter(request, response); // go
				} else {
					_response.sendRedirect("home");
				}
			} else { // private
				if (source_code_details.getCreated_by() == (int) session.getAttribute("id")) {
					System.out.println("private");
					chain.doFilter(request, response); // go
				} else {
					_response.sendRedirect("home");
				}
			}
		} else { // only public access
			System.out.println("vis: " + source_code_details.getVisibility());
			if (source_code_details.getVisibility() == 1) { // public
				System.out.println("public");
				chain.doFilter(request, response); // go
			} else {
				_response.sendRedirect("home");
			}
		}
	}
}
