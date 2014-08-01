package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaPicture
{
	def posType = 0
	def interpolation = 2

	def sourceList = [] //MetaSource
	def sourceRegionList = [] //MetaSourceRegion
	def shapeList = [] //MetaShape
	def faceList = [] //MetaFace
	def borderList = [] //MetaBorder
	def shadowList = [] //MetaShadow
	def position1List = [] //MetaPosition1
	def motion1List = [] //MetaMotion1
	
	
	public Object leftShift(it)
	{
		if(it instanceof MetaSource)
		{
			sourceList << it
		}
		else if(it instanceof MetaSourceRegion)
		{
			sourceRegionList << it
		}
		else if(it instanceof MetaShape)
		{
			shapeList << it
		}
		else if(it instanceof MetaFace)
		{
			faceList << it
		}
		else if(it instanceof MetaBorder)
		{
			borderList << it
		}
		else if(it instanceof MetaShadow)
		{
			shadowList << it
		}
		else if(it instanceof MetaPosition1)
		{
			position1List << it
		}
		else if(it instanceof MetaMotion1)
		{
			motion1List << it
			//MetaMotion1 objects should start with index=1 and move up from there. That's why we don't subtract 1 from motion1List.size()
			it.index = motion1List.size()
		}
		return it
	}
	
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<MetaPicture PosType="${posType}" Interpolation="${interpolation}">\r\n"""
		
		if(sourceList)
		{
			output << "<SourceList>\r\n"
			sourceList.each{output << it.toString() + "\r\n"}
			output << "</SourceList>\r\n"
		}
		else
		{
			output << "<SourceList/>\r\n"
		}
		
		if(sourceRegionList)
		{
			output << "<SourceRegionList>\r\n"
			sourceRegionList.each{output << it.toString() + "\r\n"}
			output << "</SourceRegionList>\r\n"
		}
		else
		{
			output << "<SourceRegionList/>\r\n"
		}

		if(shapeList)
		{
			output << "<ShapeList>\r\n"
			shapeList.each{output << it.toString() + "\r\n"}
			output << "</ShapeList>\r\n"
		}
		else
		{
			output << "<ShapeList/>\r\n"
		}

		if(faceList)
		{
			output << "<FaceList>\r\n"
			faceList.each{output << it.toString() + "\r\n"}
			output << "</FaceList>\r\n"
		}
		else
		{
			output << "<FaceList/>\r\n"
		}

		if(borderList)
		{
			output << "<BorderList>\r\n"
			borderList.each{output << it.toString() + "\r\n"}
			output << "</BorderList>\r\n"
		}
		else
		{
			output << "<BorderList/>\r\n"
		}

		if(shadowList)
		{
			output << "<ShadowList>\r\n"
			shadowList.each{output << it.toString() + "\r\n"}
			output << "</ShadowList>\r\n"
		}
		else
		{
			output << "<ShadowList/>\r\n"
		}

		if(position1List)
		{
			output << "<Position1List>\r\n"
			position1List.each{output << it.toString() + "\r\n"}
			output << "</Position1List>\r\n"
		}
		else
		{
			output << "<Position1List/>\r\n"
		}

		if(motion1List)
		{
			output << "<Motion1List>\r\n"
			motion1List.each{output << it.toString() + "\r\n"}
			output << "</Motion1List>\r\n"
		}
		else
		{
			output << "<Motion1List/>\r\n"
		}		
		
		output << "</MetaPicture>\r\n"
		
		return output.toString()
	}
}