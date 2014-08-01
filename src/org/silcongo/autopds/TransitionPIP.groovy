package org.silcongo.autopds

import org.silcongo.autopds.util.*

class TransitionPIP extends Transition
{
	def cpicture = new CPicture()
	

	def keepAspectRatio = true
	def borderGradient = false
	def borderSingle = "FFFFFFFF"
	def borderGradientBegin = "FFFFFFFF"
	def borderGradientEnd = "FF000000"
	def borderGradientAngle = 0
	def shadowDistance = 10
	def shadowDirection = 3
	def shadowGradient = false
	def shadowSingle = "FF050505"
	def shadowGradientBegin = "FF000000"
	def shadowGradientEnd = "FF000000"
	def inDirection = 0
	def inOffscreen = false
	def inFade = false
	def outDirection = 0
	def outOffscreen = false
	def outFade = false
	def keepMaskAspectRatio = true
	def dzTemplateID = ""

	public TransitionPIP(fromPIP)
	{
		fromClip = fromPIP
		transitionType = "CES_PlugIn.dll\\DSP_PI_Script"
		start = fromClip.start
		stop = fromClip.stop
		fromPIP.transition = this //give the ClipPIP a reference to this TransitionPIP
	}
	
	public Object leftShift(it)
	{
		return cpicture << it
	}
	
	public void createParamList()
	{
		params = []
		
		use(DataFormat)
		{
		/*
			def cPicture = """<LINEAR TIME="0" VALUE="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-16&quot;?&gt;
&lt;CPicture Version=&quot;1.0&quot;&gt;&lt;Header&gt;&lt;Information Creator=&quot;CPicture Exported XML&quot; CreateDate=&quot;2009/9/4&quot; LastModified=&quot;2009/9/4&quot;/&gt;&lt;/Header&gt;&lt;PictureList&gt;&lt;MetaPicture PosType=&quot;0&quot; Interpolation=&quot;1&quot;&gt;&lt;SourceList&gt;&lt;MetaSource Type=&quot;1&quot; FileName=&quot;&quot; UseAlpha=&quot;true&quot; SrcFixed=&quot;true&quot; SrcAspectRatioX=&quot;0&quot; SrcAspectRatioY=&quot;0&quot; SrcStretchMode=&quot;0&quot; DrawFlipType=&quot;0&quot; ShowShadowBorderColorKey=&quot;true&quot; Subtype=&quot;0&quot;/&gt;&lt;/SourceList&gt;&lt;SourceRegionList/&gt;&lt;ShapeList&gt;&lt;MetaShape EnableColorKey=&quot;false&quot; ColorKey=&quot;0&quot; ColorKeyRange=&quot;0&quot; ColorKeyCoordinateX=&quot;4294967295&quot; ColorKeyCoordinateY=&quot;4294967295&quot; MaskFile=&quot;&quot; MaskStretchMode=&quot;1&quot; MaskStretchFactorX0=&quot;0.000000&quot; MaskStretchFactorX1=&quot;1.000000&quot; MaskStretchFactorY0=&quot;0.000000&quot; MaskStretchFactorY1=&quot;1.000000&quot; MaskStretchBGAlpha=&quot;0&quot;/&gt;&lt;/ShapeList&gt;&lt;FaceList&gt;&lt;MetaFace Enable=&quot;true&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; EnableBlendingColor=&quot;false&quot; BlendingColor=&quot;16777215&quot;/&gt;&lt;/FaceList&gt;&lt;BorderList&gt;&lt;MetaBorder Enable=&quot;false&quot; Size=&quot;3&quot; Color1=&quot;16777215&quot; Color2=&quot;16777215&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; BorderType=&quot;1&quot; ValueType=&quot;0&quot;/&gt;&lt;/BorderList&gt;&lt;ShadowList&gt;&lt;MetaShadow EnableShape=&quot;false&quot; EnableBorder=&quot;false&quot; Color1=&quot;328965&quot; Color2=&quot;328965&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; OffsetX=&quot;7.071068&quot; OffsetY=&quot;7.071068&quot; Height=&quot;false&quot;/&gt;&lt;/ShadowList&gt;&lt;Position1List&gt;&lt;MetaPosition1 CenterX=&quot;0.500000&quot; CenterY=&quot;0.500000&quot; Width=&quot;0.400000&quot; Height=&quot;0.400000&quot; Degree=&quot;0.000000&quot; Alpha=&quot;255&quot; TangentVectorX1=&quot;0.000000&quot; TangentVectorY1=&quot;0.000000&quot; TangentVectorX2=&quot;0.000000&quot; TangentVectorY2=&quot;0.000000&quot;/&gt;&lt;/Position1List&gt;&lt;Motion1List/&gt;&lt;/MetaPicture&gt;&lt;/PictureList&gt;&lt;/CPicture&gt;
"/>"""
		*/
			
			params << new Param("CLT_SCRIPT_BUFFER", "", "", '<LINEAR TIME="0" VALUE="' + cpicture.toEscapedString() + '"/>')
			params << new Param("ID_PIP_KEEP_ASPECTRATIO", "BOOL", "${keepAspectRatio}")
			params << new Param("ID_PIP_BORDER_GRADIENT", "BOOL", "${borderGradient}")
			params << new Param("ID_PIP_BORDER_SINGLE", "RGB", "FFFFFFFF")
			params << new Param("ID_PIP_BORDER_GRADIENT_BEGIN", "RGB", "FFFFFFFF")
			params << new Param("ID_PIP_BORDER_GRADIENT_END", "RGB", "FF000000")
			params << new Param("ID_PIP_BORDER_GRADIENT_ANGLE", "BYTE", "0")
			params << new Param("ID_PIP_SHADOW_DISTANCE", "INT", "10")
			params << new Param("ID_PIP_SHADOW_DIRECTION", "BYTE", "3")
			params << new Param("ID_PIP_SHADOW_GRADIENT", "BOOL", "${shadowGradient}")
			params << new Param("ID_PIP_SHADOW_SINGLE", "RGB", "FF050505")
			params << new Param("ID_PIP_SHADOW_GRADIENT_BEGIN", "RGB", "FF000000")
			params << new Param("ID_PIP_SHADOW_GRADIENT_END", "RGB", "FF000000")
			params << new Param("ID_PIP_SHADOW_GRADIENT_ANGLE", "BYTE", "0")
			params << new Param("ID_PIP_IN_DIRECTION", "BYTE", "0")
			params << new Param("ID_PIP_IN_OFFSCREEN", "BOOL", "${inOffscreen}")
			params << new Param("ID_PIP_IN_FADE", "BOOL", "${inFade}")
			params << new Param("ID_PIP_OUT_DIRECTION", "BYTE", "0")
			params << new Param("ID_PIP_OUT_OFFSCREEN", "BOOL", "${outOffscreen}")
			params << new Param("ID_PIP_OUT_FADE", "BOOL", "${outFade}")
			params << new Param("ID_PIP_KEEP_MASK_ASPECT_RATIO", "BOOL", "${keepMaskAspectRatio}")
			params << new Param("ID_PIP_DZTEMPLATEID", "STRING", "")
		}
	}
}