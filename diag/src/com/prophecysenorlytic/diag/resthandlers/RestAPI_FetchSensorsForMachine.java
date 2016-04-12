package com.prophecysenorlytic.diag.resthandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Sensor;

public class RestAPI_FetchSensorsForMachine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static List<DTO_Sensor> getSensorsByCompanyAndMachineId(String companyName, DTO_MachineJson machine) {
		List<DTO_Sensor> sensors = new ArrayList<>();
		try {
			String uri = AssetConstants.getUrl_SensorsForMachine(companyName, machine);
			String responseJson = HttpUtils.readGetResponse(uri, "application/json", "application/json");
			System.err.println(responseJson);
			JSONParser parser = new JSONParser();
			JSONObject root = (JSONObject) parser.parse(responseJson);
			if (root.containsKey("data")) {
				JSONArray arr = (JSONArray) root.get("data");
				int size = arr.size();
				for (int i = 0; i < size; i++) {
					JSONObject obj = (JSONObject) arr.get(i);
					String _id = (String) obj.get("id");
					JSONObject sensorVals = (JSONObject) obj.get("value");
					String subAssembly = (String) sensorVals.get("SubAssembly");
					JSONObject collectorsJsonObj = (JSONObject) sensorVals.get("Collectors");
					Set<Object> keySet = collectorsJsonObj.keySet();
					List<String> collectors = new ArrayList<>();
					for (Object key : keySet) {
						collectors.add((String) key);
					}
					DTO_Sensor sensor = new DTO_Sensor(_id, machine.get_id(), subAssembly, collectors);
					sensors.add(sensor);

				}
			} else {
				throw new IllegalArgumentException("Json does not contain the KEY: data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensors;

	}

}
