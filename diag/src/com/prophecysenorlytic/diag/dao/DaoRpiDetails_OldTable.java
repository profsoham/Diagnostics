package com.prophecysenorlytic.diag.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.RpiDetails;

public class DaoRpiDetails_OldTable {
	final static String _SQL_OLD_FORMAT_LAST_HEARTBEAT_RPI = "SELECT * FROM  data where mac=? ORDER BY  data.localTime DESC LIMIT 0 , 1";

	public static void main(String[] args) {
		List<RpiDetails> l = getRpi_ByCompanyName("");
		for (RpiDetails d : l) {
			System.out.println(d.getMac() + " " + d.getName());
		}
		RpiDetails d = getLastActivityDetails("b8:27:eb:2e:9d:b0");
		System.out.println(d.getLocalTime() + " ---" + d.getInternalIp() + "  ---" + d.getLastHeartbeatTime());
		String[] x = getStatus_LastnDays(5, "b8:27:eb:2e:9d:b0");
		System.out.println(x[0] + "   " + x[1]);

	}

	public static void populateQuickInfo(RpiDetails dto, int numberOfDays) {
		String[] info = getStatus_LastnDays(numberOfDays, dto.getMac());
		dto.set_LI(info[0]);
		dto.setLastHeartbeatTime(info[1]);
	}

	public static List<RpiDetails> getRpiRecordForInterval(String mac, Date startDt, Date endDt, int offsetIndex,
			int _MAX_NUMBER_OF_RECORDS) {
		Connection connection = null;
		final String _FETCH_SQL_FORMAT = "SELECT * FROM data WHERE mac='%s' AND (DATE(data.localtime) BETWEEN  '%s' AND  '%s') order by data.localtime DESC LIMIT %d , %d";

		String sqlString = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		sqlString = String.format(_FETCH_SQL_FORMAT, mac, sdf.format(startDt), sdf.format(endDt), offsetIndex,
				_MAX_NUMBER_OF_RECORDS);

		System.err.println(sqlString);
		List<RpiDetails> details = new ArrayList<>();

		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlString);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				RpiDetails dto = process_Cursorr_Rpi(rs);
				details.add(dto);

			}
			ps.close();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return details;

	}

	public static String[] getStatus_LastnDays(int n, String mac) {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, -n);
		Date dayBefore_N_days = c.getTime();
		double _LI = getLivliness_Index(mac, dayBefore_N_days, today)[1];
		String lastHeartbeatTime = getLastActivityDetails(mac).getLocalTime();
		return new String[] { String.valueOf(_LI), lastHeartbeatTime };
	}

	public static String getSummaryAsHtmlString(String mac, Date startDate, Date endDate) {
		int no_of_Days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
		System.err.println(no_of_Days + "------------------------------------------");
		String[] data = getStatus_LastnDays(no_of_Days, mac);
		String strTemplate = "Last Heartbeat received: %s </br> Liveliness Index= %s";
		return String.format(strTemplate, data[1], data[0]);

	}

	public static String getLivelinessIndexAsString(String mac, Date startDate, Date endDate) {
		double[] lIndexParams = getLivliness_Index(mac, startDate, endDate);
		long numberOfHits = (long) lIndexParams[0];
		double _LI = lIndexParams[1];
		long numberofHitsExpected = (endDate.getTime() - startDate.getTime()) / 1000 * 10;
		String strTemplate = "Received %s heartbeats out of %s expected<br/> &there4; Livliness Index= %s &divide; %s = %s<br/>";
		return String.format(strTemplate, numberOfHits, numberofHitsExpected, numberOfHits, numberofHitsExpected, _LI);
	}

	public static double[] getLivliness_Index(String mac, Date startDate, Date endDate) {
		double livelinessIndex = 0;
		double interval_Seconds = (endDate.getTime() - startDate.getTime()) / 1000;
		int numberOfHeartbeatsReceived = 0;
		Connection connection = null;
		final String _FETCH_SQL_FORMAT = "SELECT count(*) FROM data WHERE mac='%s' AND (DATE(data.localtime) BETWEEN  '%s' AND  '%s')";
		String sqlString = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			sqlString = String.format(_FETCH_SQL_FORMAT, mac, sdf.format(startDate), sdf.format(endDate));

			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlString);

			ResultSet rs = ps.executeQuery();
			rs.next(); // This will always give result
			numberOfHeartbeatsReceived = rs.getInt(1);
			System.err.println(numberOfHeartbeatsReceived);
			livelinessIndex = (numberOfHeartbeatsReceived / interval_Seconds) * 10;
			BigDecimal bd = new BigDecimal(livelinessIndex);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			livelinessIndex = bd.doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return new double[] { numberOfHeartbeatsReceived, livelinessIndex };
	}

	public static RpiDetails process_Cursorr_Rpi(ResultSet rs) throws SQLException {
		RpiDetails dto = new RpiDetails();
		String name = rs.getString("hostname");
		String macAddr = rs.getString("mac"); // Redundant
		String mem = rs.getString("mem");
		String load = rs.getString("load");
		String uptime = rs.getString("uptime");
		String wifi = rs.getString("wifi");
		String timestamp = rs.getString("timestamp");
		String localtime = rs.getString("localTime");
		String uuid = rs.getString("uuid");
		dto.setName(name);
		dto.setMac(macAddr);
		dto.setLoad(load);
		dto.setMem(mem);
		dto.setUuid(uuid);
		dto.setWifi(wifi);
		dto.setUptime(uptime);
		dto.setTimestamp(timestamp);
		dto.setLocalTime(localtime);
		/*
		 * This block is needed as RAW field was coreected to JSON; else error
		 * would happen with prior data which was not JSON
		 */
		try {
			String raw = rs.getString("raw");
			JSONParser parser = new JSONParser();
			JSONObject root = (JSONObject) parser.parse(raw);
			dto.setSelfAsJsonObj(root);
			JSONObject cmd = (JSONObject) root.get("cmd");
			dto.setExternalIp((String) cmd.get("externalIP"));
			JSONArray internalIP_arr = (JSONArray) cmd.get("internalIP");
			System.err.println(internalIP_arr);
			StringBuffer sb = new StringBuffer();
			for (int k = 0; k < internalIP_arr.size(); k++) {
				sb.append(internalIP_arr.get(k)).append(k < internalIP_arr.size() - 1 ? "," : "");
			}
			dto.setInternalIp(sb.toString());
			sb = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public static RpiDetails getLastActivityDetails(String mac) {
		RpiDetails lastHeartbeatInfo = null;
		Connection connection = null;

		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(_SQL_OLD_FORMAT_LAST_HEARTBEAT_RPI);
			ps.setString(1, mac);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				lastHeartbeatInfo = process_Cursorr_Rpi(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return lastHeartbeatInfo;

	}

	public static List<RpiDetails> getRpi_ByCompanyName(String companyName) {
		Connection connection = null;
		List<RpiDetails> rpi_AvlblList = new ArrayList<>();
		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("SELECT  distinct(mac),hostname from data where hostname like 'ht%'");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RpiDetails dto = new RpiDetails();
				dto.setMac(rs.getString("mac"));
				dto.setName(rs.getString("hostname"));
				rpi_AvlblList.add(dto);
			}
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return rpi_AvlblList;
	}

}
