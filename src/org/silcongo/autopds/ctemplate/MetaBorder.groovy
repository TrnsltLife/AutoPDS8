package org.silcongo.autopds.ctemplate

class MetaBorder extends MetaDecorator
{
	def enable = true
	def size = 3
	def borderType = -1
	def valueType = -1
	
	//Use a HashMap to instantiate, i.e. new MetaBorder(size:5, color1:[r,g,b], color2:[r2,g2,b2], alpha:128, blurRadius:3)

	
	public void setSize(s)
	{
		s = (int)s
		if(s < 0){r = 0}
		else if(s > 10){s = 10}
		blurRadius = s
	}
	
	//CPICTURE
	//<MetaBorder Enable="false" Size="5" Color1="16777215" Color2="16777215" GradType="0" Alpha="255" BlurRadius="0" BorderType="1" ValueType="0"/>
	
	//CTEMPLATE
	//<MetaBorder Index="0" Enable="true" Size="5" Color1="0" Color2="0" GradType="7" Alpha="255" BlurRadius="0"/>
	
	public String toString()
	{
		if(color2 == null){color2 = color1}
		def output = new StringBuilder()
		if(borderType != -1 || valueType != -1)
		{
			//MetaBorder as used in CPICTURE
			if(borderType == -1){borderType = 1}
			if(valueType == -1){valueType = 0}
			output << """<MetaBorder Index="${index}" Enable="${enable}" Size="${size}" Color1="${color1}" Color2="${color2}" GradType="${gradType}" Alpha="${alpha}" BlurRadius="${blurRadius}" BorderType="${borderType}" ValueType="${valueType}"/>"""
		}
		else
		{
			//MetaBorder as used in CTEMPLATE
			output << """<MetaBorder Index="${index}" Enable="${enable}" Size="${size}" Color1="${color1}" Color2="${color2}" GradType="${gradType}" Alpha="${alpha}" BlurRadius="${blurRadius}"/>"""
		}
		return output.toString()
	}

}