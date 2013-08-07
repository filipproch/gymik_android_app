package com.jacktech.gymik;

import java.util.Calendar;

public class UtilityClass {

	public static String getDateReadable(String config) {
		String date = "nikdy";
		if(config != null && isLong(config)){
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.parseLong(config));
			Calendar now = Calendar.getInstance();
			if(c.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH))
				date = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
			else
				date = c.get(Calendar.DAY_OF_MONTH)+"."+(c.get(Calendar.MONTH)+1)+"."+c.get(Calendar.YEAR);
		}
		return date;
	}

	private static boolean isLong(String config) {
		try{
			Long.parseLong(config);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
