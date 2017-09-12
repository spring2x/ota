package com.iot.ota_upgrade.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String DateFormat(long currentTimeMills){
		Date date = new Date(currentTimeMills);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
}
