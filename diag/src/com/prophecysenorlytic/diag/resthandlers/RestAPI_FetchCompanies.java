package com.prophecysenorlytic.diag.resthandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

public class RestAPI_FetchCompanies {

	public RestAPI_FetchCompanies() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> x=getCompanyNames();
		
		for(String y:x){
			System.out.println(y);
		}
	}

	public static final String _REST_URI_COMPANIES = "http://172.99.75.121:9000/companies";

	public static List<String> getCompanyNames() {
		List<String> companyList = new ArrayList<>();
		try {
			String response = HttpUtils.readGetResponse(_REST_URI_COMPANIES, "application/json", "application/json");
			JSONParser jsonParser = new JSONParser();
			JSONArray arrayRoot = (JSONArray) jsonParser.parse(response);
			int size = arrayRoot.size();
			for (int i = 0; i < size; i++) {
				JSONObject element = (JSONObject) arrayRoot.get(i);
				if (element.containsKey("id")) {
					String companyNameOrId = (String) element.get("id");
					companyList.add(companyNameOrId);

				}
			}
			Collections.sort(companyList);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companyList;

	}

}
