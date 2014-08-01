package org.silcongo.autopds

class TrackPIP extends TrackVideo
{
	public TrackPIP(){}

	public TrackPIP(lib)
	{
		library = lib
	}
	
	public TrackPIP(lib, vidtrack)
	{
		library = lib
		videoTrackID = vidtrack
		audioTrackID = vidtrack+1
	}
	
	public Object leftShift(it)
	{
		if(it instanceof VisualObject)
		{
			addClipAt(it, videoLatestTime)
			
			it = videoTimeline[-1]
		}
		else if(it instanceof Transition)
		{
			//assign the latest video clip, if a clip is not already assigned, and if a clip exists
			//println("Transition. it.fromClip => " + it.fromClip + " | videoTimeline => " + videoTimeline)
			if(it.fromClip == null && videoTimeline)
			{
				//println("Assign transition a fromClip from " + videoTimeline[-1].getClass().toString())
				it.fromClip = videoTimeline[-1]
			}
			
			//add transition to the timeline
			transitionTimeline << it
			
			//is it the first transition, and before any video clips?
			if(transitions == 0 && it.start == 0)
			{
				//assign a unique id
				it.id = 0
			}
			else
			{
				//assign a unique id
				it.id = transitions
				transitions++
			}
			
			//keep a reference to this transition, for use by the next video clip that is added
			latestTransition = it
		}

		return it
	}
	
	
	public ClipPIP addClipAt(VisualObject it, startTime, dur=-1)
	{
		videoTimeline << new ClipPIP(videoClips, it, startTime, dur)
		if(it instanceof VideoObject)
		{
			audioTimeline << new ClipMAudio(videoClips, it, startTime)
		}
		else
		{
			audioTimeline << new ClipMAudio(videoClips, library.createAudioDummy(), startTime, it.duration)
		}
		videoClips++
		videoLatestTime += it.duration
		
		//merge this with the most recent transition, if there is one
		if(latestTransition)
		{
			latestTransition.stretchOrMove(videoTimeline[-1])
			videoLatestTime = videoTimeline[-1].stop
			latestTransition = null
		}
		
		return videoTimeline[-1]
	}
	
	
	
	public Object addClippedClip(VideoObject video, startOffset, stopOffset)
	{
		//Adds a video clip to the timeline, but first sets the clip's startOffset and stopOffset
		//The result is that the video clip in the movie is shorter than the original video file (i.e. a subsection of the orginal video file)
		if(startOffset < 0){startOffset *= -1}
		if(stopOffset < 0){stopOffset *= -1}
		
		def videoClip = new ClipPIP(videoClips, video, videoLatestTime)
		videoClip.startOffset = startOffset
		videoClip.stopOffset = stopOffset
		videoClip.recalculateStopTimes()
		
		videoTimeline << videoClip
		videoClips++
		videoLatestTime += videoClip.duration
		
		//add the corresponding MAudio clip to the audio timeline
		def audioClip = new ClipMAudio(videoClips, it, videoLatestTime)
		audioClip.startOffset = startOffset
		audioClip.stopOffset = stopOffset
		audioClip.recalculateStopTimes()
		audioTimeline << audioClip
		
		return videoClip
	}
	
	
	
	
	public String toString()
	{
		def output = toStringVideo() + "\r\n\r\n<!--SPLIT-->\r\n\r\n" + toStringAudio()
		return output
	}
	
	public String toStringVideo()
	{
		def output = new StringBuilder()
		
		if(videoTimeline)
		{
			//AEFF_PIP_TRACK
			output << """<TRACK ID="${videoTrackID}" NAME="AEFF_PIP_TRACK" MUTE="FALSE">"""
			videoTimeline.each{output << it.toString()}

			//Output the transitions
			transitionTimeline.each{output << it.toString()}
			
			output << """</TRACK>"""
		}
		else
		{
			output << """<TRACK ID="${videoTrackID}" NAME="AEFF_PIP_TRACK" MUTE="FALSE"/>"""
		}

		return output.toString()
	}
	
	public String toStringAudio()
	{
		def output = new StringBuilder()
		
		//AEFF_AUDIO_
		output << """<TRACK ID="${audioTrackID}" NAME="AEFF_AUDIO_TRACK" MUTE="FALSE">"""
		audioTimeline.each{output << it.toString()}
		output << """<EFFECT ID="0" NAME="" GUID="CES_PLUGIN.DLL\\DSP_AU_VolumeAdj" START="0" STOP="0" MUTE="FALSE">
<PARAM NAME="GAIN" VALUETYPE="FLOAT" VALUE="1.000000"/>
</EFFECT>
</TRACK>"""

		return output.toString()
	}
}