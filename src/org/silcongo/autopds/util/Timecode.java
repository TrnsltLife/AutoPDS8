package org.silcongo.autopds.util;

public class Timecode
{	
	public static long seconds(long s)
	{
		return (long)(s * 10000000);
	}
	
	public static long seconds(Long s)
	{
		return (long)(s.longValue() * 10000000);
	}

	public static long seconds(double s)
	{
		return (long)(s * 10000000);
	}
	
	public static long seconds(Double s)
	{
		return (long)(s.doubleValue() * 10000000);
	}
}