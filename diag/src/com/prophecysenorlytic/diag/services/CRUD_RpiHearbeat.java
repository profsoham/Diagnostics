package com.prophecysenorlytic.diag.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;
import org.zkoss.json.parser.ParseException;

import com.prophecysenorlytic.diag.dao.services.DaoService_Insert;
import com.prophecysenorlytic.diag.dao.services.Dto_ErrorMessage;
import com.prophecysenorlytic.diag.dao.services.UtilErroCodes;

/**
 * Servlet implementation class CRUD_RpiHearbeat
 */
@WebServlet("/CRUD_RpiHearbeat")
public class CRUD_RpiHearbeat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CRUD_RpiHearbeat() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Dto_ErrorMessage error = new Dto_ErrorMessage();
		String mac = request.getParameter("mac");
		String host = request.getParameter("hostname");
		String localTime = request.getParameter("localtime");
		String timestamp = request.getParameter("timestamp");
		String uptime = request.getParameter("uptime");
		String raw = request.getParameter("raw");
//		System.out.printf("%s %s %s %s %s %s\",mac,host,localTime,timestamp,uptime,raw);
		String company = getCompanyFromRaw(raw, error);
		if (error.isSuccess()) {
			error = DaoService_Insert.insertHearbeat(mac, host, company, timestamp, localTime, uptime, raw);
		}
		
		PrintWriter out = response.getWriter();
		out.println(error.toString());

	}

	private String getCompanyFromRaw(String raw, Dto_ErrorMessage error) {
		error.setSuccess(true);
		try {
			JSONParser parser = new JSONParser();
			JSONObject rootObj = (JSONObject) parser.parse(raw);
			if (rootObj.containsKey("cmd")) {
				JSONObject cmdObj = (JSONObject) rootObj.get("cmd");
				if (cmdObj.containsKey("user")) {
					JSONObject userObject = (JSONObject) cmdObj.get("user");
					if (userObject.containsKey("company")) {
						return (String) userObject.get("company");
					} else {
						error.setCode(UtilErroCodes.NO_SUCH_KEY);
						error.setDescription("Key 'company' does not exist in Json");
						error.setSuccess(false);
					}
				} else {
					error.setCode(UtilErroCodes.NO_SUCH_KEY);
					error.setDescription("Key 'user' does not exist in Json");
					error.setSuccess(false);
				}
			} else {
				error.setCode(UtilErroCodes.NO_SUCH_KEY);
				error.setDescription("Key 'cmd' does not exist in Json");
				error.setSuccess(false);
			}
		} catch (ParseException e) {
			error.setCode(UtilErroCodes.INCOMPATIBLE_FIRMWARE);
			error.setDescription("You use older firware");
			error.setSuccess(true);
			return "Not Available";
			
		}
		return null;

	}

}
