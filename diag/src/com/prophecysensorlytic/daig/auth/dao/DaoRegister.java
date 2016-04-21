package com.prophecysensorlytic.daig.auth.dao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.zkoss.json.JSONObject;

public class DaoRegister {

	public static void main(String[] args) {
		 System.out.println(registerUser("anir", "123455", "admin", "Aniornab"));

	}

	public static String registerUser(String uid, String pwd, String role, String name) {
		JSONObject root = new JSONObject();
		String data = "";
		String error = "";
		boolean success = false;
		try (Connection connection = ConnectionFactory.getConnection()) {
			PreparedStatement ps = connection
					.prepareStatement("insert into Users_Tbl (uid,name,password,role) values(?,?,?,?)");
			ps.setString(1, uid);
			ps.setString(2, name);
			ps.setString(3, pwd);
			ps.setString(4, role);
			int intUpdated = ps.executeUpdate();
			ps.close();
			success = true;
			data = String.format("%d records inserted", intUpdated);

		} catch (Exception e) {
			success = false;
			error = stackTraceToString(e);
			e.printStackTrace();
		}
		root.put("success", success);
		root.put("data", data);
		root.put("error", error);
		return root.toJSONString();
	}

	public static String stackTraceToString(Throwable t) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		String result = new String(baos.toByteArray());
		ps.close();
		baos = null;
		return result;
	}
}
