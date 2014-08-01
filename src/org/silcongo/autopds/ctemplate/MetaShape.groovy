package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaShape
{
	//TODO: Figure out what any of this does!
	def enableColorKey = false
	def colorKey = ColorUtils.getColorValue(255, 0, 255)
	def colorKeyRange = 0
	def colorKeyCoordinateX = 4294967295 //this is 0xFFFFFFFF
	def colorKeyCoordinateY = 4294967295 //this is 0xFFFFFFFF
	def maskFile = ""
	def maskStretchMode = 1
	def maskStretchFactorX0 = 0.0
	def maskStretchFactorX1 = 1.0
	def maskStretchFactorY0 = 0.0
	def maskStretchFactorY1 = 1.0
	def maskStretchBGAlpha = 0
	
	public void maskFile(file)
	{
		if(!file instanceof File)
		{
			file = new File(file)
		}
		maskFile = file.getCanonicalPath()
	}
	
	public String toString()
	{
		//<MetaShape EnableColorKey="false" ColorKey="16711935" ColorKeyRange="0" ColorKeyCoordinateX="4294967295" ColorKeyCoordinateY="4294967295" MaskFile="" MaskStretchMode="1" MaskStretchFactorX0="0.000000" MaskStretchFactorX1="1.000000" MaskStretchFactorY0="0.000000" MaskStretchFactorY1="1.000000" MaskStretchBGAlpha="0"/>

		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaShape EnableColorKey="${enableColorKey.b()}" ColorKey="${colorKey.i()}" ColorKeyRange="${colorKeyRange.i()}" ColorKeyCoordinateX="${colorKeyCoordinateX.i()}" ColorKeyCoordinateY="${colorKeyCoordinateY.i()}" MaskFile="${maskFile}" MaskStretchMode="${maskStretchMode}" MaskStretchFactorX0="${maskStretchFactorX0.f()}" MaskStretchFactorX1="${maskStretchFactorX1.f()}" MaskStretchFactorY0="${maskStretchFactorY0.f()}" MaskStretchFactorY1="${maskStretchFactorY1.f()}" MaskStretchBGAlpha="${maskStretchBGAlpha.i()}"/>"""
		}
		
		return output.toString()
	}
}