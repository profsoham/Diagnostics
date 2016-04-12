package com.prophecysenorlytic.diag.resthandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_Sensor;
import com.prophecysenorlytic.diag.dto.DTO_SensorStat;

public class RestAPI_HistorySensorStats {
	public final static String _URL_BASE_HISTORY_SENSOR_STATS = "http://mvp.production.prophecysensorlytics.com:5678/api/datasources/proxy/1/query?epoch=ms&q=";
	public final static String _Q = "SELECT \"value\" FROM \"statsd_rate\" WHERE \"sensorType\" =~ /(AC|MG|IR|PF|VAC)$/ AND \"sensorId\" =~ /%s$/ AND time > now() - %s GROUP BY \"sensorType\"";
	private static final int _UNIT_MINUTES = 0;
	private static final int _UNIT_HOURS = 1;

	public static String build_Q(DTO_Sensor sensor, int duration, int unit) {
		String interval = "";
		switch (unit) {
		case _UNIT_MINUTES:
			interval = duration + "m";
			break;
		case _UNIT_HOURS:
			interval = duration + "s";
			break;

		default:
			break;
		}
		return String.format(_Q, sensor.get_id(), interval);
	}

	public static String getUrl_SensorStatsHistory(DTO_Sensor sensor, int duration, int unit, boolean isMVP) {
		return _URL_BASE_HISTORY_SENSOR_STATS + build_Q(sensor, duration, unit);
	}

	public static List<DTO_SensorStat> getSensorStatsHistory(DTO_Sensor sensor, int duration, int unit, boolean isMVP) {
		List<DTO_SensorStat> statsList = new ArrayList<>();
		try {
			String sensorId = sensor.get_id();
			String _url = getUrl_SensorStatsHistory(sensor, duration, unit, isMVP);

			_url = AssetConstants.convertUriToURLString(_url);

			String responseJson = HttpUtils.readGetResponse(_url, "application/json", "application/json");

			JSONParser parser = new JSONParser();
			JSONObject root = (JSONObject) parser.parse(responseJson);
			JSONArray arr = (JSONArray) root.get("results");
			JSONObject arrElementJsonObj = (JSONObject) arr.get(0);
			JSONArray seriesArr = (JSONArray) arrElementJsonObj.get("series");
			if (null != seriesArr) {
				int size = seriesArr.size();
				for (int i = 0; i < size; i++) {
					JSONObject jsonElement = (JSONObject) seriesArr.get(i);
					String sensorType = (String) ((JSONObject) jsonElement.get("tags")).get("sensorType");
					JSONArray statsValues = (JSONArray) jsonElement.get("values");
					int elSize = statsValues.size();
					for (int j = 0; j < elSize; j++) {
						JSONArray val = (JSONArray) statsValues.get(j);
						long time = (long) val.get(0);
						int rate = (int) val.get(1);
						DTO_SensorStat dto = new DTO_SensorStat(rate, time, sensorId, sensorType,false);
						statsList.add(dto);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statsList;
	}
	/* This version is for testing only */
	public static List<DTO_SensorStat> getSensorStatsHistory(DTO_Sensor sensor, int duration, int unit, boolean isMVP,boolean testVersion) {
		List<DTO_SensorStat> statsList = new ArrayList<>();
		try {
			String sensorId = "b0b448c8c884"; // FAKE ; for testing only
			sensor.set_id(sensorId);
			String _url = getUrl_SensorStatsHistory(sensor, duration, unit, isMVP);

			_url = AssetConstants.convertUriToURLString(_url);

			String responseJson = HttpUtils.readGetResponse(_url, "application/json", "application/json");

			JSONParser parser = new JSONParser();
			JSONObject root = (JSONObject) parser.parse(responseJson);
			JSONArray arr = (JSONArray) root.get("results");
			JSONObject arrElementJsonObj = (JSONObject) arr.get(0);
			JSONArray seriesArr = (JSONArray) arrElementJsonObj.get("series");
			if (null != seriesArr) {
				int size = seriesArr.size();
				for (int i = 0; i < size; i++) {
					JSONObject jsonElement = (JSONObject) seriesArr.get(i);
					String sensorType = (String) ((JSONObject) jsonElement.get("tags")).get("sensorType");
					JSONArray statsValues = (JSONArray) jsonElement.get("values");
					int elSize = statsValues.size();
					for (int j = 0; j < elSize; j++) {
						JSONArray val = (JSONArray) statsValues.get(j);
						long time = (long) val.get(0);
						int rate = (int) val.get(1);
						DTO_SensorStat dto = new DTO_SensorStat(rate, time, sensorId, sensorType,false);
						statsList.add(dto);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statsList;
	}

	public static void main(String[] args) {
		DTO_Sensor s = new DTO_Sensor();
		s.set_id("b0b448c8c884");
		List<DTO_SensorStat> h = getSensorStatsHistory(s, 45, _UNIT_MINUTES, true);
		for (DTO_SensorStat a : h) {
			System.out.println(a);
		}
	}
}
