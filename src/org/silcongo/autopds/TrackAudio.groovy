package org.silcongo.autopds

class TrackAudio extends Track
{
	def audioClips = 0
	def audioLatestTime = 0
	def audioTimeline = []	
	def audioTrackID = 22
	
	public TrackAudio(lib)
	{
		library = lib
	}
	
	public TrackAudio(lib, track)
	{
		library = lib
		audioTrackID = track
	}

	public Object leftShift(it)
	{
		if(it instanceof Number)
		{
			//create a blank audio space in the audio track
			//println("Timeline << silence of length $it")
			audioLatestTime += it
			it = [duration:it]
		}
		else if(it instanceof AudioObject)
		{
			audioTimeline << new ClipAudio(audioClips, it, audioLatestTime)
			audioClips++
			audioLatestTime += it.duration
			
			it = audioTimeline[-1]
		}
		
		return it
	}
	
	public Object addClippedClip(AudioObject audio, startOffset, stopOffset)
	{
		//Adds an audio clip to the timeline, but first sets the clip's startOffset and stopOffset
		//The result is that the audio in the movie is shorter than the original audio file (i.e. a subsection of the orginal audio file)
		if(startOffset < 0){startOffset *= -1}
		if(stopOffset < 0){stopOffset *= -1}
		
		def audioClip = new ClipAudio(audioClips, audio, audioLatestTime)
		audioClips++

		audioClip.startOffset = startOffset
		audioClip.stopOffset = stopOffset
		audioClip.recalculateStopTimes()
		
		audioTimeline << audioClip
		audioLatestTime += audioClip.duration
		
		return audioClip
	}
	
	
	public String toString()
	{
		def output = new StringBuilder()

		output << """<TRACK ID="${audioTrackID}" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">"""
		if(audioTimeline)
		{
			output << audioTimeline.collect{it.toString()}.join("")
		}
		output << """<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>"""
		output << """</TRACK>"""

		return output.toString()
	}
}