package org.silcongo.autopds.util

import java.text.*

class DataFormat
{
	static NumberFormat nfFloat = NumberFormat.getInstance();
	static {
		nfFloat.setMinimumFractionDigits(6);
		nfFloat.setMaximumFractionDigits(6);
		nfFloat.setMinimumIntegerDigits(1);
		nfFloat.setGroupingUsed(false);
	}
	
	static NumberFormat nfInt = NumberFormat.getInstance()
	static {
		nfInt.setMinimumFractionDigits(0);
		nfInt.setMaximumFractionDigits(0);
		nfInt.setGroupingUsed(false);
	}
	
	static String f(f)
	{
		if(f == "omit")
		{
			return ""
		}
		else
		{
			return nfFloat.format(f)
		}
	}
	
	static String i(i)
	{
		if(i == "omit")
		{
			return ""
		}
		else
		{
			return nfInt.format(i)
		}
	}
	
	static String b(b)
	{
		if(b != "false" && b != "FALSE" && b){return "true"}
		return "false"
	}
	
	static String B(b)
	{
		if(b != "false" && b != "FALSE" && b){return "TRUE"}
		return "FALSE"
	}
}