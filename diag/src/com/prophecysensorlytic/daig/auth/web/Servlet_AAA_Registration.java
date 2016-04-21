package com.prophecysensorlytic.daig.auth.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prophecysensorlytic.daig.auth.dao.DaoRegister;

/**
 * Servlet implementation class Servlet_AAA_Registration
 */
@WebServlet("/SERVLET_REG_AAA")
public class Servlet_AAA_Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet_AAA_Registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uid = request.getParameter("uid");
		String name = request.getParameter("name");
		String role = request.getParameter("role");
		String password = request.getParameter("password");
		String jsonStringResponse = DaoRegister.registerUser(uid, password, role, name);

		PrintWriter out = response.getWriter();
		out.println(jsonStringResponse);
		out.close();
	}

}
