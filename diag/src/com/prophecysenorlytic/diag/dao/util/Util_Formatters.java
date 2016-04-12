package com.prophecysenorlytic.diag.dao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util_Formatters {

	public static String getFormattedMAC(String unformattedMAC) {
		final char divisionChar = ':';// change to '-' if you want

		String formattedMAC = unformattedMAC.replaceAll("(.{2})", "$1" + divisionChar).substring(0, 17);
		return formattedMAC.toUpperCase();
	}
	public static String formatDate(Date date,String formatString){
		SimpleDateFormat format=new SimpleDateFormat(formatString);
		return format.format(date);
	}
	public static String formatNow(String formatString){
		return formatDate(new Date(),formatString);
	}
}
