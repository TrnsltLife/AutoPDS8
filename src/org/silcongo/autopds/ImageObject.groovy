package org.silcongo.autopds

import org.silcongo.autopds.util.XMLUtils

class ImageObject extends VisualObject
{
	//<IMAGEOBJECT ID="1" SRC="C:\Program Files (x86)\CyberLink\PowerDirector\SampleClips\Cherry.jpg" NAME="Cherry.jpg" MUTE="FALSE"/>

	def duration = 200000000 //20 seconds
	
	public ImageObject(uid, file, dur=-1)
	{
		id = uid
		def f = new File(file)
		src = f.getCanonicalPath()
		name = f.getName()
		if(dur != -1)
		{
			duration = dur
		}
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<IMAGEOBJECT ID="$id" SRC="${XMLUtils.escape(src)}" NAME="${XMLUtils.escape(name)}" MUTE="""
		output << '"' + mute.toString().toUpperCase() + '"'
		output << """ SUBFOLDERNAME="${XMLUtils.escape(subfolderName)}"/>"""
		return output.toString()
	}
}