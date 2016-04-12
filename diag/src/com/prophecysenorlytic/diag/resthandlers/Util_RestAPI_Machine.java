package com.prophecysenorlytic.diag.resthandlers;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;

public class Util_RestAPI_Machine {
		public static DTO_MachineJson[] getMachineForZoneAndCompany(String zoneId,String company){
		try {
			return RestUtilMachines.readMachinesByCompanyAndZone(company, zoneId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
