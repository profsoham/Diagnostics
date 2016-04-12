package com.prophecysenorlytic.diag.dao.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.prophecysenorlytic.diag.dao.ConnectionFactory;

public class DaoService_Insert {

	public DaoService_Insert() {
		// TODO Auto-generated constructor stub 2016-04-04 05:57:59 2016-04-04
		// 06:03:27 b8:27:eb:ee:4c:3b
		// raspberrypi81a36080-fa4a-11e5-9b22-e5fca6c6bca6
		// [0.00537109375,0.03076171875,0.04541015625]
	}

	public static void main(String[] args) {

		Dto_ErrorMessage msg = insertHearbeat("b8:27:eb:ee:4c:3b", "raspberrypi81a36080", "Z-company",
				"2016-04-04 05:57:59", "2016-04-04 06:03:27", "234", "rawTest");
		System.out.println(msg);
	}

	final static String _SQL_INSERT = "INSERT INTO messages(mac,hostname,company,timestamp,messages.localTime,uptime,raw) values(?,?,?,?,?,?,?)";

	public static Dto_ErrorMessage insertHearbeat(String mac, String hostname, String company,String timestamp, String localTime,
		String uptime, String raw) {
		Dto_ErrorMessage msg = new Dto_ErrorMessage();
		Connection connection = null;
		boolean success = true;
		try {
			connection=ConnectionFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(_SQL_INSERT);
			ps.setString(1, mac);
			ps.setString(2, hostname);
			ps.setString(3, company);
			ps.setTimestamp(4, convertStringToTimestamp(timestamp));
			ps.setTimestamp(5, convertStringToTimestamp(localTime));
			ps.setDouble(6, Double.parseDouble(uptime));
			ps.setString(7, raw);
			int intRecordsUpdated = ps.executeUpdate();
			success = (intRecordsUpdated > 0);

		} catch (Throwable e) {
			success = false;
			msg.setCode(getCodeByThrowable(e));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bos));
			msg.setStackTrace(new String(bos.toByteArray()));
			try {
				bos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bos = null;
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		msg.setSuccess(success);
		msg.setDescription(getDescriptionByCode(msg.getCode()));
		return msg;
	}

	private static String getDescriptionByCode(int code) {

		return "To-Do";
	}

	private static int getCodeByThrowable(Throwable e) {

		return 1;
	}

	public static Timestamp convertStringToTimestamp(String str_date) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

			return timeStampDate;
		} catch (ParseException e) {
			System.out.println("Exception :" + e);
			return null;
		}
	}
}
