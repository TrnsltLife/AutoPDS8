package org.silcongo.autopds

import org.silcongo.autopds.util.AudioFileInfo
import org.silcongo.autopds.util.XMLUtils

class AudioObject
{
	//<AUDIOOBJECT ID="46" SRC="C:\Users\Jeremy\Desktop\VCD-Software\GRN\GoodNews\lingala\A22091-01.mp3" NAME="A22091-01.mp3" MUTE="FALSE"/>

	def id
	def src
	def name
	def mute = false
	def subfolderName = ""
	def duration = 0
	def durationTruncated = 0
	
	public AudioObject(){}

	public AudioObject(uid, file)
	{
		id = uid
		def f = new File(file)
		src = f.getCanonicalPath()
		name = f.getName()
		def afi = new AudioFileInfo(file)
		duration = afi.filedata["seconds"]
		if(duration == null){duration = 0}
		durationTruncated = ((duration*1000) as BigInteger)*10000
		duration = duration*10000000 as BigInteger
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<AUDIOOBJECT ID="$id" SRC="${XMLUtils.escape(src)}" NAME="${XMLUtils.escape(name)}" MUTE="""
		output << '"' + mute.toString().toUpperCase() + '"'
		output << """ SUBFOLDERNAME="${XMLUtils.escape(subfolderName)}"/>"""
		return output.toString()
	}
}