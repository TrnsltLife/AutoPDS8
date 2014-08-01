package org.silcongo.autopds

class Library
{
	def idCounter = 0 //all the Slot objects use ID to distinguish their items. The id series is shared between them, so videoSlot may have an object with ID=0, audioSlot with ID=1, and imageSlot with ID=2, etc.

	def videoSlot = []
	def audioSlot = []
	def imageSlot = []
	def colorSlot = []
	def smartSoundSlot = []
	def virtualCutSlot = []
	def templateSlot = []
	def subfolderSlot = []
	def audioDummy = null
	
	public int nextID()
	{
		def thisID = idCounter
		idCounter++
		return thisID
	}
	
	public Object add(it, duration=-1)
	{
		//specifying duration gives a way to give a duration to things without a build in duration (like images and color boards) or to specify a duration for an object like a video or audio clip that cannot be read from Java
		
		if(it instanceof File)
		{
			it = it.getCanonicalPath()
		}
		
		def filename = it
	
		if(it instanceof String)
		{
			def spreadIt = [it]
			if(duration != -1){spreadIt << duration}
			
			//create a VideoObject, AudioObject, or ImageObject based on file extension
			def format = Library.classifyFileFormat(it)
			if(it == "AUDIO_DUMMY")
			{
				it = createAudioDummy()
			}
			else if(format == "audio")
			{
				it = new AudioObject(nextID(), *spreadIt)
			}
			else if(format == "image")
			{
				it = new ImageObject(nextID(), *spreadIt)
			}
			else if(format == "video")
			{
				it = new VideoObject(nextID(), *spreadIt)
			}
		}
		else if(it instanceof List)
		{
			it = new ColorObject(nextID(), it[0], it[1], it[2], duration)
		}
		
		if(it instanceof VideoObject)
		{
			def matchingObject = videoSlot.find{asset-> asset.src == filename}
			if(matchingObject)
			{
				it.id = matchingObject.id
			}
			else
			{
				videoSlot << it
			}
		}
		else if(it instanceof AudioDummy)
		{
			it = createAudioDummy()
		}
		else if(it instanceof AudioObject)
		{
			def matchingObject = audioSlot.find{asset-> asset.src == filename}
			if(matchingObject)
			{
				it.id = matchingObject.id
			}
			else
			{
				audioSlot << it
			}
		}
		else if(it instanceof ImageObject)
		{
			def matchingObject = imageSlot.find{asset-> asset.src == filename}
			if(matchingObject)
			{
				it.id = matchingObject.id
			}
			else
			{
				imageSlot << it
			}
		}
		else if(it instanceof ColorObject)
		{
			colorSlot << it
		}
		
		return it
	}
	
	public Object leftShift(it)
	{
		return add(it)
	}
	
	public AudioDummy createAudioDummy()
	{
		//Creates an AudioDummy object if one does not exist
		if(audioDummy == null)
		{
			audioDummy = new AudioDummy(nextID())
			audioSlot << audioDummy
		}
		
		return audioDummy
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << "<LIBRARY>"
		
		if(videoSlot){output << "<VIDEOSLOT>" + videoSlot.collect{it.toString()}.join("") + "</VIDEOSLOT>"}
		else{output << "<VIDEOSLOT/>"}
		
		if(audioSlot){output << "<AUDIOSLOT>" + audioSlot.collect{it.toString()}.join("") + "</AUDIOSLOT>"}
		else{output << "<AUDIOSLOT/>"}

		if(imageSlot){output << "<IMAGESLOT>" + imageSlot.collect{it.toString()}.join("") + "</IMAGESLOT>"}
		else{output << "<IMAGESLOT/>"}

		if(colorSlot){output << "<COLORSLOT>" + colorSlot.collect{it.toString()}.join("") + "</COLORSLOT>"}
		else{output << "<COLORSLOT/>"}
		
		if(smartSoundSlot){output << "<SMARTSOUNDSLOT>" + smartSoundSlot.collect{it.toString()}.join("") + "</SMARTSOUNDSLOT>"}
		else{output << "<SMARTSOUNDSLOT/>"}

		if(virtualCutSlot){output << "<VIRTUALCUTSLOT>" + virtualCutSlot.collect{it.toString()}.join("") + "</VIRTUALCUTSLOT>"}
		else{output << "<VIRTUALCUTSLOT/>"}

		if(templateSlot){output << "<TEMPLATESLOT>" + templateSlot.collect{it.toString()}.join("") + "</TEMPLATESLOT>"}
		else{output << "<TEMPLATESLOT/>"}
		
		if(subfolderSlot){output << "<SUBFOLDERSLOT>" + subfolderSlot.collect{it.toString()}.join("") + "</SUBFOLDERSLOT>"}
		else{output << "<SUBFOLDERSLOT/>"}
		
		output << "</LIBRARY>"
		
		return output.toString()
	}
	
	public static String classifyFileFormat(String format)
	{
		format = format.toLowerCase()
		if(format =~ /\.(mp3|wav|asf|wma|aif)$/)
		{
			return "audio"
		}
		else if(format =~ /\.(bmp|jpg|jpeg|jpe|gif|png|tif|tiff)$/)
		{
			return "image"
		}
		else if(format =~ /\.(mpg|mpeg|mpe|dat|avi|wmv|mov|vob|vro|asf|dvr-ms|wtv|mod|mp4|m2ts|mts|jts|tod|tpd|ts)$/)
		{
			return "video"
		}
		return "unknown"
	}
	
	public static File findFileOrDie(file, message="File $file does not exist")
	{
		if(file instanceof String){file = new File(file)}
		if(file.exists()){return file}
		else
		{
			//println(message)
			//System.exit(1)
			throw new Exception(message)
		}
	}
}