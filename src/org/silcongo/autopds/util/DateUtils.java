package org.silcongo.autopds.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DateUtils
{
	static public String now()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(new Date());
	}
	
	static public String now(String format)
	{
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());	
	}
}