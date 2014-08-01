package org.silcongo.autopds

import org.silcongo.autopds.util.*

class ClipAudio extends Clip
{
	//AEFF_AUDIO_TRACK
	//<CLIP ID="0" IDREF="46" NAME="A22091-01.mp3" START="0" STOP="581740000" SCENESTART="0" SCENESTOP="0" MEDIASTART="0" MEDIASTOP="581740000" MUTE="FALSE" STRETCHMODE="">

	def effectKeyframe
	def effectFadein
	def effectFadeout
	def effectGain
	
	public ClipAudio(uid, mediaObj, startTime, dur=-1)
	{
		id = uid
		mediaObject = mediaObj
		idRef = mediaObject.id
		name = mediaObject.name
		
		if(dur == -1){mediaDuration = mediaObject.duration}
		else{mediaDuration = dur}
		duration = mediaDuration
		
		start = startTime
		
		recalculateStopTimes()
	}
	
	public void recalculateStopTimes()
	{
		if(mediaObject instanceof AudioDummy)
		{
			stop = start + duration
			mediaStart = 0
			mediaStop = duration
			sceneStart = 0
			sceneStop = 0
		}
		else
		{
			mediaStart = startOffset
			mediaStop = mediaDuration - stopOffset
			
			duration = mediaStop - mediaStart
			
			stop = start + duration
			
			sceneStop = mediaDuration
		}
		
		resetEffects()
	}
	
	public void resetEffects()
	{
		//These need to be reset after any change to the stop time
		effectKeyframe = new EffectVolumeAdjust(0, "KEYFRAME", this)
		effectFadein = new EffectVolumeAdjust(1, "FADEIN", this)
		effectFadeout = new EffectVolumeAdjust(2, "FADEOUT", this)
		effectGain = new EffectVolumeAdjust(3, "GAIN", this)
	}
	
	public void setConstantVolume(volume)
	{
		if(volume < 0){volume = 0}
		if(volume > 2){volume = 2}
		effectKeyframe.clear()
		effectKeyframe.addFirst(volume)
		effectKeyframe.addLast(volume)
	}
	
	public String toString()
	{
		def output = new StringBuilder()

		use(DataFormat)
		{
			output << """<CLIP ID="$id" IDREF="$idRef" NAME="$name" START="${start.i()}" STOP="${stop.i()}" SCENESTART="${sceneStart.i()}" SCENESTOP="${sceneStop.i()}" MEDIASTART="${mediaStart.i()}" MEDIASTOP="${mediaStop.i()}" MUTE="${mute.B()}" STRETCHMODE="${stretchMode}" PANOVISIONMODE="$panovisionMode">
<CLIP_EXTRA_DATA ID="0">
<PARAM NAME="AUDIO_NOICE_REDUCE_STRENGTH" VALUETYPE="FLOAT" VALUE="0.000000"/>
<PARAM NAME="AUDIO_NOICE_REDUCE_TYPE" VALUETYPE="INT" VALUE="0"/>
<PARAM NAME="AUDIO_REVERSE_FLAG" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="AUDIO_USER_DATA_ALIAS" VALUETYPE="STRING" VALUE="$name"/>
<PARAM NAME="AUDIO_USER_DATA_APPLY_MAGIC_CLEAN" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="AUDIO_VALID" VALUETYPE="BOOL" VALUE="TRUE"/>
<PARAM NAME="AUDIO_MUTE" VALUETYPE="BOOL" VALUE="FALSE"/>
<PARAM NAME="AUDIO_USER_DATA_SPEED_FACTOR" VALUETYPE="FLOAT" VALUE="1.000000"/>
<CLIP_EXTRA_DATA_MAGICEFFECT ID="0"/>
</CLIP_EXTRA_DATA>"""

			output << effectKeyframe.toString()
			output << effectFadein.toString()
			output << effectFadeout.toString()
			output << effectGain.toString()
			output << "</CLIP>"

/*
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="KEYFRAME" VALUETYPE="FLOAT" VALUE="">
<LINEAR TIME="0" VALUE="1.000000"/>
<LINEAR TIME="${mediaStop}" VALUE="1.000000"/>
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
<LINEAR TIME="${mediaStop}" VALUE="1"/>
<LINEAR TIME="${mediaStop}" VALUE="0"/>
</PARAM>
</EFFECT>
<EFFECT ID="3" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
*/
		}

		return output.toString()
	}
}