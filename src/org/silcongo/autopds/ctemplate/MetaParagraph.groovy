package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaParagraph extends MetaPlaceable
{
	static final int ALIGN_LEFT = 0
	static final int ALIGN_RIGHT = 2
	static final int ALIGN_CENTER = 6
	
	def lineStart = 0
	def lineEnd = 0
	def align = ALIGN_CENTER
	def lineSpace = 0.0
	def beforeSpace = 0.0
	def afterSpace = 0.0
	def leftIndent = 0.0
	def rightIndent = 0.0
	
//<MetaParagraph Index="0" Left="0.000000" Top="0.000000" Width="106.189453" Height="30.234375" LineStart="0" LineEnd="0" Align="6" LineSpace="0.000000" BeforeSpace="0.000000" AfterSpace="0.000000" LeftIndent="0.000000" RightIndent="0.000000"/>
//<MetaParagraph Index="1" Left="0.000000" Top="30.234375" Width="158.282227" Height="30.234375" LineStart="1" LineEnd="1" Align="6" LineSpace="0.000000" BeforeSpace="0.000000" AfterSpace="0.000000" LeftIndent="0.000000" RightIndent="0.000000"/>
//<MetaParagraph Index="2" Left="0.000000" Top="60.468750" Width="133.224609" Height="30.234375" LineStart="2" LineEnd="2" Align="6" LineSpace="0.000000" BeforeSpace="0.000000" AfterSpace="0.000000" LeftIndent="0.000000" RightIndent="0.000000"/>

	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaParagraph Index="${index}" Left="${left.f()}" Top="${top.f()}" Width="${width.f()}" Height="${height.f()}" LineStart="${lineStart}" LineEnd="${lineEnd}" Align="${align}" LineSpace="${lineSpace.f()}" BeforeSpace="${beforeSpace.f()}" AfterSpace="${afterSpace.f()}" LeftIndent="${leftIndent.f()}" RightIndent="${rightIndent.f()}"/>"""
		}
		
		return output.toString()
	}
}