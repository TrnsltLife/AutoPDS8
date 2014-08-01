package org.silcongo.autopds

import org.silcongo.autopds.util.ColorUtils
import org.silcongo.autopds.util.XMLUtils

class ColorObject extends VisualObject
{
	//<COLOROBJECT ID="91" COLOR="8519755" NAME="" MUTE="TRUE"/>
	//-> hex 82004b -> 130,0,75 -> reverse 75, 0, 130

	def mute = true
	def duration = 50000000 //5 seconds
	
	def red = 0
	def green = 0
	def blue = 0

	public ColorObject(uid, r, g, b, dur=-1)
	{
		id = uid
		red = r
		green = g
		blue = b
		src = "(null)"
		name = "$r,$g,$b"
		if(dur != -1)
		{
			duration = dur
		}
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<COLOROBJECT ID="$id" COLOR="${ColorUtils.getColorValue(red, green, blue)}" NAME="${XMLUtils.escape(name)}" MUTE="""
		output << '"' + mute.toString().toUpperCase() + '"'
		output << """ SUBFOLDERNAME=""/>"""
		return output.toString()
	}
	
	public ColorObject clone(dur)
	{
		def newColor = new ColorObject(id, red, green, blue, dur)
	}
}
