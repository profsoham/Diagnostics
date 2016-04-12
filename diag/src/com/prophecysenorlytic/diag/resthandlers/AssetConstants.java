package com.prophecysenorlytic.diag.resthandlers;

import java.net.URI;
import java.net.URL;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Zone;

public class AssetConstants {

	private AssetConstants() {
		// TODO Auto-generated constructor stub
	}

	public static final String _REST_API_CREATE_MACHINE = "http://172.99.75.121:9000/company/%s/zone/%s/machine";
	public final static String _REST_API_READ_MACHINES_BY_ZONE = "http://172.99.75.121:9000/company/%s/zone/%s/machines";
	public final static String _REST_API_UPDATE_MACHINE = "http://172.99.75.121:9000/company/%s/zone/%s/machine/%s";
	public final static String _REST_API_DELETE_MACHINE = "http://172.99.75.121:9000/company/%s/zone/%s/machine/%s";

	public static final String _REST_API_ADD_MODEL = "http://172.99.75.121:9000/company/%s/model";
	public static final String _REST_API_READ_MODEL = "http://172.99.75.121:9000/company/%s/models";
	public static final String _REST_API_DELETE_MODEL = "http://172.99.75.121:9000/company/%s/model/%s";

	public static final String _REST_API_ADD_ZONE = "http://172.99.75.121:9000/company/%s/zone";

	public static final String _REST_API_FETCH_ZONES = "http://172.99.75.121:9000/company/%s/zones";

	public static final String _REST_API_DELETE_ZONE = "http://172.99.75.121:9000/company/%s/zone/%s";
	public static final String _URL_FETCH_SENSORS_BY_MACHINE_AND_COMPANY = "http://172.99.75.121:9000/sensors/company/%s/machine/%s";

	public static final String getUrl_SensorsForMachine(String companyName,DTO_MachineJson machine) throws Exception{
		String strUrl = String.format(_URL_FETCH_SENSORS_BY_MACHINE_AND_COMPANY, companyName, machine.get_id());
		return convertUriToURLString(strUrl);
	}
	public static final String getUrlReadMachinesByZone(String company, String zoneId) throws Exception {
		String strUrl = String.format(_REST_API_READ_MACHINES_BY_ZONE, company, zoneId);
		System.err.println(strUrl);
		return convertUriToURLString(strUrl);
	}

	public static final String getUrlCreateMachine(String company, String zoneId) throws Exception {
		String strUrl = String.format(_REST_API_CREATE_MACHINE, company, zoneId);
		return convertUriToURLString(strUrl);

	}

	public static final String getUrlAddZone(String company) throws Exception {

		String strUrl = String.format(_REST_API_ADD_ZONE, company);
		return convertUriToURLString(strUrl);
		// System.out.println(url.toString());

	}

	public static final String getUrlReadModel(String company) throws Exception {
		String strUrl = String.format(_REST_API_READ_MODEL, company);
		return convertUriToURLString(strUrl);
	}

	public static final String getUrlCreateModel(String company) throws Exception {
		String strUrl = String.format(_REST_API_ADD_MODEL, company);
		return convertUriToURLString(strUrl);
	}

	public static final String getUrlFetchZonesByCompnay(String company) throws Exception {
		String strUrl = String.format(_REST_API_FETCH_ZONES, company);
		System.err.println(strUrl + "-----------------------------------");
		return convertUriToURLString(strUrl);
	}

	public static String convertUriToURLString(String strUrl) throws Exception {
		URL url = new URL(strUrl);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
				url.getQuery(), url.getRef());
		url = uri.toURL();
		return url.toExternalForm();
	}
}
