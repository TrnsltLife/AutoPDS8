package org.silcongo.autopds

class TrackTitle extends Track
{
	def titles = 0
	def titleLatestTime = 0
	def titleTimeline = []	
	def titleTrackID = 21
	
	public TrackTitle(lib)
	{
		library = lib
	}
	
	public Object leftShift(it)
	{
		if(it instanceof EffectTitle)
		{
			//println("adding a EffectTitle")
			it.id = titles
			titleTimeline << it
			titles++
			
			if(it.start == 0){it.start = titleLatestTime}
			it.recalculateStopTime()
			titleLatestTime = it.stop
		}
		return it
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<EFFECT ID="0" NAME="AEFF_TITLE_DUMMY" GUID=""" + 
			'"' + titleTrackID + '"' + 
			""" START="0" STOP="0" MUTE="TRUE"/>"""
		titleTimeline.eachWithIndex{it, index-> 
			output << it.toString()
		}
		return output.toString()
	}
}