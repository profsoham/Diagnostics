package com.prophecysensorlytic.daig.auth.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prophecysensorlytic.daig.auth.dao.DaoLogin;

/**
 * Servlet implementation class ServletGetDigestByUserName
 */
@WebServlet("/sdbun")
public class ServletGetDigestByUserName extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletGetDigestByUserName() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		String uid = request.getParameter("uid");
		response.getWriter().print(DaoLogin.getDigestValueByUserName(uid));
		response.getWriter().close();
	}

}
