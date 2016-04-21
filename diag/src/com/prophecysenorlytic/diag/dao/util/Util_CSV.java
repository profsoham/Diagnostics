package com.prophecysenorlytic.diag.dao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;

import com.prophecysenorlytic.diag.dto.DTO_SensorStat;
import com.prophecysenorlytic.diag.dto.RpiDetails;

public class Util_CSV {

	public Util_CSV() {
		// TODO Auto-generated constructor stub
	}

	public static String getCSV_FileName(Date startDate, Date endDate, String mac) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String result = String.format("%s_Log_%s-to%s.csv", mac, format.format(startDate), format.format(endDate));
		System.err.println("--------------------------"+result);
		return result;
	}

	public static byte[] exportToCSV_RpiLogs(ListModel<RpiDetails> model) {

		int size = model.getSize();
		StringBuffer sb = new StringBuffer();
		String header = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", "localTime", "timestamp", "externalIp",
				"internalIp", "uuid", "load", "mem", "uptime", "wifi");
		sb.append(header);
		for (int i = 0; i < size; i++) {
			RpiDetails rpi = model.getElementAt(i);
			System.err.println(rpi);
			sb.append(rpi.toString());

		}
		String csvString = sb.toString();
		sb = null;
		return csvString.getBytes();
	}

	public static byte[] exportToCSV_SensorDiagnosticsLog(ListModel<DTO_SensorStat> model) {

		int size = model.getSize();
		StringBuffer sb = new StringBuffer();
		String header = String.format("%s,%s,%s,%s\n", "ID", "TYPE", "DATE", "RATE");
		sb.append(header);
		for (int i = 0; i < size; i++) {
			DTO_SensorStat dto = model.getElementAt(i);
			// System.err.println(dto);
			sb.append(dto.toCSVString()).append("\n");

		}
		String csvString = sb.toString();
		sb = null;
		return csvString.getBytes();
	}

	private static String getFileName_CSV_SensorDiagnosticsLog(ListModel<DTO_SensorStat> model) {
		DTO_SensorStat dto = model.getElementAt(0);
		return String.format("%s-%s.csv", dto.getId(), dto.getDate());
	}

	public static void downloadCSV_SensorDiagnosticsLog(ListModel<DTO_SensorStat> model) {
		byte[] data = exportToCSV_SensorDiagnosticsLog(model);
		String fileName = getFileName_CSV_SensorDiagnosticsLog(model);
		Filedownload.save(data, "text/csv", fileName);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
