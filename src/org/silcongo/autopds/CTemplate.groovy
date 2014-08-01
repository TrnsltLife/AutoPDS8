package org.silcongo.autopds

import org.silcongo.autopds.ctemplate.*
import org.silcongo.autopds.util.*

class CTemplate
{
	def extensionPath = "" //e.g.: C:\Users\Jeremy\Documents\CyberLink\PowerDirector\6.5\MyTitles\Title_000\
	def layers = []
	
	public Object leftShift(it)
	{
		if(it instanceof MetaLayer)
		{
			layers << it
		}
		else
		{
			return layers[-1] << it
		}
		return it
	}
	
	public String toAliasString()
	{
		def output = ""
		layers.each{layer->
			if(layer.mediaType == MetaLayer.TEXT_MEDIA)
			{
				layer.contents[0].characterList.each{c-> output += (char)c.value}
			}
		}
		output = XMLUtils.escape(output)
		return output
	}
	
	public String toEscapedString()
	{
		def output = this.toString()
		output = output.replaceAll(/\r|\n/, "")
		output = output.replaceAll(/<\?xml version="1\.0" encoding="UTF-16"\?>/, """<?xml version="1.0" encoding="UTF-16"?>\r\n""")
		output = output.replaceAll(/</, "&lt;")
		output = output.replaceAll(/>/, "&gt;")
		return output
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<?xml version="1.0" encoding="UTF-16"?>
<CTemplate Version="1.0">
<Header>
<Information Creator="CES_Template Exported XML" CreateDate="2009/8/11" LastModified="2009/8/11"/>
"""

		if(extensionPath)
		{
			output << """<Extension ReferencePath="${new File(extensionPath).getCanonicalPath()}\\"/>"""
		}
		
		output << """
</Header>
<OutputInfo>
<DisplayMask Left="0.000000" Top="0.000000" Width="1.000000" Height="1.000000"/>
</OutputInfo>
"""

		if(layers)
		{
			output << "<LayerList>"
//output += getPurpleBar()
			layers.each{output << it.toString()}
			output << "</LayerList>"
		}
		else
		{
			output << "<LayerList/>"
		}
		output << """\r\n</CTemplate>"""

		return output.toString()
	}
	
	
	
	public String getPurpleBar()
	{
		def output = new StringBuffer()
		output << """<MetaLayer LayerMediaType="2" LayerMediaPtr="0" LayerBeginTime="0.000000" LayerEndTime="1.000000" LayerVisible="true">
<PictureList>
<MetaPicture PosType="0" Interpolation="1">
<SourceList>
<MetaSource Type="1" FileName="C:\\Users\\Jeremy\\Documents\\SIL-Congo\\ScriptureGiftMission\\VideoMediaFiles\\JSW\\src-media\\shared\\overlays\\PurpleBar.png" UseAlpha="true" SrcFixed="true" SrcAspectRatioX="640" SrcAspectRatioY="480" SrcStretchMode="0" DrawFlipType="0" ShowShadowBorderColorKey="true" Subtype="0"/>
</SourceList>
<SourceRegionList/>
<ShapeList>
<MetaShape EnableColorKey="false" ColorKey="16711935" ColorKeyRange="0" ColorKeyCoordinateX="4294967295" ColorKeyCoordinateY="4294967295" MaskFile="" MaskStretchMode="1" MaskStretchFactorX0="0.000000" MaskStretchFactorX1="1.000000" MaskStretchFactorY0="0.000000" MaskStretchFactorY1="1.000000" MaskStretchBGAlpha="0"/>
</ShapeList>
<FaceList>
<MetaFace Enable="true" Alpha="255" BlurRadius="0" EnableBlendingColor="false" BlendingColor="16777215"/>
</FaceList>
<BorderList>
<MetaBorder Enable="false" Size="5" Color1="16777215" Color2="16777215" GradType="0" Alpha="255" BlurRadius="0" BorderType="1" ValueType="0"/>
</BorderList>
<ShadowList>
<MetaShadow EnableShape="true" EnableBorder="true" Color1="0" Color2="0" GradType="7" Alpha="128" BlurRadius="3" OffsetX="3.000000" OffsetY="3.000000" Height="false"/>
</ShadowList>
<Position1List>
<MetaPosition1 CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="255" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
</Position1List>
<Motion1List>
<MetaMotion1 Index="1" Keyframe="0.000000" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="128" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
<MetaMotion1 Index="2" Keyframe="0.333333" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="128" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
<MetaMotion1 Index="3" Keyframe="0.666667" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="128" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
<MetaMotion1 Index="4" Keyframe="1.000000" CenterX="0.508036" CenterY="0.509524" Width="1.019643" Height="1.019048" Degree="0.000000" Alpha="128" TangentVectorX1="1.000000" TangentVectorY1="0.000000" TangentVectorX2="0.000000" TangentVectorY2="0.000000"/>
</Motion1List>
</MetaPicture>
</PictureList>
</MetaLayer>
"""
		return output.toString()
	}
}