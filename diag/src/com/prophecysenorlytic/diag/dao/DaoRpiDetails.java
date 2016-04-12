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

/**
 * This DAO operates CRUD on data sent from Raspberry-PI
 *
 * @author Soham Sengupta
 * @version 1.0
 * @since 2016-03-31
 */
public class DaoRpiDetails {

	final static String _SQL_FORMAT_LAST_HEARTBEAT_RPI = "SELECT * FROM  messages where mac=? ORDER BY  messages.localTime DESC LIMIT 0 , 1";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int no_of_Days = 1;
		Date endDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.DATE, -no_of_Days);
		Date startDate = c.getTime();
		List<RpiDetails> details = DaoRpiDetails.getRpiRecordForInterval("b8:27:eb:45:80:e7", startDate, endDate, 0, 5);
		for (RpiDetails r : details) {
			System.out.println(r.getLocalTime() + " " + r.getCompany());
		}
		// System.out.println(getLastActivityDetails("b8:27:eb:45:80:e7").getInternalIp());

	}

	public static RpiDetails getLastActivityDetails(String mac) {
		RpiDetails lastHeartbeatInfo = null;
		Connection connection = null;

		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(_SQL_FORMAT_LAST_HEARTBEAT_RPI);
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
			PreparedStatement ps = connection.prepareStatement(
					"SELECT  distinct(mac), hostname from messages where company=? order by hostname");
			ps.setString(1, companyName);
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

	/* This is for testing only */
	public static List<RpiDetails> getRpi_ByCompanyName(String companyName, boolean testingVersion) {
		Connection connection = null;
		List<RpiDetails> rpi_AvlblList = new ArrayList<>();
		try {
			connection = ConnectionFactory.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("SELECT  distinct(mac), hostname from messages order by hostname");

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

	/*
	 * {"action":"heartbeat","cmd":{"id":
	 * "sumin2f84a7060-f73a-11e5-a458-edb529368cea","time":
	 * "2016-03-31T12:27:05.748Z","name":"sumin2","externalIP":"65.210.82.114",
	 * "internalIP":["192.168.10.73"],"mac":"b8:27:eb:ab:2c:0a","sysInfo":{
	 * "load":[0.6689453125,0.4697265625,0.29248046875],"mem":625569792,"uptime"
	 * :3143.26324376}},"address":"::ffff:65.210.82.114"} SELECT * FROM data
	 * WHERE mac='b8:27:eb:45:80:e7' AND (DATE(localtime) BETWEEN '2016-04-01'
	 * AND '2016-04-02') LIMIT 0 , 5
	 * 
	 * 
	 * 
	 */

	public static List<RpiDetails> getRpiRecordForInterval(String mac, Date startDt, Date endDt, int offsetIndex,
			int _MAX_NUMBER_OF_RECORDS) {
		Connection connection = null;
		final String _FETCH_SQL_FORMAT = "SELECT * FROM messages WHERE mac='%s' AND (DATE(messages.localtime) BETWEEN  '%s' AND  '%s') order by messages.localtime DESC LIMIT %d , %d";

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

	public static RpiDetails process_Cursorr_Rpi(ResultSet rs) throws SQLException {
		RpiDetails dto = new RpiDetails();
		String name = rs.getString("hostname");
		String macAddr = rs.getString("mac"); // Redundant
		// String mem = rs.getString("mem");
		// String load = rs.getString("load");
		String uptime = rs.getString("uptime");
		// String wifi = rs.getString("wifi");
		String timestamp = rs.getString("timestamp");
		String localtime = rs.getString("localTime");
		// String uuid = rs.getString("uuid");
		dto.setName(name);
		dto.setMac(macAddr);
		// dto.setLoad(load);
		// dto.setMem(mem);
		// dto.setUuid(uuid);
		// dto.setWifi(wifi);
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
			JSONObject jObj = (JSONObject) parser.parse(raw);
			dto.setSelfAsJsonObj(jObj);
			dto.setAction((String) jObj.get("action"));
			JSONObject jObjRoot2 = (JSONObject) jObj.get("cmd");
			dto.setUuid((String) jObjRoot2.get("id"));
			
			dto.setMac((String) jObjRoot2.get("mac"));
			JSONObject sysInfoObj = (JSONObject) jObjRoot2.get("sysInfo");
			dto.setMem(String.valueOf((Integer) sysInfoObj.get("mem")));
			dto.setUptime(String.valueOf((Integer) sysInfoObj.get("uptime")));
			String load=getLoadAsString((JSONArray)sysInfoObj.get("load"));
			dto.setLoad(load);
			JSONObject jOb3=(JSONObject)jObjRoot2.get("connDetails");
			dto.setExternalIp((String) jOb3.get("externalIP"));
			JSONArray arr = (JSONArray) jOb3.get("internalIP");
			int sizeInternalIP = arr.size();
			StringBuffer sb = new StringBuffer();
			for (int k = 0; k < sizeInternalIP; k++) {
				sb.append((String) arr.get(k)).append(",");
			}
			dto.setInternalIp(sb.toString());
			sb = null;
			String wifi=getWifiAsString((JSONArray)jOb3.get("wifi"));
			dto.setWifi(wifi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	private static String getWifiAsString(JSONArray jsonArray) {
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<jsonArray.size();k++){
			JSONObject obj=(JSONObject) jsonArray.get(k);
			String entryWifi=String.format("%s %s %s,", obj.get("interface"),obj.get("ieee"),obj.get("mode"));
			sb.append(entryWifi);
		}
		String result=sb.toString();
		sb=null;
		return result;
	}

	private static String getLoadAsString(JSONArray jsonArray) {
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<jsonArray.size();k++){
			sb.append(jsonArray.get(k)).append(",");
		}
		String result=sb.toString();
		sb=null;
		return result;
	}

	public static void populateQuickInfo(RpiDetails dto, int numberOfDays) {
		String[] info = getStatus_LastnDays(numberOfDays, dto.getMac());
		dto.set_LI(info[0]);
		dto.setLastHeartbeatTime(info[1]);
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
		final String _FETCH_SQL_FORMAT = "SELECT count(*) FROM messages WHERE mac='%s' AND (DATE(messages.localtime) BETWEEN  '%s' AND  '%s')";
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
}
/*
 * 
 * {"action":"heartbeat","cmd":{"id":
 * "prophecykol8fa42f90-f7c8-11e5-bbb3-f5db39d2616e","time":
 * "2016-04-01T05:23:40.194Z","name":"prophecykol","externalIP":"27.63.145.27",
 * "internalIP":["192.168.1.101"],"mac":"b8:27:eb:45:80:e7","sysInfo":{"load":[0
 * .0029296875,0.0146484375,0.04541015625],"mem":619810816,"uptime":52075.
 * 764594191}},"address":"::ffff:223.191.17.195"}
 * 
 */