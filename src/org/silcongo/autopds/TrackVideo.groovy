package org.silcongo.autopds

class TrackVideo extends Track
{
	def videoClips = 0
	def videoLatestTime = 0
	def videoTimeline = []
	def audioTimeline = []
	
	def videoTrackID = 0
	def audioTrackID = 1
	
	def transitions = 0
	def transitionTimeline = []
	def latestTransition = null
	
	public TrackVideo(){}
	
	public TrackVideo(lib)
	{
		library = lib
	}
	
	
	public Object leftShift(it)
	{
		if(it instanceof VisualObject)
		{
			def clipMVideo = new ClipMVideo(videoClips, it, videoLatestTime)
			def clipMAudio
			//add the video clip to the videoTimeline array
			videoTimeline << clipMVideo
			if(it instanceof VideoObject)
			{
				clipMAudio = new ClipMAudio(videoClips, it, videoLatestTime)
				//associate the audio clip with the video clip & vice versa
				clipMVideo.clipMAudio = clipMAudio
				clipMAudio.clipMVideo = clipMVideo
				//add the audio clip to the audioTimeline array
				audioTimeline << clipMAudio
			}
			else
			{
				clipMAudio = new ClipMAudio(videoClips, library.createAudioDummy(), videoLatestTime, it.duration)
				//associate the audio clip with the video clip & vice versa
				clipMVideo.clipMAudio = clipMAudio
				clipMAudio.clipMVideo = clipMVideo
				//add the audio clip to the audioTimeline array
				audioTimeline << clipMAudio 
			}
			videoClips++
			videoLatestTime += it.duration
			
			//merge this with the most recent transition, if there is one
			if(latestTransition)
			{
				//println("Apply latestTransition to the latest video object " + videoTimeline[-1].getClass().toString())
				//stretch or move the start of the latest clip back in time to match the start of the transition
				latestTransition.stretchOrMove(videoTimeline[-1])
				//update the videoLatestTime value (in case the latest video was moved back in time and not just stretched)
				videoLatestTime = videoTimeline[-1].stop
				latestTransition = null
			}
			
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
	
	
	
	public Object addClippedClip(VideoObject video, startOffset, stopOffset)
	{
		//Adds a video clip to the timeline, but first sets the clip's startOffset and stopOffset
		//The result is that the video clip in the movie is shorter than the original video file (i.e. a subsection of the orginal video file)
		if(startOffset < 0){startOffset *= -1}
		if(stopOffset < 0){stopOffset *= -1}
		
		def videoClip = new ClipMVideo(videoClips, video, videoLatestTime)
		videoClip.startOffset = startOffset
		videoClip.stopOffset = stopOffset
		videoClip.recalculateStopTimes()
		
		videoTimeline << videoClip
		videoClips++
		
		//add the corresponding MAudio clip to the audio timeline
		def audioClip = new ClipMAudio(videoClips, video, videoLatestTime)
		audioClip.startOffset = startOffset
		audioClip.stopOffset = stopOffset
		audioClip.recalculateStopTimes()
		audioTimeline << audioClip
		
		videoClip.clipMAudio = audioClip
		
		videoLatestTime += videoClip.duration
		
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
		
		//AEFF_MVIDEO_A_TRACK
		output << """<TRACK ID="${videoTrackID}" NAME="AEFF_MVIDEO_A_TRACK" MUTE="FALSE">"""
		videoTimeline.eachWithIndex{it, index-> 
			if(index % 2 == 0){output << it.toString()}
		}

		//Output the first video transition here if its start time is 0
		if(transitionTimeline && transitionTimeline[0].start == 0)
		{
			output << transitionTimeline[0].toString()
		}
		
		output << """</TRACK>"""

		//AEFF_MVIDEO_B_TRACK
		output << """<TRACK ID="${videoTrackID}" NAME="AEFF_MVIDEO_B_TRACK" MUTE="FALSE">"""
		videoTimeline.eachWithIndex{it, index-> 
			if(index % 2 == 1){output << it.toString()}
		}

		//All the video transitions are printed here, except the first one, if its start time is 0, in which case it is printed in AEFF_MVIDEO_A_TRACK.
		transitionTimeline.each{it->
			if(it.start > 0)
			{
				output << it.toString()
			}
		}
		output << """</TRACK>"""

		return output.toString()
	}
	
	public String toStringAudio()
	{
		def output = new StringBuilder()
		
		//AEFF_MAUDIO_A_TRACK
		output << """<TRACK ID="${audioTrackID}" NAME="AEFF_MAUDIO_A_TRACK" MUTE="FALSE">"""
		audioTimeline.eachWithIndex{it, index-> 
			if(index % 2 == 0){output << it.toString()}
		}
		output << """</TRACK>"""
		
		//AEFF_MAUDIO_B_TRACK
		output << """<TRACK ID="${audioTrackID}" NAME="AEFF_MAUDIO_B_TRACK" MUTE="FALSE">"""
		audioTimeline.eachWithIndex{it, index-> 
			if(index % 2 == 1){output << it.toString()}
		}
		output << """</TRACK>"""

		return output.toString()
	}
}