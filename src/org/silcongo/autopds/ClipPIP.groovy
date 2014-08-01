package org.silcongo.autopds

import org.silcongo.autopds.util.*

class ClipPIP extends Clip
{
	public static final int UNKNOWN_TYPE_MAYBE_VIDEO_TYPE = 1
	public static final int IMAGE_TYPE = 4
	public static final int UNKNOWN_TYPE_MAYBE_IMAGE = 4096
	
	def panovisionMode = 0
	
	//ClipExtraData
	def objectType = UNKNOWN_TYPE_MAYBE_IMAGE
	
	//Related transition
	def transition
	
	public ClipPIP(uid, mediaObj, startTime, dur=-1)
	{
		id = uid
		mediaObject = mediaObj

		aspectRatioX = 0
		aspectRatioY = 0
		if(mediaObject instanceof VideoObject)
		{
			objectType = VIDEO_TYPE
			aspectRatioX = -1
			aspectRatioY = -1
		}

		idRef = mediaObject.id
		name = mediaObject.name
		oriFileName = mediaObject.src
		alias = mediaObject.name

		if(dur == -1){mediaDuration = mediaObject.duration}
		else{mediaDuration = dur}
		duration = mediaDuration

		start = startTime
		
		stretchMode = "STRETCH"
		
		recalculateStopTimes()
	}
	
