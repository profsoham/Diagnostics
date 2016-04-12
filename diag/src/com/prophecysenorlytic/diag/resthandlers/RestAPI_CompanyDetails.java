package com.prophecysenorlytic.diag.resthandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Sensor;
import com.prophecysenorlytic.diag.dto.DTO_Zone;

public class RestAPI_CompanyDetails {
	private List<String> comapniesList;
	private String currentCompanyName;
	private JSONObject jsonObject_Currentcompany;
	private List<DTO_Zone> zonesList;
	private DTO_Zone currentZone;
	private JSONArray jsonArray_CurrentMachinesNode;

	private int companyIndex;
	private int zoneIndex;
	private int machineIndex;
	private String currentCompnay;
	private DTO_MachineJson currentMachine;

	public static void main(String[] args) {
		RestAPI_CompanyDetails d = new RestAPI_CompanyDetails(false);

		List<String> cList = d.getAllCompanyNames();
		for (String x : cList) {
			System.out.println(x);
		}

	}

	private boolean isMVP;

	public boolean isMVP() {
		return isMVP;
	}

	public void setMVP(boolean isMVP) {
		this.isMVP = isMVP;
		initCompanies(isMVP);
	}

	public final static String _REST_URL_COMPANY_DETAILS_BETA = "http://beta.production.prophecysensorlytics.com:3000/companies";
	public final static String _REST_URL_COMPANY_DETAILS_MVP = "http://mvp.production.prophecysensorlytics.com:3000/companies";
	private JSONArray rootNode;
	private JSONArray jsonObject_ZonesNode;
	private String json;
	private ArrayList<DTO_MachineJson> machinesList;

	public RestAPI_CompanyDetails(boolean isMVP) {

		initCompanies(isMVP);

	}

	public void initCompanies(boolean isMVP) {
		try {
			String _REST_URL_COMPANY_DETAILS = isMVP ? _REST_URL_COMPANY_DETAILS_MVP : _REST_URL_COMPANY_DETAILS_BETA;
			this.json = HttpUtils.readGetResponse(_REST_URL_COMPANY_DETAILS, "application/json", "application/json");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getAllCompanyNames() {
		JSONParser parser = new JSONParser();
		rootNode = (JSONArray) parser.parse(json);
		List<String> result = new ArrayList<>();
		int size = rootNode.size();
		for (int i = 0; i < size; i++) {
			JSONObject companyJsonObj = (JSONObject) rootNode.get(i);
			result.add((String) companyJsonObj.get("id"));
		}
		this.comapniesList = result;
		return result;
	}

	public List<DTO_Zone> getZonesForCompany(int index) {
		zonesList = new ArrayList<>();
		this.jsonObject_Currentcompany = (JSONObject) rootNode.get(index);
		this.currentCompanyName = this.comapniesList.get(index);

		JSONObject valueJsonObj = (JSONObject) this.jsonObject_Currentcompany.get("value");
		this.jsonObject_ZonesNode = (JSONArray) valueJsonObj.get("zones");
		int size = this.jsonObject_ZonesNode.size();
		for (int k = 0; k < size; k++) {
			JSONObject zoneJsonObj = (JSONObject) this.jsonObject_ZonesNode.get(k);
			DTO_Zone zone = new DTO_Zone((String) zoneJsonObj.get("name"), (String) zoneJsonObj.get("id"));
			zonesList.add(zone);
		}

		return zonesList;
	}

	public List<DTO_MachineJson> getMachinesUnderZoneOfComapny(int zoneIndex) {
		this.jsonArray_CurrentMachinesNode = (JSONArray) ((JSONObject) this.jsonObject_ZonesNode.get(zoneIndex))
				.get("machines");
		this.currentZone = zonesList.get(zoneIndex);
		machinesList = new ArrayList<>();
		int number_Of_Machines = this.jsonArray_CurrentMachinesNode.size();
		for (int k = 0; k < number_Of_Machines; k++) {
			JSONObject jsonObj_Machine = (JSONObject) this.jsonArray_CurrentMachinesNode.get(k);
			String _name = (String) jsonObj_Machine.get("name");
			String _id = (String) jsonObj_Machine.get("_id");
			String type = (String) jsonObj_Machine.get("type");
			String _model = (String) jsonObj_Machine.get("model");
			String zone = (String) jsonObj_Machine.get("zone");
			String assembly = (String) jsonObj_Machine.get("assembly");
			DTO_MachineJson machine = new DTO_MachineJson(_name, assembly, type, _model, zone, _id);
			machinesList.add(machine);
		}
		return machinesList;
	}

	public String getRestUrlFormatForSensorByMachine() {
		return isMVP ? "http://mvp.production.prophecysensorlytics.com:3000/company/%s/zone/%s/machine/%s"
				: "http://beta.production.prophecysensorlytics.com:3000/company/%s/zone/%s/machine/%s";
	}

	public List<DTO_Sensor> getSensorsForByMachineId(int index) {
		List<DTO_Sensor> sensors=new ArrayList<>();
		this.currentMachine=this.machinesList.get(index);
		String machineId = this.currentMachine.get_id();
		String urlFormat = getRestUrlFormatForSensorByMachine();
		String company = this.currentCompanyName;
		String zone = this.currentZone.getId();
		try {
			String url = AssetConstants.convertUriToURLString(String.format(urlFormat, company, zone, machineId));
			String jsonReponse = HttpUtils.readGetResponse(url, "application/json", "application/json");
			JSONParser parser = new JSONParser();
			JSONObject dataObj = (JSONObject) ((JSONObject) parser.parse(jsonReponse)).get("data");
			JSONObject jsonNodeSubassemblies = (JSONObject) dataObj.get("subassemblies");
			Set<Object> keys = jsonNodeSubassemblies.keySet();
			for (Object key : keys) {
				JSONObject subassembly = (JSONObject) jsonNodeSubassemblies.get(key);
				JSONObject collectorsNode = (JSONObject) subassembly.get("collectors");
				Set<Object> collectorKeys = collectorsNode.keySet();
				for (Object collectorKey : collectorKeys) {
					JSONObject jsonObject_collector = (JSONObject) collectorsNode.get(collectorKey);

					String _type = (String) (jsonObject_collector.containsKey("sensor_type")
							? jsonObject_collector.get("sensor_type") : "");
					String _id = (String) (jsonObject_collector.containsKey("sensor_id")
							? jsonObject_collector.get("sensor_id") : "");

					DTO_Sensor sensor = new DTO_Sensor(_id, machineId, (String) key, (String) collectorKey, _type);
					sensors.add(sensor);
				}
			}
			System.out.println(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sensors;

	}

	public String getCurrentCompanyName() {
		return currentCompanyName;
	}

	public DTO_MachineJson getCurrentMachine() {
		return currentMachine;
	}

}
