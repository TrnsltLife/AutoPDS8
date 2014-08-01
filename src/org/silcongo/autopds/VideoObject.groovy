package org.silcongo.autopds

import org.silcongo.autopds.util.XMLUtils

class VideoObject extends VisualObject
{
	//<VIDEOOBJECT ID="0" SRC="C:\Program Files (x86)\CyberLink\PowerDirector\SampleClips\Aquarium.mpg" NAME="Aquarium.mpg" MUTE="FALSE"/>

	public VideoObject(uid, file)
	{
		id = uid
		def f = new File(file)
		src = f.getCanonicalPath()
		name = f.getName()
		mute = false
	}
	
	public VideoObject(uid, file, dur)
	{
		id = uid
		def f = new File(file)
		src = f.getCanonicalPath()
		name = f.getName()
		duration = dur
		mute = false
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<VIDEOOBJECT ID="$id" SRC="${XMLUtils.escape(src)}" NAME="${XMLUtils.escape(name)}" MUTE="""
		output << '"' + mute.toString().toUpperCase() + '"'
		output << """ SUBFOLDERNAME="${XMLUtils.escape(subfolderName)}"/>"""
		return output.toString()
	}
}