package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaCharacter extends MetaPlaceable
{
	def value = 'A'
	def widthScale = 1.0
	def spacing = 0.0
	def lineIndex = 0
	def fontIndex = 0
	def faceIndex = 0
	def borderIndex = 0
	def shadowIndex = 0
	
	public MetaCharacter(char c)
	{
		value = c
	}
	
	public MetaCharacter(String s)
	{
		value = (char)s[0]
	}
	
	public MetaCharacter(int i)
	{
		value = (char)i
	}
	
//<MetaCharacter Index="0" Value="78" Left="0.000000" Top="6.117188" Width="13.007813" Height="24.117188" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/>
//<MetaCharacter Index="1" Value="100" Left="13.007813" Top="6.117188" Width="10.019531" Height="24.117188" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/>

	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{		
			output << """<MetaCharacter Index="${index}" Value="${(int)(char)value}" Left="${left.f()}" Top="${top.f()}" Width="${width.f()}" Height="${height.f()}" WidthScale="${widthScale.f()}" Spacing="${spacing.f()}" LineIndex="${lineIndex}" FontIndex="${fontIndex}" FaceIndex="${faceIndex}" BorderIndex="${borderIndex}" ShadowIndex="${shadowIndex}"/>"""
		}
		
		return output.toString()
	}
}