	public void recalculateStopTimes()
	{
		mediaStart = startOffset
		mediaStop = mediaDuration - stopOffset
		
		duration = mediaStop - mediaStart
		
		stop = start + duration
		
		if(mediaObject instanceof VideoObject)
		{
			sceneStop = mediaDuration
		}
		
		if(transition != null)
		{
			transition.recalculateTimes()
		}
	}
	
	
	public String toString()
	{
		def output = new StringBuilder()

		use(DataFormat)
		{
			output << """<CLIP ID="$id" IDREF="$idRef" NAME="$name" START="${start.i()}" STOP="${stop.i()}" SCENESTART="${sceneStart.i()}" SCENESTOP="${sceneStop.i()}" MEDIASTART="${mediaStart.i()}" MEDIASTOP="${mediaStop.i()}" MUTE="${mute.B()}" STRETCHMODE="$stretchMode" PANOVISIONMODE="$panovisionMode">
<CLIP_EXTRA_DATA ID="0">
<PARAM NAME="PINP_EAGLEVISION_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="PINP_EAGLEVISION_MODE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="PINP_STABLIZER_MODE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="PINP_STABLIZER_TOLERANCE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="PINP_STABLIZER_ANGLE" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_ASPECTRATIO_X" VALUETYPE="INT" VALUE="$aspectRatioX"/>
<PARAM NAME="VIDEO_ASPECTRATIO_Y" VALUETYPE="INT" VALUE="$aspectRatioY"/>
<PARAM NAME="PINP_DENOISE_MODE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="PINP_DENOISE_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="PINP_REVERSE_FLAG" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="PINP_TTMOTION_FLAG" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="PINP_TTHD_MODE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="PINP_ROTATE_ANGLE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="PINP_TTHD_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="PINP_USER_DATA_ALIAS" VALUETYPE="STRING" VALUE="$alias"/>
<PARAM NAME="PINP_USER_DATA_ORI_FILENAME" VALUETYPE="STRING" VALUE="$oriFileName"/>
<PARAM NAME="PINP_USER_DATA_APPLY_MAGIC_CLEAN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="PINP_USER_DATA_AUTOFIX_FLAG" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="PINP_USER_DATA_REFOCUS_LEVEL" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="PINP_USER_DATA_APPLY_MAGIC_ENHANCE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="PINP_USER_DATA_STABILIZER_LEVEL" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="PINP_USER_DATA_IMAGE_RESIZE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="PINP_USER_DATA_OBJECT_TYPE" VALUETYPE="INT" VALUE="$objectType"/>
<PARAM NAME="PINP_USER_DATA_POWER_TOOL" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="PINP_USER_DATA_SPEED_FACTOR" VALUETYPE="FLOAT" VALUE="1.000000"/>
<CLIP_EXTRA_DATA_MAGICEFFECT ID="0"/>
</CLIP_EXTRA_DATA>
</CLIP>"""
		}
		
		return output.toString()
	}
	
/*
	public String toStringTransition()
	{
		def output = ""
		
		use(DataFormat)
		{
			output += """<TRANSITION ID="0" GUID="CES_PlugIn.dll\\DSP_PI_Script" START="${start.i()}" STOP="${stop.i()}">
<PARAM NAME="CLT_SCRIPT_BUFFER" VALUETYPE="" VALUE="">
<LINEAR TIME="0" VALUE="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-16&quot;?&gt;
&lt;CPicture Version=&quot;1.0&quot;&gt;&lt;Header&gt;&lt;Information Creator=&quot;CPicture Exported XML&quot; CreateDate=&quot;2009/9/4&quot; LastModified=&quot;2009/9/4&quot;/&gt;&lt;/Header&gt;&lt;PictureList&gt;&lt;MetaPicture PosType=&quot;0&quot; Interpolation=&quot;1&quot;&gt;&lt;SourceList&gt;&lt;MetaSource Type=&quot;1&quot; FileName=&quot;&quot; UseAlpha=&quot;true&quot; SrcFixed=&quot;true&quot; SrcAspectRatioX=&quot;0&quot; SrcAspectRatioY=&quot;0&quot; SrcStretchMode=&quot;0&quot; DrawFlipType=&quot;0&quot; ShowShadowBorderColorKey=&quot;true&quot; Subtype=&quot;0&quot;/&gt;&lt;/SourceList&gt;&lt;SourceRegionList/&gt;&lt;ShapeList&gt;&lt;MetaShape EnableColorKey=&quot;false&quot; ColorKey=&quot;0&quot; ColorKeyRange=&quot;0&quot; ColorKeyCoordinateX=&quot;4294967295&quot; ColorKeyCoordinateY=&quot;4294967295&quot; MaskFile=&quot;&quot; MaskStretchMode=&quot;1&quot; MaskStretchFactorX0=&quot;0.000000&quot; MaskStretchFactorX1=&quot;1.000000&quot; MaskStretchFactorY0=&quot;0.000000&quot; MaskStretchFactorY1=&quot;1.000000&quot; MaskStretchBGAlpha=&quot;0&quot;/&gt;&lt;/ShapeList&gt;&lt;FaceList&gt;&lt;MetaFace Enable=&quot;true&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; EnableBlendingColor=&quot;false&quot; BlendingColor=&quot;16777215&quot;/&gt;&lt;/FaceList&gt;&lt;BorderList&gt;&lt;MetaBorder Enable=&quot;false&quot; Size=&quot;3&quot; Color1=&quot;16777215&quot; Color2=&quot;16777215&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; BorderType=&quot;1&quot; ValueType=&quot;0&quot;/&gt;&lt;/BorderList&gt;&lt;ShadowList&gt;&lt;MetaShadow EnableShape=&quot;false&quot; EnableBorder=&quot;false&quot; Color1=&quot;328965&quot; Color2=&quot;328965&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; OffsetX=&quot;7.071068&quot; OffsetY=&quot;7.071068&quot; Height=&quot;false&quot;/&gt;&lt;/ShadowList&gt;&lt;Position1List&gt;&lt;MetaPosition1 CenterX=&quot;0.500000&quot; CenterY=&quot;0.500000&quot; Width=&quot;0.400000&quot; Height=&quot;0.400000&quot; Degree=&quot;0.000000&quot; Alpha=&quot;255&quot; TangentVectorX1=&quot;0.000000&quot; TangentVectorY1=&quot;0.000000&quot; TangentVectorX2=&quot;0.000000&quot; TangentVectorY2=&quot;0.000000&quot;/&gt;&lt;/Position1List&gt;&lt;Motion1List/&gt;&lt;/MetaPicture&gt;&lt;/PictureList&gt;&lt;/CPicture&gt;
"/>
</PARAM>
<PARAM NAME="ID_PIP_KEEP_ASPECTRATIO" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_BORDER_SINGLE" VALUETYPE="RGB" VALUE="FFFFFFFF"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_BEGIN" VALUETYPE="RGB" VALUE="FFFFFFFF"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_END" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_ANGLE" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_SHADOW_DISTANCE" VALUETYPE="INT" VALUE="10"/>
<PARAM NAME="ID_PIP_SHADOW_DIRECTION" VALUETYPE="BYTE" VALUE="3"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_SHADOW_SINGLE" VALUETYPE="RGB" VALUE="FF050505"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_BEGIN" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_END" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_ANGLE" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_IN_DIRECTION" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_IN_OFFSCREEN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_IN_FADE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_OUT_DIRECTION" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_OUT_OFFSCREEN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_OUT_FADE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_KEEP_MASK_ASPECT_RATIO" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="ID_PIP_DZTEMPLATEID" VALUETYPE="STRING" VALUE=""/>
</TRANSITION>"""
		}
		
		return output
	}
*/
}


