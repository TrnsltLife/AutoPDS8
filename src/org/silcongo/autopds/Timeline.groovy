package org.silcongo.autopds

class Timeline
{
	Library library
	
	def videoTrack
	
	def audioTrack
	def musicTrack = []
	def pipTrack = []
	
	def titleTrack
	def subtitleTrack
	

	public Timeline(lib)
	{
		library = lib
		videoTrack = new TrackVideo(library)

		audioTrack = new TrackAudio(library, 22)

		
		musicTrack[1] = new TrackAudio(library, 23)
		musicTrack[2] = new TrackAudio(library, 24)
		musicTrack[3] = new TrackAudio(library, 25)

		titleTrack = new TrackTitle(library)
		subtitleTrack = new TrackSubtitle(library)
		
		pipTrack[1] = new TrackPIP(library, 3)
		pipTrack[2] = new TrackPIP(library, 5)
		pipTrack[3] = new TrackPIP(library, 7)
		pipTrack[4] = new TrackPIP(library, 9)
		pipTrack[5] = new TrackPIP(library, 11)
		pipTrack[6] = new TrackPIP(library, 13)
		pipTrack[7] = new TrackPIP(library, 15)
		pipTrack[8] = new TrackPIP(library, 17)
		pipTrack[9] = new TrackPIP(library, 19)
	}
	
	public Double getVideoLatestTime()
	{
		return videoTrack.videoLatestTime
	}
	
	public Double getAudioLatestTime()
	{
		return audioTrack.audioLatestTime
	}
	
	public Object leftShift(it)
	{
		if(it instanceof Map)
		{
			//TODO do special assignment
		}
		else if(it instanceof Number)
		{
			it = audioTrack << it
		}
		else if(it instanceof VisualObject)
		{
			it = videoTrack << it
		}
		else if(it instanceof AudioObject)
		{
			it = audioTrack << it
		}
		else if(it instanceof Transition)
		{
			it = videoTrack << it
		}
		else if(it instanceof EffectSubtitle)
		{
			it = subtitleTrack << it
		}
		else if(it instanceof EffectTitle)
		{
			it = titleTrack << it
		}
		return it
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<TIMELINE ID="0">"""

		def trackID = 0

		//output visual tracks <GROUP ID="0">
		output << """<GROUP ID="0">"""
		
		output << """<COMPOSITE ID="0">"""
		output << """<COMPOSITE ID="0">"""
		output << """<COMPOSITE ID="0">"""
		output << """<COMPOSITE ID="0">"""

		output << videoTrack.toStringVideo()
		
		output << """</COMPOSITE>
<EFFECT ID="0" NAME="AEFF_EFFECT_DUMMY" GUID="2" START="0" STOP="0" MUTE="TRUE"/>
</COMPOSITE>"""

		//Output PIP tracks' video
		for(int pipIndex=1; pipIndex < pipTrack.size(); pipIndex++)
		{
			pipTrack[pipIndex].each{output << it.toStringVideo()}
		}

		//Title track
		output << titleTrack.toString()


		output << """</COMPOSITE>"""


		//Subtitle Track
		output << subtitleTrack.toString()

		
/*
		output += """</COMPOSITE>
<EFFECT ID="0" NAME="AEFF_TITLE_DUMMY" GUID="19" START="0" STOP="0" MUTE="TRUE"/>
</COMPOSITE>"""
*/

		output << """</COMPOSITE>"""
	
		output << """</GROUP>"""
		


		//output aural tracks <GROUP ID="1">
		output << """<GROUP ID="1">"""
		output << """<COMPOSITE ID="0">"""
		output << """<COMPOSITE ID="1">"""
		
		output << videoTrack.toStringAudio()

		output << """</COMPOSITE>"""
		
		//Output PIP tracks' audio
		for(int pipIndex=1; pipIndex < pipTrack.size(); pipIndex++)
		{
			pipTrack[pipIndex].each{output << it.toStringAudio()}
		}

/*
output += """<TRACK ID="4" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="6" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="8" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="10" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="12" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="14" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="16" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="18" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>
<TRACK ID="20" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">
<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>"""
*/

		//AEFF_AUDIO_TRACKs for Voice, Music1, Music2, Music3
		output << audioTrack.toString()
		
		for(int musicIndex=1; musicIndex < musicTrack.size(); musicIndex++)
		{
			output << musicTrack[musicIndex].toString()
		}
		
		output << """</COMPOSITE>"""
		
		output << """</GROUP>"""
		
		output << """</TIMELINE>"""
		return output.toString()
	}
}