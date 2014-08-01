package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaLayer
{
	public static final int TEXT_MEDIA = 1
	public static final int IMAGE_MEDIA = 2
	def mediaType = TEXT_MEDIA
	def mediaPtr = 0
	def beginTime = 0.0
	def endTime = 1.0
	def visible = true
	
	def contents = []
	
	public MetaLayer(){}
	
	public MetaLayer(type)
	{
		mediaType = type
	}
	
	public Object leftShift(it)
	{
		if(it instanceof MetaString)
		{
			contents << it
		}
		else if(it instanceof MetaPicture)
		{
			contents << it
		}
		else
		{
			return contents[-1] << it
		}
		return it
	}
	
	
	
//<MetaLayer LayerMediaType="1" LayerMediaPtr="0" LayerBeginTime="0.000000" LayerEndTime="1.000000" LayerVisible="true">

	public String toString()
	{
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """<MetaLayer LayerMediaType="${mediaType}" LayerMediaPtr="${mediaPtr}" LayerBeginTime="${beginTime.f()}" LayerEndTime="${endTime.f()}" LayerVisible="${visible}">"""
			
			
			if(mediaType == TEXT_MEDIA)
			{
				output << """<StringList>"""
				contents.each{output << it.toString()}
				output << """</StringList>"""
			}
			else if(mediaType == IMAGE_MEDIA)
			{
				output << """<PictureList>"""
				contents.each{output << it.toString()}
				output << """</PictureList>"""
			}

		
			output << """</MetaLayer>"""
		}
		
		return output.toString()
	}
}