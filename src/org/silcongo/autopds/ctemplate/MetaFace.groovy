package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaFace extends MetaDecorator
{
	def enable = true
	def enableBlendingColor = false
	def blendingColor = -1
	
	//Use a HashMap to instantiate, i.e. new MetaFace(color1:[r,g,b], color2:[r2,g2,b2], alpha:128, blurRadius:3)
	
	//CPICTURE
	//<MetaFace Enable="true" Alpha="255" BlurRadius="0" EnableBlendingColor="false" BlendingColor="16777215"/>

	//CTEMPLATE
	//<MetaFace Index="0" Enable="true" Color1="16777215" Color2="16777215" GradType="7" Alpha="255" BlurRadius="0"/>
	
	public void setBlendingColor(color)
	{
		if(color instanceof List)
		{
			blendingColor = ColorUtils.getColorValue(color[0], color[1], color[2])
		}
		else
		{
			blendingColor = color
		}
	}
	
	public String toString()
	{
		if(color2 == null){color2 = color1}
		def output = new StringBuilder()
		use(DataFormat)
		{
			if(enableBlendingColor || blendingColor != -1)
			{
				//MetaFace as used in CPICTURE
				if(blendingColor == -1){blendingColor = WHITE}
				output << """<MetaFace Enable="${enable}" Alpha="${alpha}" BlurRadius="${blurRadius}" EnableBlendingColor="${enableBlendingColor.b()}" BlendingColor="${blendingColor.b()}"/>"""
			}
			else
			{
				//MetaFace as used in CTEMPLATE
				output << """<MetaFace Index="${index}" Enable="${enable}" Color1="${color1}" Color2="${color2}" GradType="${gradType}" Alpha="${alpha}" BlurRadius="${blurRadius}"/>"""
			}
		}
		return output.toString()
	}

}