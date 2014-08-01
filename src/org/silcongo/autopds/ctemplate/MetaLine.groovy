package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaLine extends MetaPlaceable
{
	def charStart = 0
	def charEnd = 0
	def paragraphIndex = 0
	
	
//<MetaLine Index="0" Left="26.046387" Top="0.000000" Width="106.189453" Height="30.234375" CharStart="0" CharEnd="12" ParagraphIndex="0"/>
//<MetaLine Index="1" Left="0.000000" Top="0.000000" Width="158.282227" Height="30.234375" CharStart="13" CharEnd="34" ParagraphIndex="1"/>
//<MetaLine Index="2" Left="12.528809" Top="0.000000" Width="133.224609" Height="30.234375" CharStart="35" CharEnd="54" ParagraphIndex="2"/>

	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaLine Index="${index}" Left="${left.f()}" Top="${top.f()}" Width="${width.f()}" Height="${height.f()}" CharStart="${charStart}" CharEnd="${charEnd}" ParagraphIndex="${paragraphIndex}"/>"""
		}
		
		return output.toString()
	}
}