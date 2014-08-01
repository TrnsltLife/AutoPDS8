package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*
import javax.imageio.*

class MetaSource
{
	public static final int IMAGE_TYPE = 1 // ?
	def type = IMAGE_TYPE
	def fileName = ""
	def useAlpha = true
	def srcFixed = true
	def srcAspectRatioX = 0
	def srcAspectRatioY = 0
	def srcStretchMode = 0
	def drawFlipType = 0
	def showShadowBorderColorKey = true
	def subtype = 0
	
	public MetaSource(file)
	{
		setFileName(file)
	}
	
	public void setFileName(file)
	{
		if(!(file instanceof File))
		{
			file = new File(file)
		}
		fileName = file.getCanonicalPath()
		initAspectRatio(file)
	}
	
	public void initAspectRatio(file)
	{
		def img = ImageIO.read(file)
		srcAspectRatioX = img.width
		srcAspectRatioY = img.height
	}

	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaSource Type="${type}" FileName="${fileName}" UseAlpha="${useAlpha.b()}" SrcFixed="${srcFixed.b()}" SrcAspectRatioX="${srcAspectRatioX.i()}" SrcAspectRatioY="${srcAspectRatioY.i()}" SrcStretchMode="${srcStretchMode}" DrawFlipType="${drawFlipType}" ShowShadowBorderColorKey="${showShadowBorderColorKey.b()}" Subtype="${subtype}"/>"""
		}
		
		return output.toString()
	}
}