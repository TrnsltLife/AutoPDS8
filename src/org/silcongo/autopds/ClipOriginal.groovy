package org.silcongo.autopds

class ClipOriginal
{
	//Video/Image A/B:
	//<CLIP ID="1" IDREF="90" NAME="" START="560226893" STOP="610276943" SCENESTART="0" SCENESTOP="0" MEDIASTART="0" MEDIASTOP="5006005" MUTE="FALSE" STRETCHMODE="STRETCH" PANOVISIONMODE="0">

	//Audio A/B:
	//<CLIP ID="0" IDREF="84" NAME="" START="0" STOP="581668335" SCENESTART="0" SCENESTOP="0" MEDIASTART="0" MEDIASTOP="0" MUTE="TRUE" STRETCHMODE="">

	//AEFF_AUDIO_TRACK
	//<CLIP ID="0" IDREF="46" NAME="A22091-01.mp3" START="0" STOP="581740000" SCENESTART="0" SCENESTOP="0" MEDIASTART="0" MEDIASTOP="581740000" MUTE="FALSE" STRETCHMODE="">
	
	def track
	def id
	def idRef
	def mediaObject
	def name = ""
	def start = 0
	def stop = 0
	def sceneStart = 0
	def sceneStop = 0
	def mediaStart = 0
	def mediaStop = 0
	def mute = false
	def stretchMode = ""
	def panovisionMode = 0
	
	public ClipOriginal(trk, uid, mediaObj, startTime, duration=-1)
	{
		track = trk
		id = uid
		mediaObject = mediaObj
		idRef = mediaObject.id
		name = mediaObject.name
		start = startTime
		
		if(mediaObject instanceof AudioDummy)
		{
			if(duration == -1){duration = mediaObject.duration}
			stop = start + duration
			mediaStart = 0
			mediaStop = 0
			sceneStart = 0
			sceneStop = 0
		}
		else
		{
			stop = start + mediaObject.duration
		}
		
		//set default mute value
		if(track =~ /AEFF_MVIDEO/)
		{
			mute = false
		}
		else if(track =~ /AEFF_AUDIO/)
		{
			mediaStop = mediaObject.duration //TODO figure out how to calculate this value properly
			mute = false
		}
		else if(track =~ /AEFF_MAUDIO/)
		{
			sceneStop = mediaObject.duration //TODO figure out how to calculate this value properly
			mediaStop = mediaObject.duration //TODO figure out how to calculate this value properly
			mute = true
		}
	}
	
	public String toString()
	{
		def output = ""
		if(track =~ /AEFF_MVIDEO/)
		{
			output += """<CLIP ID="$id" IDREF="$idRef" NAME="$name" START="$start" STOP="$stop" SCENESTART="$sceneStart" SCENESTOP="$sceneStop" MEDIASTART="$mediaStart" MEDIASTOP="$mediaStop" MUTE="""
			output += '"' + mute.toString().toUpperCase() + '"'
			output += """ STRETCHMODE="$stretchMode" """
			output += """PANOVISIONMODE="$panovisionMode">"""
output += """<CLIP_EXTRA_DATA ID="0">
<PARAM NAME="VIDEO_EAGLEVISION_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_EAGLEVISION_FLAG" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_MOTIONPARAM" VALUETYPE="INT" VALUE=""/>
<PARAM NAME="VIDEO_MOTIONPARAM_SIZE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_EAGLEVISION_MODE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_STABLIZER_MODE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_STABLIZER_TOLERANCE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_STABLIZER_ANGLE" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_ASPECTRATIO_X" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_ASPECTRATIO_Y" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_ALIAS" VALUETYPE="STRING" VALUE="${mediaObject.name}"/>
<PARAM NAME="VIDEO_USER_DATA_ORI_FILENAME" VALUETYPE="STRING" VALUE="${mediaObject.src}"/>
<PARAM NAME="VIDEO_USER_DATA_APPLY_MAGIC_CLEAN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="VIDEO_USER_DATA_AUTOFIX_FLAG" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_APPLY_MAGIC_MOTION" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="VIDEO_USER_DATA_AUTO_SET_FOCUS" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_MOTION_X_COORD" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_MOTION_Y_COORD" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_MOTION_WIDTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_MOTION_HEIGHT" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_MOTION_STYLE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_MAGIC_REFOCUS_LEVEL" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_APPLY_MAGIC_ENHANCE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="VIDEO_USER_DATA_STABILIZER_LEVEL" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_STAB_REAL_TOLRANCE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_STAB_SHADOW_TOLRANCE" VALUETYPE="UNSIGNED_INT" VALUE="0"/>
<PARAM NAME="VIDEO_USER_DATA_IMAGE_RESIZE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="VIDEO_USER_DATA_SHOW_TIMECODE" VALUETYPE="BOOL" VALUE="FALSE"/>
<CLIP_EXTRA_DATA_MAGICEFFECT ID="0"/>
</CLIP_EXTRA_DATA>
</CLIP>"""
		}
		else if(track =~ /AEFF_MAUDIO/)
		{
			output += """<CLIP ID="$id" IDREF="$idRef" NAME="" START="$start" STOP="$stop" SCENESTART="$sceneStart" SCENESTOP="$sceneStop" MEDIASTART="$mediaStart" MEDIASTOP="$mediaStop" MUTE="""
			output += '"' + mute.toString().toUpperCase() + '"'
			output += """ STRETCHMODE="$stretchMode"/>"""
		}
		else if(track =~ /AEFF_AUDIO/)
		{
			output += """<CLIP ID="$id" IDREF="$idRef" NAME="$name" START="$start" STOP="$stop" SCENESTART="$sceneStart" SCENESTOP="$sceneStop" MEDIASTART="$mediaStart" MEDIASTOP="$mediaStop" MUTE="""
			output += '"' + mute.toString().toUpperCase() + '"'
			output += """ STRETCHMODE="$stretchMode">"""
output += """<CLIP_EXTRA_DATA ID="0">
<PARAM NAME="AUDIO_NOICE_REDUCE_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="AUDIO_NOICE_REDUCE_TYPE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="AUDIO_USER_DATA_ALIAS" VALUETYPE="STRING" VALUE="$name"/>
<PARAM NAME="AUDIO_USER_DATA_APPLY_MAGIC_CLEAN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="AUDIO_VALID" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="AUDIO_MUTE" VALUETYPE="BOOL" VALUE="FALSE"/>
<CLIP_EXTRA_DATA_MAGICEFFECT ID="0"/>
</CLIP_EXTRA_DATA>
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="KEYFRAME" VALUETYPE="FLOAT" VALUE="">
<LINEAR TIME="0" VALUE="1.000000"/>
<LINEAR TIME="$mediaStop" VALUE="1.000000"/>
</PARAM>
</EFFECT>
<EFFECT ID="1" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="TRUE">
<PARAM NAME="FADEIN" VALUETYPE="INT" VALUE="">
<LINEAR TIME="0" VALUE="0"/>
<LINEAR TIME="0" VALUE="1"/>
</PARAM>
</EFFECT>
<EFFECT ID="2" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="TRUE">
<PARAM NAME="FADEOUT" VALUETYPE="INT" VALUE="">
<LINEAR TIME="${mediaObject.duration}" VALUE="1"/>
<LINEAR TIME="${mediaObject.duration}" VALUE="0"/>
</PARAM>
</EFFECT>
<EFFECT ID="3" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</CLIP>"""
		}
		return output
	}
}