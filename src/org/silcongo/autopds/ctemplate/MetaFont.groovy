package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

import java.awt.*
import java.awt.font.*
import java.awt.image.*

class MetaFont
{
	//Bold:           <MetaFont Index="0" LogFont="00F8FFFF000000000000000000000000BC020000000000010000000041007200690061006C00200055006E00690063006F006400650020004D0053000000000000000000000000000000000000000000000000000000000000000000" SizeRatio="0.004883" Ascent="7.280273" Descent="-2.099609" IntLead="3.398438" ExtLead="0.000000" StrikeoutPos="3.085938" StrikeoutSize="0.498047" UnderlinePos="-1.000977" UnderlineSize="0.498047"/>
	//Regular Italic: <MetaFont Index="0" LogFont="00F8FFFF00000000000000000000000090010000010000010000000041007200690061006C00200055006E00690063006F006400650020004D0053000000000000000000000000000000000000000000000000000000000000000000" SizeRatio="0.004883" Ascent="7.280273" Descent="-2.099609" IntLead="3.398438" ExtLead="0.000000" StrikeoutPos="3.085938" StrikeoutSize="0.498047" UnderlinePos="-1.000977" UnderlineSize="0.498047"/>

	def index = 0
	def font = ""
	def bold = false
	def italic = false
	def size = 8
	
	//apparently these are optional
	def ascent = 0.0
	def descent = 0.0
	def intLead = 0.0
	def extLead = 0.0
	def strikeoutPos = 0.0
	def strikeoutSize = 0.0
	def underlinePos = 0.0
	def underlineSize = 0.0
	
	BufferedImage bi
	Graphics2D g2
	Font actualFont
	FontRenderContext frc
	FontMetrics fontMetrics
	LineMetrics lineMetrics
	def height = 0.0
	
	public MetaFont(fontname, fontsize)
	{
		this(fontname, fontsize, false, false)
	}
	
	public MetaFont(fontname, fontsize, b, i)
	{
		font = fontname
		size = fontsize
		bold = b
		italic = i
		createMetrics()
	}
	
	public MetaFont(Map m)
	{
		m.each{key, value->this[key] = value}
		createMetrics()
	}
	
	public void createMetrics()
	{
		def style = Font.PLAIN
		if(bold){style += Font.BOLD}
		if(italic){style += Font.ITALIC}
		
		actualFont = new Font(font, style, size)
		
		bi = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB)
		g2 = bi.createGraphics()
		
		frc = g2.getFontRenderContext()
		lineMetrics = actualFont.getLineMetrics("Sample Text NA\u0303dza\u0254\u0303", frc)
		ascent = lineMetrics.getAscent()
		descent = lineMetrics.getDescent()
		height = lineMetrics.getHeight()
		
		fontMetrics = g2.getFontMetrics(actualFont)
	}
	
	public double getSizeRatio()
	{
		return size * 0.0004883
	}
	
	public String getLogFont()
	{
		def output = "00F8FFFF000000000000000000000000" + 
			(bold ? "BC02" : "9001") + 
			"0000" +
			(italic ? "0100" : "0000") + 
			"000100000000" +
			HexUtils.encodeString(font).padRight(128, "0")
		return output
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaFont Index="$index" LogFont="${getLogFont()}" SizeRatio="${getSizeRatio().f()}" Ascent="${ascent.f()}" Descent="${descent.f()}" IntLead="${intLead.f()}" ExtLead="${extLead.f()}" StrikeoutPos="${strikeoutPos.f()}" StrikeoutSize="${strikeoutSize.f()}" UnderlinePos="${underlinePos.f()}" UnderlineSize="${underlineSize.f()}"/>"""
		}
		
		return output.toString()
	}
}
