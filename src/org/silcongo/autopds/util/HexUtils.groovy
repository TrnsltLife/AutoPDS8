package org.silcongo.autopds.util

class HexUtils
{
	public static Float decodeFloat(hex)
	{
		def f = null
		def section = (hex[6..7] + hex[4..5] + hex[2..3] + hex[0..1])
		try
		{
			f = Float.intBitsToFloat(Long.parseLong(section, 16).intValue())
		}
		catch(Exception e){}
		return f
	}

	public static List decodeFloats(hex)
	{
		def floats = []
		while(hex)
		{
			def f = decodeFloat(hex[0..7])
			floats << f
			hex -= hex[0..7]
		}
		return floats
	}
	
	
	public static String encodeFloat(f)
	{
		def hex = Integer.toHexString(Float.floatToIntBits(f as float))
		hex = hex.padLeft(8, "0")
		hex = hex[6..7] + hex[4..5] + hex[2..3] + hex[0..1]
		return hex
	}
	
	public static String encodeFloats(floatList)
	{
		def hex = ""
		floatList.each{f->
			hex += encodeFloat(f)
		}
		return hex.toUpperCase()
	}
	
	public static String encodeString(s)
	{
		def hex = ""
		s.each{c->
			c = Integer.toHexString((int)(char)c)
			c = c.padLeft(4,"0")
			c = c[2..3] + c[0..1]
			hex += c
		}
		return hex.toUpperCase()
	}
	
	public static String decodeString(hex)
	{
		String s = ""
		while(hex)
		{
			def c = hex[0..3]
			c = c[2..3] + c[0..1]
			c = (char)Integer.parseInt(c, 16)
			s += c
			hex -= hex[0..3]
		}
		return s
	}
}