package org.silcongo.autopds

import org.silcongo.autopds.util.*

class EffectTitle extends Effect
{
	def ctemplate = new CTemplate()

	public EffectTitle()
	{
		name = "AEFF_TEMPLATE"
	}
	
	public EffectTitle(dur)
	{
		name = "AEFF_TEMPLATE"
		duration = dur
	}

/*
<EFFECT ID="1" NAME="AEFF_TEMPLATE" GUID="" START="312645979" STOP="690023356" MUTE="FALSE">
<PARAM NAME="TEMPLATE_BUFFER_SIZE" VALUETYPE="LONGLONG" VALUE="9944"/>
<PARAM NAME="TEMPLATE_OUTPOINT" VALUETYPE="LONGLONG" VALUE="377377377"/>
<PARAM NAME="TEMPLATE_INPOINT" VALUETYPE="LONGLONG" VALUE="0"/>
<PARAM NAME="TEMPLATE_BUFFER" VALUETYPE="PBYTE">
<TEMPLATE_BUFFER>
&lt;?xml version="1.0" encoding="UTF-16"?&gt;
&lt;CTemplate Version="1.0"&gt;&lt;Header&gt;&lt;Information Creator="CES_Template Exported XML" CreateDate="2009/8/18" LastModified="2009/8/18"/&gt;&lt;/Header&gt;&lt;OutputInfo&gt;&lt;DisplayMask Left="0.000000" Top="0.000000" Width="1.000000" Height="1.000000"/&gt;&lt;/OutputInfo&gt;&lt;LayerList&gt;&lt;MetaLayer LayerMediaType="1" LayerMediaPtr="0" LayerBeginTime="0.000000" LayerEndTime="1.000000" LayerVisible="true"&gt;&lt;StringList&gt;&lt;MetaString Index="0" Left="97.589844" Top="199.750000" Width="124.820313" Height="20.250000" ParagraphCount="0" WidthLimit="1000000000.000000" RotateDegree="0.000000" Vertical="false" PathID="PATH_NOEFFECT" PathProgress="1.000000" MaskFile=""&gt;&lt;CharList&gt;&lt;MetaCharacter Index="0" Value="72" Left="0.000000" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="1" Value="101" Left="9.601563" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="2" Value="108" Left="19.203125" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="3" Value="108" Left="28.804688" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="4" Value="111" Left="38.406250" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="5" Value="32" Left="48.007813" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="6" Value="119" Left="57.609375" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="7" Value="111" Left="67.210938" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="8" Value="114" Left="76.812500" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="9" Value="108" Left="86.414063" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="10" Value="100" Left="96.015625" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="11" Value="32" Left="105.617188" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;MetaCharacter Index="12" Value="50" Left="115.218750" Top="2.125000" Width="9.601563" Height="18.125000" WidthScale="1.000000" Spacing="0.000000" LineIndex="0" FontIndex="0" FaceIndex="0" BorderIndex="0" ShadowIndex="0"/&gt;&lt;/CharList&gt;&lt;LineList&gt;&lt;MetaLine Index="0" Left="0.000000" Top="0.000000" Width="124.820313" Height="20.250000" CharStart="0" CharEnd="12" ParagraphIndex="0"/&gt;&lt;/LineList&gt;&lt;ParagraphList&gt;&lt;MetaParagraph Index="0" Left="0.000000" Top="0.000000" Width="124.820313" Height="20.250000" LineStart="0" LineEnd="0" Align="0" LineSpace="0.000000" BeforeSpace="0.000000" AfterSpace="0.000000" LeftIndent="0.000000" RightIndent="0.000000"/&gt;&lt;/ParagraphList&gt;&lt;FontList&gt;&lt;MetaFont Index="0" LogFont="00F8FFFF000000000000000000000000BC020000000000010000000043006F007500720069006500720020004E0065007700000000000000000000000000000000000000000000000000000000000000000000000000000000000000" SizeRatio="0.007813" Ascent="10.132813" Descent="-3.343750" IntLead="2.125000" ExtLead="0.000000" StrikeoutPos="4.140625" StrikeoutSize="0.796875" UnderlinePos="-3.726563" UnderlineSize="1.601563"/&gt;&lt;/FontList&gt;&lt;FaceList&gt;&lt;MetaFace Index="0" Enable="true" Color1="16777215" Color2="16777215" GradType="7" Alpha="255" BlurRadius="0"/&gt;&lt;/FaceList&gt;&lt;BorderList&gt;&lt;MetaBorder Index="0" Enable="true" Size="5" Color1="0" Color2="0" GradType="7" Alpha="255" BlurRadius="0"/&gt;&lt;/BorderList&gt;&lt;ShadowList&gt;&lt;MetaShadow Index="0" EnableFace="false" EnableBorder="false" Color1="0" Color2="0" GradType="7" Alpha="128" BlurRadius="3" OffsetX="1.414214" OffsetY="1.414214" Height="false"/&gt;&lt;/ShadowList&gt;&lt;PathList/&gt;&lt;/MetaString&gt;&lt;/StringList&gt;&lt;/MetaLayer&gt;&lt;/LayerList&gt;&lt;/CTemplate&gt;
</TEMPLATE_BUFFER>
</PARAM>
<PARAM NAME="TEMPLATE_TITLE_TYPE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="TEMPLATE_PROGRESS" VALUETYPE="FLOAT" VALUE="1.000000"/>
<PARAM NAME="TEMPLATE_ALIAS" VALUETYPE="CHAR" VALUE="Hello world 2"/>
<PARAM NAME="ID_TITLE_DZTEMPLATEID" VALUETYPE="STRING" VALUE=""/>
</EFFECT>
*/


	public Object leftShift(it)
	{
		return ctemplate << it
	}
	
	
	public void createParamList()
	{
		def template = ctemplate.toEscapedString()
		def aLittleBitExtra = 100
		def bytesize = (template.size() * 2) + aLittleBitExtra
		template = "<TEMPLATE_BUFFER>" + template + "</TEMPLATE_BUFFER>"

		params = []
		params << new Param("TEMPLATE_BUFFER_SIZE", "LONGLONG", bytesize)
		params << new Param("TEMPLATE_OUTPOINT", "LONGLONG", duration)
		params << new Param("TEMPLATE_INPOINT", "LONGLONG", 0)
		params << new Param("TEMPLATE_BUFFER", "PBYTE", null, template)
		params << new Param("TEMPLATE_TITLE_TYPE", "UNSIGNED_INT", 0)
		params << new Param("TEMPLATE_PROGRESS", "FLOAT", 1.0)
		params << new Param("TEMPLATE_ALIAS", "CHAR", ctemplate.toAliasString())
		params << new Param("ID_TITLE_DZTEMPLATEID", "STRING", "")
	}
}