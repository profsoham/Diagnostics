package com.prophecysenorlytic.diag.dao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

public class UtilJson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonString = "{\"action\":\"heartbeat\",\"cmd\":{\"id\":\"raspberrypid9701b90-0077-11e6-8c96-697d6d15bc46\",\"time\":\"2016-04-12T02:36:16-04:00\",\"name\":\"raspberrypi\",\"mac\":\"b8:27:eb:ee:4c:3b\",\"sysInfo\":{\"load\":[0.02880859375,0.05322265625,0.04541015625],\"mem\":123228160,\"uptime\":514315},\"user\":{\"username\":\"blr-nootan-collector\",\"company\":\"ZREYAS-BLR-NOOTAN\"},\"sensorDetails\":\"null\",\"processDetails\":{\"prophecy_upgrade_service_name\":{\"processName\":\"prophecy-upgrade\",\"pid\":3293,\"startTime\":\"2016-04-11T01:56:09.401-0400\"},\"prophecy_gateway_service_name\":{\"processName\":\"prophecy-gateway\",\"pid\":27655,\"startTime\":\"2016-04-12T02:10:55.211-0400\"},\"gatewayrunning\":true,\"updaterunning\":true},\"connDetails\":{\"wifi\":[{\"interface\":\"wlan0\",\"access_point\":\"70:62:b8:70:ad:f4\",\"frequency\":2.412,\"ieee\":\"802.11bgn\",\"mode\":\"managed\",\"quality\":70,\"ssid\":\"ZREYAS-BLR-GTR-PRIMARY\"}],\"externalIP\":\"106.51.131.217\",\"internalIP\":[\"192.168.0.117\"]},\"wsState\":\"true\"},\"address\":\"::ffff:106.51.131.217\"}";
		JSONParser p = new JSONParser();
		JSONObject obj = (JSONObject) p.parse(jsonString);
		List<String[]> x = getAllKeyValPairs(obj);
		System.err.println(x.size());
		for (String[] y : x) {
			System.err.println(y[0] + "  " + y[1]);
		}
		System.err.println("SS");

	}

	public static List<String[]> getAllKeyValPairs(JSONObject rootObject) {
		List<String[]> al = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		rootObject.putAll(map);
		Set<Entry<String, String>> entries = map.entrySet();
		for (Entry<String, String> entry : entries) {
			String key = entry.getKey();
			String val = entry.getValue();
			al.add(new String[] { key, val });
		}
		return al;
	}

}
