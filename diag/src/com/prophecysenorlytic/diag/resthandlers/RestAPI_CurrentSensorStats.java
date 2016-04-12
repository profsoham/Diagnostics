package com.prophecysenorlytic.diag.resthandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_SensorStat;

public class RestAPI_CurrentSensorStats {

	public static void main(String[] args) {
		/*
		 * List<DTO_SensorStat> al = getAllSensorsCurrentStats(); for
		 * (DTO_SensorStat s : al) { System.out.println(s); }
		 */
		System.out.println(getSensorCurrentStatsBySensorId("b0b448c8c884", true));

	}

	public final static String _URL_MVP_SENOR_STATS_CURRENT_SNAPSHOT = "http://mvp.production.prophecysensorlytics.com:3000/stats/all";
	public final static String _URL_BETA_SENOR_STATS_CURRENT_SNAPSHOT = "http://beta.production.prophecysensorlytics.com:3000/stats/all";

	private static final String DELIMITER_SENSORS = "_";

	public static String getSensorCurrentStatsBySensorIdAsString(String _id, boolean isMVP) {
		DTO_SensorStat stat = getSensorCurrentStatsBySensorId(_id, isMVP);
		return String.format("Last Heartbeat: %s at %d packets/second", stat.getDate(), stat.getRate());
	}

	public static DTO_SensorStat getSensorCurrentStatsBySensorId(String _id, boolean isMVP) {
		List<DTO_SensorStat> stats = getAllSensorsCurrentStats(isMVP);
		for (DTO_SensorStat dto : stats) {
			if (dto.getId().equalsIgnoreCase(_id)) {
				return dto;
			}
		}
		return null;
	}

	public static List<DTO_SensorStat> getAllSensorsCurrentStats(boolean isMVP) {
		List<DTO_SensorStat> lstSensorStats = new ArrayList<>();
		try {
			String jsonResponse = HttpUtils.readGetResponse(
					isMVP ? _URL_MVP_SENOR_STATS_CURRENT_SNAPSHOT : _URL_BETA_SENOR_STATS_CURRENT_SNAPSHOT,
					"application/json", "application/json");
			JSONParser parser = new JSONParser();
			JSONObject rootObject = (JSONObject) parser.parse(jsonResponse);
			Set<Object> set_Keys = rootObject.keySet();
			for (Object key : set_Keys) {
				String mKey = (String) key;
				if (mKey.contains(DELIMITER_SENSORS) && mKey.indexOf(DELIMITER_SENSORS) != 0) {
					String sensor_id = mKey.split(DELIMITER_SENSORS)[0];
					String sensor_type = mKey.split(DELIMITER_SENSORS)[1];
					JSONObject sensorValsObj = (JSONObject) rootObject.get(mKey);

					int rate = (int) sensorValsObj.get("rate");
					long time = (int) sensorValsObj.get("time");
					DTO_SensorStat sensorStat = new DTO_SensorStat(rate, time, sensor_id, sensor_type);
					lstSensorStats.add(sensorStat);
				}
			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return lstSensorStats;

	}

}
