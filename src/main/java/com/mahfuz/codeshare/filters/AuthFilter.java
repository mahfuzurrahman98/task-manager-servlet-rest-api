package com.mahfuz.codeshare.filters;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({ "/login", "/register"})
public class AuthFilter extends HttpFilter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("login register filter...");
		HttpServletRequest _request = (HttpServletRequest) request;
		HttpServletResponse _response = (HttpServletResponse) response;

		HttpSession session = _request.getSession();

		if (session.getAttribute("username") != null) { // logged in
			_response.sendRedirect("home");
		} else { // not logged in
			chain.doFilter(request, response); // go
			System.out.println("passed!");
		}
	}
	
}
