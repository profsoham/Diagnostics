package com.prophecysenorlytic.diag.resthandlers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;

import com.prophecysenorlytic.diag.dto.DTO_DiagBundle;

public class RestAPI_BundleViewer {

	public static final String _REST_URL_BUNDLE_VIEWER_COMPANY_DATE_RANGE = "http://%s.production.prophecysensorlytics.com:4000/cassandra/getDiagnosticBundleList?clientId=%s&date_range=%s";

	public final static String _REST_URL_BUNDLE_BY_PIN="http://%s.production.prophecysensorlytics.com:4000/cassandra/getDiagnosticBundle/%s";

	public static String getUrl_BundleByPin(String pin, boolean isMVP) {
		return String.format(_REST_URL_BUNDLE_BY_PIN,isMVP?"mvp":"beta",pin);
	}
	public final static String getUrl_DiagnosticBundleViewer(boolean isMVP, String clientId, String str_DateRange)
			throws Exception {
		return AssetConstants.convertUriToURLString(String.format(_REST_URL_BUNDLE_VIEWER_COMPANY_DATE_RANGE,
				isMVP ? "mvp" : "beta", clientId, str_DateRange));

	}
	public static String getLink_BundleFileDownload(String pin,boolean isMVP){
		return String.format("http://%s.production.prophecysensorlytics.com:4000/cassandra/getDiagnosticBundle/%s", isMVP?"mvp":"beta",pin);
	}
	
	public static List<DTO_DiagBundle> getdiagnosticBundleByCompanyAndDateRange(String str_CompanyId,String str_DateRange, boolean isMVP){
		List<DTO_DiagBundle> bundles=new ArrayList<>();
		try {
			
			String responseJsonString=HttpUtils.readGetResponse(getUrl_DiagnosticBundleViewer(isMVP, str_CompanyId, str_DateRange), "application/json", "application/json");
			JSONParser parser=new JSONParser();
			JSONArray rootBundleArray=(JSONArray)parser.parse(responseJsonString);
			int size=rootBundleArray.size();
			for(int k=0;k<size;k++){
				JSONObject budnleJsonObj=(JSONObject)rootBundleArray.get(k);
				String client_id=(String) budnleJsonObj.get("client_id");
				String insert_datetime=(String) budnleJsonObj.get("insert_datetime");
				String user=(String) budnleJsonObj.get("user");
				String file_name=(String) budnleJsonObj.get("file_name");
				String pin=(String) budnleJsonObj.get("pin");
				String lnk_download=getLink_BundleFileDownload(pin, isMVP);
				DTO_DiagBundle dto_bundle=new DTO_DiagBundle(client_id, file_name, lnk_download, insert_datetime, pin, user);
				dto_bundle.setJsonString(budnleJsonObj);
				bundles.add(dto_bundle);
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return bundles;
	}

	public static void main(String[] args) {
		 List<DTO_DiagBundle> x=getdiagnosticBundleByCompanyAndDateRange("novatec","sixMonth",false);
		 for(DTO_DiagBundle a:x){
			 System.out.println(a);
		 }
		 

	}

}
