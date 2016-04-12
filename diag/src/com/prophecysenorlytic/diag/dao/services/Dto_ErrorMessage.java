package com.prophecysenorlytic.diag.dao.services;

import org.zkoss.json.JSONObject;

public class Dto_ErrorMessage {
	boolean success;
	private int code;
	private String description;
	private String stackTrace;

	public Dto_ErrorMessage() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Dto_ErrorMessage(boolean success, int code, String description, String stackTrace) {
		super();
		this.success = success;
		this.code = code;
		this.description = description;
		this.stackTrace = stackTrace;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	@Override
	public String toString() {
		JSONObject obj = new JSONObject();
		obj.put("success", this.success);
		obj.put("code", this.code);
		obj.put("description", this.description);
		obj.put("stackTrace", this.stackTrace);
		return obj.toJSONString();

	}

}
