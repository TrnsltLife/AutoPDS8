package org.silcongo.autopds.util

class ColorUtils
{
	public static int getColorValue(red, green, blue)
	{
		def r = Integer.toHexString(red).padLeft(2, "0")
		def g = Integer.toHexString(green).padLeft(2, "0")
		def b = Integer.toHexString(blue).padLeft(2, "0")
		def hex = b + g + r
		def val = Integer.parseInt(hex, 16).intValue()
		return val
	}
	
	public static int getColorValue(red, green, blue, alpha)
	{
		def r = Integer.toHexString(red).padLeft(2, "0")
		def g = Integer.toHexString(green).padLeft(2, "0")
		def b = Integer.toHexString(blue).padLeft(2, "0")
		def a = Integer.toHexString(alpha).padLeft(2, "0")
		def hex = a + b + g + r
		def val = Integer.parseInt(hex, 16).intValue()
		return val
	}
	
	public static int getColorHex(red, green, blue)
	{
		def r = Integer.toHexString(red).padLeft(2, "0")
		def g = Integer.toHexString(green).padLeft(2, "0")
		def b = Integer.toHexString(blue).padLeft(2, "0")
		def hex = b + g + r
		return hex
	}
	
	public static int getColorHex(red, green, blue, alpha)
	{
		def r = Integer.toHexString(red).padLeft(2, "0")
		def g = Integer.toHexString(green).padLeft(2, "0")
		def b = Integer.toHexString(blue).padLeft(2, "0")
		def a = Integer.toHexString(alpha).padLeft(2, "0")
		def hex = a + b + g + r
		return hex
	}
}