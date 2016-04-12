package com.prophecysenorlytic.diag.dto;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class DTO_MachineJson {
	private String name;
	private String assembly;
	private String type = "machine";
	private String model;
	private String zone;
	private String _id;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public DTO_MachineJson(String name, String assembly, String type, String model, String zone, String id) {
		super();
		this.name = name;
		this.assembly = assembly;
		this.type = type;
		this.model = model;
		this.zone = zone;
		this._id = id;
	}

	
	public DTO_MachineJson(String name, String assembly, String model, String zone, String _id) {
		super();
		this.name = name;
		this.assembly = assembly;
		this.model = model;
		this.zone = zone;
		this._id = _id;
	}

	public DTO_MachineJson() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		try {
			jsonInString = mapper.writeValueAsString(this);

		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch blockS
			e.printStackTrace();
		}
		return jsonInString;
	}

}
