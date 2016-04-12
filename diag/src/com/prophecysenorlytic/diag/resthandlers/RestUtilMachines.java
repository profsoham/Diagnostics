package com.prophecysenorlytic.diag.resthandlers;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Zone;

public class RestUtilMachines {

	public static List<DTO_MachineJson> readMachinesOfCurrentCompany(String company) throws Exception {

		System.err.println(company);
		DTO_Zone[] zones = RestUtilZones.getAllZonesByCompanyName(company);
		ArrayList<DTO_MachineJson> al = new ArrayList<DTO_MachineJson>();
		for (DTO_Zone zone : zones) {
			System.err.println(zone);
			DTO_MachineJson[] machines = readMachinesByCompanyAndZone(company, zone.getId());
			al.addAll(Arrays.asList(machines));
		}
		return al;

	}

	public static DTO_MachineJson[] readMachinesByCompanyAndZone(String company, String mZone) {
		ArrayList<DTO_MachineJson> machines = new ArrayList<DTO_MachineJson>();
		try {

			String jsonString = HttpUtils.readGetResponse(AssetConstants.getUrlReadMachinesByZone(company, mZone),
					"application/json", "application/json");
			JSONParser jsonParser = new JSONParser();
			JSONObject obj;

			obj = (JSONObject) jsonParser.parse(jsonString);

			if (obj.containsKey("success")) {
				boolean b = (boolean) obj.get("success");
				if (b) {

					JSONObject dataObj = (JSONObject) obj.get("data");
					Set<Object> allDataKey = dataObj.keySet();
					for (Object key : allDataKey) {
						JSONObject machineJson = (JSONObject) dataObj.get(key);
						String name = (String) machineJson.get("name");
						String model = (String) machineJson.get("model");
						String type = (String) machineJson.get("type");
						String zone = (String) machineJson.get("zone");
						String _id = (String) machineJson.get("_id");
						String assembly = (String) machineJson.get("assembly");
						DTO_MachineJson machine = new DTO_MachineJson(name, assembly, type, model, zone, _id);
						machines.add(machine);

					}

				} else {
					// DO NOTHING
				}

			} else {
				throw new IllegalArgumentException("Incompatible JSON response!" + jsonString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return machines.toArray(new DTO_MachineJson[machines.size()]);
	}

	/*
	 * public static void main(String[] args) throws Exception { DTO_Model m =
	 * new DTO_Model("MVP100", "MVP100"); DTO_Zone test = new DTO_Zone("test",
	 * "test"); DTO_Machine machine = new DTO_Machine("machine-A", m, "pump",
	 * test); DTO_MachineJson mj = new DTO_MachineJson(machine);
	 * System.out.println(mj.toString()); boolean b = createMachine(mj,
	 * "Zreyas Technology"); System.out.println(b); DTO_MachineJson[] ms =
	 * readMachinesByCompanyAndZone("Zreyas Technology", "KA1"); for
	 * (DTO_MachineJson a : ms) { System.out.println(a.getModel()); }
	 * 
	 * }
	 */
	public static void main(String[] args) throws Exception {
		List<DTO_MachineJson> lst = readMachinesOfCurrentCompany("IFB Appliances");
		System.out.println(lst.size());
	}
}
