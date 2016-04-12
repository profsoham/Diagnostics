package com.prophecysenorlytic.diag.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.json.JSONObject;

import com.prophecysenorlytic.diag.dao.util.Util_Formatters;

public class DTO_SensorStat {

	private int rate;
	private long time;
	private String id;
	private String type;
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DTO_SensorStat(int rate, long time, String id, String type) {
		super();
		this.rate = rate;
		this.time = time;
		this.id = id;
		this.type = type;
		Date dateObj=new Date(this.time* 1000);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		this.date=sdf.format(dateObj);
				
	}
	public DTO_SensorStat(int rate, long time, String id, String type,boolean millis) {
		super();
		this.rate = rate;
		this.time = time;
		this.id = id;
		this.type = type;
		Date dateObj=new Date(this.time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		this.date=sdf.format(dateObj);
				
	}

	public DTO_SensorStat() {
		super();

	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		obj.put("type", type);
		obj.put("rate", rate);
		obj.put("time", time);
		obj.put("date", date);
		return obj.toJSONString();
	}
	public String toCSVString(){
		
		return String.format("%s , %s, %s, %s", Util_Formatters.getFormattedMAC(this.id),this.type,this.date,this.rate);
	}

}
