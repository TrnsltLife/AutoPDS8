package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

class MetaString extends MetaPlaceable
{
	def paragraphCount = 0
	def widthLimit = 10000000.000000 //1000000000.000000
	def rotateDegrees = 0.0
	def vertical = false
	def pathID = "PATH_NOEFFECT"
	def pathProgress = 1.0
	def maskFile = ""
	
	def defaultLineHeight = 30.234375
	def defaultAlign = 6 //apparently this is Centered

	def characterList = [] //MetaCharacter
	def lineList = [] //MetaLine
	def paragraphList = [] //MetaParagraph
	def fontList = [] //MetaFont
	def faceList = [] //MetaFace
	def borderList = [] //MetaBorder
	def shadowList = [] //MetaShadow
	def pathList = [] //MetaPath
	
	def screenWidth = 320.00
	def screenHeight = 240.00
	def xOffset = 0.0 //allows you to shift the Left position of MetaString. There will be a correspondingly sized whitespace on the right. 
	def yOffset = 0.0 //allows you to shift the Top position of MetaString
	def maximumLineWidth = screenWidth //override this to make lines wrap at smaller widths
	def lineHeightMultiplier = 0.5 //1.0 uses the line height from FontMetrics, < 1.0 makes smaller lines, > 1.0 makes bigger lines. Line heigh from FontMetrics is generally too large.

	def breakingCharacterList = "-"
	
	public Object leftShift(it)
	{
		if(it instanceof String)
		{
			it.each{c->
				this << new MetaCharacter(c)
			}
		}
		else if(it instanceof MetaCharacter)
		{
			if(lineList.size() == 0)
			{
				this << new MetaLine()
			}
			characterList << it
			it.index = characterList.size()-1
			it.lineIndex = lineList[-1].index
			lineList[-1].charEnd = characterList[-1].index
			
			//TODO set font, face, border, shadow, top, height, etc.
			if(it.top == "omit"){it.top = lineList[-1].top}
			if(it.height == "omit"){it.height = lineList[-1].height}
		}
		else if(it instanceof MetaLine)
		{
			if(paragraphList.size() == 0)
			{
				this << new MetaParagraph()
			}
			lineList << it
			it.index = lineList.size()-1
			it.paragraphIndex = paragraphList[-1].index
			if(lineList.size() > 1)
			{
				it.charStart = lineList[-2].charEnd + 1
			}
			paragraphList[-1].lineEnd = lineList[-1].index
			
			//TODO set top, height, etc.
			if(it.top == 0.0){it.top = paragraphList[-1].top}
			if(it.height == 0.0){it.height = paragraphList[-1].height}
		}
		else if(it instanceof MetaParagraph)
		{
			paragraphList << it
			it.index = paragraphList.size()-1
			if(paragraphList.size() > 1)
			{
				it.lineStart = paragraphList[-2].lineEnd + 1
			}
			
			//TODO set top, height, etc.
		}
		else if(it instanceof MetaFont)
		{
			fontList << it
			it.index = fontList.size()-1
		}
		else if(it instanceof MetaFace)
		{
			faceList << it
			it.index = faceList.size()-1
		}
		else if(it instanceof MetaBorder)
		{
			borderList << it
			it.index = borderList.size()-1
		}
		else if(it instanceof MetaShadow)
		{
			shadowList << it
			it.index = shadowList.size()-1
		}
		else if(it instanceof MetaPath)
		{
			pathList << it
			//MetaPath starts numbering its indexes from 1 instead of from 0
			it.index = pathList.size()
		}
		return it
	}
	
	
	
