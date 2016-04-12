package com.prophecysenorlytic.diag.dto;

import org.zkoss.json.JSONObject;

public class DTO_DiagBundle {

	public final static String _FILE_DOWNLOAD_BASE = "";
	private String client_id;
	private String file_name;
	private String link_fileDownload;
	private String insert_datetime;
	private String pin;
	private String user;
	private JSONObject jsonString;
	
	

	public DTO_DiagBundle(String client_id, String file_name, String link_fileDownload, String insert_datetime,
			String pin, String user, JSONObject jsonString) {
		super();
		this.client_id = client_id;
		this.file_name = file_name;
		this.link_fileDownload = link_fileDownload;
		this.insert_datetime = insert_datetime;
		this.pin = pin;
		this.user = user;
		this.jsonString = jsonString;
	}

	public JSONObject getJsonString() {
		return jsonString;
	}

	public void setJsonString(JSONObject jsonString) {
		this.jsonString = jsonString;
	}

	public String getLink_fileDownload() {
		return link_fileDownload;
	}

	public void setLink_fileDownload(String link_fileDownload) {
		this.link_fileDownload = link_fileDownload;
	}

	public DTO_DiagBundle(String client_id, String file_name, String link_fileDownload, String insert_datetime,
			String pin, String user) {
		super();
		this.client_id = client_id;
		this.file_name = file_name;
		this.link_fileDownload = link_fileDownload;
		this.insert_datetime = insert_datetime;
		this.pin = pin;
		this.user = user;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getInsert_datetime() {
		return insert_datetime;
	}

	public void setInsert_datetime(String insert_datetime) {
		this.insert_datetime = insert_datetime;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s", this.client_id, this.file_name, this.insert_datetime,
				this.link_fileDownload, this.pin, this.user);
	}

}
