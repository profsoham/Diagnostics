package com.prophecysenorlytic.diag.dto;

import org.zkoss.json.JSONObject;

/**
 * This POJO represent data sent from Raspberry-PI
 *
 * @author Soham Sengupta
 * @version 1.0
 * @since 2016-03-31
 */
public class RpiDetails {
	private String action;
	private String _LI = "";
	private String lastHeartbeatTime = "";
	private JSONObject selfAsJsonObj;

	public JSONObject getSelfAsJsonObj() {
		return selfAsJsonObj;
	}

	public void setSelfAsJsonObj(JSONObject selfAsJsonObj) {
		this.selfAsJsonObj = selfAsJsonObj;
	}

	public String get_LI() {
		return _LI;
	}

	public void set_LI(String _LI) {
		this._LI = _LI;
	}

	public String getLastHeartbeatTime() {
		return lastHeartbeatTime;
	}

	public void setLastHeartbeatTime(String lastHeartbeatTime) {
		this.lastHeartbeatTime = lastHeartbeatTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private String name;
	private String mac;
	private String internalIp;
	private String externalIp;
	private String localTime;
	private String timestamp;
	private String uuid;
	private String load;
	private String mem;
	private String uptime;
	private String wifi; // should be integer;
	private String company; // As of now it has no role
	private String zone; // Not functional now

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public RpiDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getLocalTime() {
		return localTime;
	}

	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,\"%s\",%s,%s,%s\n", localTime, timestamp, externalIp, internalIp, uuid,
				load, mem, uptime, wifi);
	}

}