	//Notes:
	//MetaString gives offets for left and top, also includes width and height
	//MetaParagraph should have left=0.0 and top as an offset from MetaString.top. Width should be the same as MetaString.width, and height should be the height of this paragraph of text == one line of text
	//MetaLine should have left as an offset from MetaString.left/MetaParagraph.left. Top should be 0.0. Height should be the same as MetaParagraph.height, width should be the width of this line of text.

	
	public void calculateMetrics()
	{
		//TODO: Right now, this assumes there is only one MetaLine and one MetaParagraph
		def charIndex = 0
		def lineIndex = 0

		//Find the maximum line height
		def lineHeight = 0.0
		characterList.each{metaC->
			def metaF = fontList[metaC.fontIndex]
			if(metaF.height > lineHeight){lineHeight = metaF.height}
		}
		//lineHeight *= lineHeightMultiplier
		
		//Set the metrics for this MetaString
		//this.left = (screenWidth - start) / 2
		this.left = 0.0
		this.width = screenWidth
		this.top = screenHeight - lineHeight
		this.height = screenHeight - this.top
		
		//Need to determine the maximum line width
		maximumLineWidth = screenWidth - (2 * xOffset) //Normal screen height with space for xOffset on the left and on the right.
		def longestLineWidth = 0.0
		
		//Loop over all characters until they are all assigned to the proper line, along with their metrics
		while(charIndex < characterList.size()-1)
		{
			charIndex = calculateLineMetrics(charIndex, lineIndex, lineHeight)
			//set the ending character for the line
			lineList[lineIndex].charEnd = charIndex
			
			//update the longestLineWidth
			if(lineList[lineIndex].width > longestLineWidth)
			{
				longestLineWidth = lineList[lineIndex].width
			}
			
			//move to the next lineIndex
			lineIndex++
			//if there are more characters left that weren't assigned to the last line, add a new paragraph and a new line
			if(charIndex < characterList.size()-1)
			{
				//move to the next character index, which needs to be put on the next line
				charIndex++
				
				//add a new paragraph (copying the first paragraph's alignment) and add a new line
				def newParagraph = this << new MetaParagraph(align:paragraphList[0].align)
				def newLine = this << new MetaLine()
				
				//change the top and height of MetaString to make room for the new line
				this.top = screenHeight - (lineIndex+1) * lineHeight
				this.height = (lineIndex+1) * lineHeight
				
				//change the top of every paragraph before
				paragraphList.eachWithIndex{para, i->
					para.top = lineHeight * i
				}
			}
		}
		
		
		//Update MetaString again
		this.left = (screenWidth - longestLineWidth) / 2 + xOffset
		this.top += yOffset
		this.width = longestLineWidth
		

		//Update the left of each line to match the appropriate align property in paragraph
		lineList.each{line->
			def para = paragraphList[line.paragraphIndex]
			if(para.align == MetaParagraph.ALIGN_LEFT)
			{
				line.left = 0.0
			}
			else if(para.align == MetaParagraph.ALIGN_RIGHT)
			{
				line.left = this.width - line.width
			}
			else if(para.align == MetaParagraph.ALIGN_CENTER)
			{
				line.left = (this.width - line.width) / 2
			}
		}
		
		/*
		def output = ""
		def lastIndex = -1
		//print("MetaString top: " + top)
		characterList.each{c-> 
			if(c.lineIndex != lastIndex)
			{
				output += "\r\n${c.lineIndex} at ${paragraphList[lineList[c.lineIndex].paragraphIndex].top}|"
			}
			output += (char)c.value
			
			lastIndex = c.lineIndex
		}
		println(output)
		*/
	}
	
		
	public int calculateLineMetrics(charIndex, lineIndex, lineHeight)
	{
		//This function determines how many characters can be printed on one line. Once the line has become too long,
		//it backtracks to the last whitespace character and breaks the line at that point using insertReturnAfter(...).
		//It returns the last character on the current line to the calling method.
		//Line width can be altered using the maximumLineWidth property of MetaString.
	
		//Set the metrics for the MetaCharacters
		def start = 0.0
		def lastBreakCharIndex = charIndex
		def lastBreakCharX = 0
		def breakLine = false
		for(charIndex; charIndex < characterList.size(); charIndex++)
		{
			def metaC = characterList[charIndex]
			def metaF = fontList[metaC.fontIndex]
			def advance = metaF.fontMetrics.charWidth((char)metaC.value)
			//Check for whitespace (excluding the non-breaking space \u00a0), or check for characters in the breakingCharacterList (which can be modified to include non-breaking space)
			if((Character.isWhitespace(metaC.value) && metaC.value != '\u00a0') || breakingCharacterList.contains(metaC.value.toString()))
			{
				lastBreakCharIndex = charIndex
				lastBreakCharX = start + advance
			}
			if(start + advance > maximumLineWidth)
			{
				lastBreakCharIndex = insertReturnAfter(lastBreakCharIndex, lineIndex)
				lineList[lineIndex].charEnd = lastBreakCharIndex
				breakLine = true
				break
			}
			else
			{
				metaC.lineIndex = lineIndex
				metaC.height = lineHeight
				metaC.left = start
				metaC.width = advance
				metaC.top = 0.0
				start += advance
				lineList[lineIndex].charEnd = charIndex
			}
		}
		
		//Set the metrics for the MetaParagraph
		def paragraphIndex = lineIndex
		paragraphList[paragraphIndex].left = 0.0
		paragraphList[paragraphIndex].top = paragraphIndex * lineHeight
		paragraphList[paragraphIndex].width = start
		paragraphList[paragraphIndex].height = lineHeight
		
		//Set the metrics for the MetaLine
		lineList[lineIndex].left = 0.0
		lineList[lineIndex].top = 0.0
		lineList[lineIndex].width = paragraphList[paragraphIndex].width
		lineList[lineIndex].height = paragraphList[paragraphIndex].height
		
		if(breakLine)
		{
			paragraphList[paragraphIndex].width = lastBreakCharX
			lineList[lineIndex].width = paragraphList[paragraphIndex].width

			return lastBreakCharIndex
		}
		
		return charIndex-1
	}
	
	
	public int insertReturnAfter(charIndex, lineIndex)
	{
		//This function is used by calculateLineMetrics to insert the MetaCharacters \r and \n where a line break needs to occur.
		//It subsequently renumbers the indexes of all the following MetaCharacters.
	
		def lastC = characterList[charIndex]

		characterList.add(charIndex+1, new MetaCharacter("\r"))
		characterList.add(charIndex+2, new MetaCharacter("\n"))
		
		def nextC = characterList[charIndex+1]
		nextC.index = charIndex + 1
		nextC.top = lastC.top
		nextC.left = lastC.left
		nextC.width = 0.0
		nextC.height = lastC.height
		nextC.lineIndex = lastC.lineIndex
		nextC.fontIndex = lastC.fontIndex
		nextC.faceIndex = lastC.faceIndex
		nextC.borderIndex = lastC.borderIndex
		nextC.shadowIndex = lastC.shadowIndex
		
		nextC = characterList[charIndex+2]
		nextC.index = charIndex + 2
		nextC.top = 0.0
		nextC.left = 0.0
		nextC.width = 0.0
		nextC.height = lastC.height
		nextC.lineIndex = lineIndex+1
		nextC.fontIndex = lastC.fontIndex
		nextC.faceIndex = lastC.faceIndex
		nextC.borderIndex = lastC.borderIndex
		nextC.shadowIndex = lastC.shadowIndex

		//Renumber all the characters after these 2 with their new index number
		for(int i=charIndex+3; i<characterList.size(); i++)
		{
			def metaC = characterList[i]
			metaC.index += 2
		}
		
		return charIndex+1
	}
	
	
	public String toString()
	{
		calculateMetrics()
	
		StringBuffer output = new StringBuffer()
		
		use(DataFormat)
		{
			output << """<MetaString Index="0" Left="${left.f()}" Top="${top.f()}" Width="${width.f()}" Height="${height.f()}" ParagraphCount="${paragraphCount}" WidthLimit="${widthLimit.f()}" RotateDegree="${rotateDegrees.f()}" Vertical="${vertical}" PathID="${pathID}" PathProgress="${pathProgress.f()}" MaskFile="${maskFile}">"""
			
			if(characterList)
			{
				output << "<CharList>\r\n"
				characterList.each{output << it.toString() + "\r\n"}
				output << "</CharList>\r\n"
			}
			else
			{
				output << "<CharList/>\r\n"
			}

			if(lineList)
			{
				output << "<LineList>\r\n"
				lineList.each{output << it.toString() + "\r\n"}
				output << "</LineList>\r\n"
			}
			else
			{
				output << "<LineList/>\r\n"
			}
			
			if(paragraphList)
			{
				output << "<ParagraphList>\r\n"
				paragraphList.each{output << it.toString() + "\r\n"}
				output << "</ParagraphList>\r\n"
			}
			else
			{
				output << "<ParagraphList/>\r\n"
			}
			
			if(fontList)
			{
				output << "<FontList>\r\n"
				fontList.each{output << it.toString() + "\r\n"}
				output << "</FontList>\r\n"
			}
			else
			{
				output << "<FontList/>\r\n"
			}
			
			if(faceList)
			{
				output <<"<FaceList>\r\n"
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
			
			if(pathList)
			{
				output << "<PathList>\r\n"
				pathList.each{output << it.toString() + "\r\n"}
				output << "</PathList>\r\n"
			}
			else
			{
				output << "<PathList/>\r\n"
			}
			
			output << "</MetaString>"
		}
		
		return output.toString()
	}
}