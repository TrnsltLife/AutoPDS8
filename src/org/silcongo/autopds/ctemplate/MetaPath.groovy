package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaPath
{
	//<MetaPath Index="1" KeyFrame="0.000000" PathID="PATH_NOEFFECT" PathProgress="0.000000"/>
	//<MetaPath Index="2" KeyFrame="0.333333" PathID="PATH_NOEFFECT" PathProgress="1.000000"/>
	//<MetaPath Index="3" KeyFrame="0.666667" PathID="PATH_NOEFFECT" PathProgress="1.000000"/>
	//<MetaPath Index="4" KeyFrame="1.000000" PathID="PATH_NOEFFECT" PathProgress="0.000000"/>	
	
	def index
	def keyFrame = 0.0
	def pathID = "PATH_NOEFFECT"
	def pathProgress = 0.0
	
	
	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaPath Index="${index}" KeyFrame="${keyFrame.f()}" PathID="${pathID}" PathProgress="${pathProgress.f()}"/>"""
		}
		
		return output.toString()
	}
}