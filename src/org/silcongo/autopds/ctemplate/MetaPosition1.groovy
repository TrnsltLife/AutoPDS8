package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaPosition1 extends MetaPlaceablePicture
{
	//<MetaPosition1 CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="255" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>

	
	
	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << "<MetaPosition1 " + super.toString() + "/>"
		}
		
		return output.toString()
	}
}