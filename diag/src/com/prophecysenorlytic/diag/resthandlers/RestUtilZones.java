package com.prophecysenorlytic.diag.resthandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;
import org.zkoss.json.parser.ParseException;

import com.prophecysenorlytic.diag.dto.DTO_Zone;

public class RestUtilZones {

	private RestUtilZones() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] at) throws Exception {
		DTO_Zone[] zones = getAllZonesByCompanyName("IFB Appliances");
		for (DTO_Zone z : zones) {
			System.out.println(z);
		}
	}

	public static DTO_Zone[] getAllZonesByCompanyName(String company) {
		// TODO Auto-generated method stub
		ArrayList<DTO_Zone> zones = new ArrayList<DTO_Zone>();
		try {
			String jsonString = HttpUtils.readGetResponse(AssetConstants.getUrlFetchZonesByCompnay(company),
					"application/json", "application/json");

			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(jsonString);
			if (obj.containsKey("success")) {
				boolean b = (boolean) obj.get("success");
				if (b) {
					JSONObject jObj = (JSONObject) obj.get("data");

					if (null == jObj) {
						return null;
					}
					Set<Object> keys = jObj.keySet();
					for (Object key : keys) {
						JSONObject zoneObj = (JSONObject) jObj.get(key);
						String zoneId = (String) zoneObj.get("id");
						String zoneName = (String) zoneObj.get("name");

						DTO_Zone zone = new DTO_Zone();
						zone.setId(zoneId);
						zone.setName(zoneName);
						zones.add(zone);

					}

				}
			} else {
				throw new IllegalArgumentException("Incompatible JSON response!" + jsonString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zones.toArray(new DTO_Zone[zones.size()]);

	}
}
