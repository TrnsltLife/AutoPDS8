package org.silcongo.autopds

class AudioDummy extends AudioObject
{
	//<AUDIOOBJECT ID="46" SRC="AUDIO_DUMMY" NAME="AUDIO_DUMMY" MUTE="TRUE"/>

	def id
	def src = "AUDIO_DUMMY"
	def name = "AUDIO_DUMMY"
	def mute = true
	def duration = 0
	def durationTruncated = 0
	
	public AudioDummy(){}

	public AudioDummy(uid)
	{
		id = uid
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<AUDIOOBJECT ID="$id" SRC="$src" NAME="$name" MUTE="""
		output << '"' + mute.toString().toUpperCase() + '"'
		output << """ SUBFOLDERNAME=""/>"""
		return output.toString()
	}
}