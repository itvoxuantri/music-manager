package com.tma.spring.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static String convertDateToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String result = dateFormat.format(date);
		return result;
	}

	public static String convertDatetimeToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String result = dateFormat.format(date);
		return result;
	}
}
