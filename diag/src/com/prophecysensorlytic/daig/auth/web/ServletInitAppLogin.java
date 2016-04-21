package com.prophecysensorlytic.daig.auth.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prophecysensorlytic.daig.auth.dao.DaoLogin;

/**
 * Servlet implementation class ServletInitAppLogin
 */
@WebServlet("/sial")
public class ServletInitAppLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletInitAppLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uid = request.getParameter("uid");
		String pwd = request.getParameter("pwd");
		String imei = request.getParameter("imei");
		String imsi = request.getParameter("imsi");
		String deviceModel = request.getParameter("model");
		String responseString = DaoLogin.getLoginResponse(uid, pwd, imei, imsi, deviceModel);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		System.err.println(responseString);
		out.println(responseString);
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
