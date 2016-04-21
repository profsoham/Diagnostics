package com.prophecysensorlytic.daig.auth.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class Filter_Firewall
 */
@WebFilter("/*")
public class Filter_Firewall implements Filter {

	public static final String _DIAG_USER_ID = "_DIAG_USER_ID";

	/**
	 * Default constructor.
	 */
	public Filter_Firewall() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rsp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String uri = req.getRequestURI();
		if (isAllowedUri(uri)) {
			chain.doFilter(request, response);
		} else {
			if (null != session) {
				Object obj = session.getAttribute(_DIAG_USER_ID);
				if (null != obj) {
					chain.doFilter(request, response);
				} else {
					redirectToLogin(req, rsp);
				}
			} else {
				redirectToLogin(req, rsp);
			}
		}
	}

	private boolean isAllowedUri(String uri) {
		// TODO Auto-generated method stub
		return (uri.endsWith("/auth.zul") || uri.endsWith("/zkau") || uri.contains("/sdbun") || uri.contains("sial")
				|| uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".wpd") || uri.endsWith(".wcs")
				|| uri.endsWith(".gif") || uri.endsWith(".jpg"));

	}

	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {

		try {
			response.sendRedirect("auth.zul");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