/*
<TRANSITION ID="0" GUID="CES_PlugIn.dll\DSP_PI_Script" START="0" STOP="50050050">
<PARAM NAME="CLT_SCRIPT_BUFFER" VALUETYPE="" VALUE="">
<LINEAR TIME="0" VALUE="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-16&quot;?&gt;
&lt;CPicture Version=&quot;1.0&quot;&gt;&lt;Header&gt;&lt;Information Creator=&quot;CPicture Exported XML&quot; CreateDate=&quot;2009/9/4&quot; LastModified=&quot;2009/9/4&quot;/&gt;&lt;/Header&gt;&lt;PictureList&gt;&lt;MetaPicture PosType=&quot;0&quot; Interpolation=&quot;1&quot;&gt;&lt;SourceList&gt;&lt;MetaSource Type=&quot;1&quot; FileName=&quot;&quot; UseAlpha=&quot;true&quot; SrcFixed=&quot;true&quot; SrcAspectRatioX=&quot;0&quot; SrcAspectRatioY=&quot;0&quot; SrcStretchMode=&quot;0&quot; DrawFlipType=&quot;0&quot; ShowShadowBorderColorKey=&quot;true&quot; Subtype=&quot;0&quot;/&gt;&lt;/SourceList&gt;&lt;SourceRegionList/&gt;&lt;ShapeList&gt;&lt;MetaShape EnableColorKey=&quot;false&quot; ColorKey=&quot;0&quot; ColorKeyRange=&quot;0&quot; ColorKeyCoordinateX=&quot;4294967295&quot; ColorKeyCoordinateY=&quot;4294967295&quot; MaskFile=&quot;&quot; MaskStretchMode=&quot;1&quot; MaskStretchFactorX0=&quot;0.000000&quot; MaskStretchFactorX1=&quot;1.000000&quot; MaskStretchFactorY0=&quot;0.000000&quot; MaskStretchFactorY1=&quot;1.000000&quot; MaskStretchBGAlpha=&quot;0&quot;/&gt;&lt;/ShapeList&gt;&lt;FaceList&gt;&lt;MetaFace Enable=&quot;true&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; EnableBlendingColor=&quot;false&quot; BlendingColor=&quot;16777215&quot;/&gt;&lt;/FaceList&gt;&lt;BorderList&gt;&lt;MetaBorder Enable=&quot;false&quot; Size=&quot;3&quot; Color1=&quot;16777215&quot; Color2=&quot;16777215&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; BorderType=&quot;1&quot; ValueType=&quot;0&quot;/&gt;&lt;/BorderList&gt;&lt;ShadowList&gt;&lt;MetaShadow EnableShape=&quot;false&quot; EnableBorder=&quot;false&quot; Color1=&quot;328965&quot; Color2=&quot;328965&quot; GradType=&quot;0&quot; Alpha=&quot;255&quot; BlurRadius=&quot;0&quot; OffsetX=&quot;7.071068&quot; OffsetY=&quot;7.071068&quot; Height=&quot;false&quot;/&gt;&lt;/ShadowList&gt;&lt;Position1List&gt;&lt;MetaPosition1 CenterX=&quot;0.500000&quot; CenterY=&quot;0.500000&quot; Width=&quot;0.400000&quot; Height=&quot;0.400000&quot; Degree=&quot;0.000000&quot; Alpha=&quot;255&quot; TangentVectorX1=&quot;0.000000&quot; TangentVectorY1=&quot;0.000000&quot; TangentVectorX2=&quot;0.000000&quot; TangentVectorY2=&quot;0.000000&quot;/&gt;&lt;/Position1List&gt;&lt;Motion1List/&gt;&lt;/MetaPicture&gt;&lt;/PictureList&gt;&lt;/CPicture&gt;
"/>
</PARAM>
<PARAM NAME="ID_PIP_KEEP_ASPECTRATIO" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_BORDER_SINGLE" VALUETYPE="RGB" VALUE="FFFFFFFF"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_BEGIN" VALUETYPE="RGB" VALUE="FFFFFFFF"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_END" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_BORDER_GRADIENT_ANGLE" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_SHADOW_DISTANCE" VALUETYPE="INT" VALUE="10"/>
<PARAM NAME="ID_PIP_SHADOW_DIRECTION" VALUETYPE="BYTE" VALUE="3"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_SHADOW_SINGLE" VALUETYPE="RGB" VALUE="FF050505"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_BEGIN" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_END" VALUETYPE="RGB" VALUE="FF000000"/>
<PARAM NAME="ID_PIP_SHADOW_GRADIENT_ANGLE" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_IN_DIRECTION" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_IN_OFFSCREEN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_IN_FADE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_OUT_DIRECTION" VALUETYPE="BYTE" VALUE="0"/>
<PARAM NAME="ID_PIP_OUT_OFFSCREEN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_OUT_FADE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="ID_PIP_KEEP_MASK_ASPECT_RATIO" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="ID_PIP_DZTEMPLATEID" VALUETYPE="STRING" VALUE=""/>
</TRANSITION>
*/