package com.prophecysensorlytic.daig.auth.dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.zkoss.json.JSONObject;
import org.zkoss.zhtml.Pre;

import com.prophecysensorlytic.daig.auth.UtilCrypto_Web;

public class DaoLogin {

	private static final String _USER_TABLE = "Users_Tbl";
	private static final String _MAPPING_TABLE = "CryptoMapping_Tbl";

	public static void main(String[] args) {
		System.out.println(getLoginResponse("soham@gmail.com", "123456", "1234556778", "1223456787654",
				"Samsung Galaxy Mega 6.7"));
	}

	public static String getLoginResponse(String uid, String pwd, String imei, String imsi, String deviceModel) {
		boolean isValidUser = isValid(uid, pwd);
		System.out.println("Valid ? " + isValidUser);
		String responseJson = "";
		if (!isValidUser) {
			return createErroJsonInvalidLogin();
		} else {
			String userInfo = String.format("%s %s %s %s %s", uid, pwd, imei, imsi, deviceModel);
			System.out.println(userInfo);
			String digestString = "";
			try {
				digestString = UtilCrypto_Web.generateDigest(userInfo);
				System.out.println("digest = " + digestString);
				boolean boolResponse = insertOrUpdateDigestValue(digestString, uid);
				responseJson = createResponseJsonSuccessfulLogin(digestString);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return responseJson;
		}
	}

	public static String getDigestValueByUserName(String uid) {
		Connection connection = null;
		JSONObject root = new JSONObject();
		boolean success = false;
		String data = "";
		String msg = "";
		String result = null;
		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("select Crypto_Digest from CryptoMapping_Tbl where uid=?");
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				success = true;
				result = rs.getString(1);
				data =result;
			} else {
				success = false;
				data = null;
				msg = "User not found # " + uid;
			}
			

		} catch (Exception e) {
			msg = "Error : " + e.toString();
			success = false;
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		root.put("success", success);
		root.put("msg", msg);
		root.put("data", data);

		return root.toJSONString();
	}

	private static boolean insertOrUpdateDigestValue(String digestString, String uid) {
		Connection connection = null;
		boolean boolUpdated = false;
		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("update " + _MAPPING_TABLE + " set Crypto_Digest=? where uid=?");
			ps.setString(1, digestString);
			ps.setString(2, uid);
			int intUpdated = ps.executeUpdate();
			boolUpdated = (intUpdated > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return boolUpdated;
	}

	private static String createResponseJsonSuccessfulLogin(String value) {
		JSONObject jObj = new JSONObject();
		jObj.put("success", true);
		jObj.put("msg", "Successfully logged in!");
		jObj.put("data", value);
		return jObj.toJSONString();
	}

	private static String createErroJsonInvalidLogin() {
		JSONObject jObj = new JSONObject();
		jObj.put("success", false);
		jObj.put("msg", "Error in login!");
		return jObj.toJSONString();
	}

	private static boolean isValid(String uid, String pwd) {
		Connection connection = null;
		boolean boolExists = false;
		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("select count(*) from " + _USER_TABLE + " where uid=? AND password=?");
			ps.setString(1, uid);
			ps.setString(2, pwd);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int intCount = rs.getInt(1);
			boolExists = (intCount > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return boolExists;
	}

}
