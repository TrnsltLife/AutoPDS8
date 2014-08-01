package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaMotion1 extends MetaPlaceablePicture
{
	def index = 0
	def keyframe = 0



	//<MetaMotion1 Index="1" Keyframe="0.000000" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="0" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
	//<MetaMotion1 Index="2" Keyframe="0.333333" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="255" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
	//<MetaMotion1 Index="3" Keyframe="0.666667" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="255" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
	//<MetaMotion1 Index="4" Keyframe="1.000000" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="0" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>

	
	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaMotion1 Index="${index}" Keyframe="${keyframe.f()}" """ + super.toString() + "/>"
		}
		
		return output.toString()
	}
}