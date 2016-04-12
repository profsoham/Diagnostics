package com.prophecysenorlytic.diag.dto;

import java.util.List;

public class DTO_Sensor {
	private String _id;
	private String machineId;
	private String subAssembly;
	private String collectors;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getSubAssembly() {
		return subAssembly;
	}

	public void setSubAssembly(String subAssembly) {
		this.subAssembly = subAssembly;
	}

	public String getCollectors() {
		return collectors;
	}

	public void setCollectors(String collectors) {
		this.collectors = collectors;
	}

	public DTO_Sensor(String _id, String machineId, String subAssembly, String collector, String type) {
		super();
		this._id = _id;
		this.machineId = machineId;
		this.subAssembly = subAssembly;
		this.collectors = collector;
		this.type = type;
	}

	public DTO_Sensor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DTO_Sensor(String _id2, String get_id, String subAssembly2, List<String> collectors2) {
		// TODO Auto-generated constructor stub
	}

}
