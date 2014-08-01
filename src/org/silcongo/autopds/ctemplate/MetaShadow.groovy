package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaShadow extends MetaDecorator
{
	def enableFace = true
	def enableBorder = true
	def enableShape = null
	def offsetX = 1.5
	def offsetY = 1.5
	def height = false
	
	//Use a HashMap to instantiate, i.e. new MetaShadow(color1:[r,g,b], offsetX:2.5, offsetY:-2.5)

	
	//<MetaShadow Index="0" EnableFace="false" EnableBorder="false" Color1="0" Color2="0" GradType="7" Alpha="128" BlurRadius="3" OffsetX="1.590990" OffsetY="1.590990" Height="false"/>

	public String toString()
	{
		if(color2 == null){color2 = color1}

		def output = new StringBuilder()
		
		use(DataFormat)
		{
			if(enableShape != null)
			{
				//MetaShadow as used in CPICTURE
				output << """<MetaShadow EnableShape="${enableShape.b()}" EnableBorder="${enableBorder.b()}" Color1="${color1}" Color2="${color2}" GradType="${gradType}" Alpha="${alpha}" BlurRadius="${blurRadius}" OffsetX="${offsetX.f()}" OffsetY="${offsetY.f()}" Height="${height}"/>"""
			}
			else
			{
				//MetaShadow as used in CTEMPLATE
				output << """<MetaShadow Index="${index}" EnableFace="${enableFace.b()}" EnableBorder="${enableBorder.b()}" Color1="${color1}" Color2="${color2}" GradType="${gradType}" Alpha="${alpha}" BlurRadius="${blurRadius}" OffsetX="${offsetX.f()}" OffsetY="${offsetY.f()}" Height="${height}"/>"""
			}
		}
		
		return output.toString()
	}
}