package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

abstract class MetaDecorator
{
	public static final int WHITE = 16777215 //White - 0xFFFFFF
	public static final int BLACK = 0 //Black - 0x000000
	def index = 0
	def color1 = BLACK
	def color2 = null
	def gradType = 7
	def alpha = 255
	def blurRadius = 0
	
	public void setColor1(color)
	{
		if(color instanceof List)
		{
			color1 = ColorUtils.getColorValue(color[0], color[1], color[2])
		}
		else
		{
			color1 = color
		}
	}
	
	public void setColor2(color)
	{
		if(color instanceof List)
		{
			color2 = ColorUtils.getColorValue(color[0], color[1], color[2])
		}
		else
		{
			color2 = color
		}
	}
	
	public void setAlpha(a)
	{
		a = (int)a
		if(a < 0){a = 0}
		else if(a > 255){a = 255}
		alpha = a
	}
	
	public void setBlurRadius(r)
	{
		r = (int)r
		if(r < 0){r = 0}
		else if(r > 10){r = 10}
		blurRadius = r
	}
}