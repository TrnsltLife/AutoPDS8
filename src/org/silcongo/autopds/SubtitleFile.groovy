package org.silcongo.autopds

import org.silcongo.autopds.util.*

class SubtitleFile
{
	def subtitles = []
	def variables = [:]
	
	public SubtitleFile(file)
	{
		this(file, "UTF-8")
	}
	
	public SubtitleFile(file, encoding)
	{
		new File(file).newReader(encoding).eachLine{line->
		
			//ignore UTF-8 byte-order mark on first line if it is present
			if(encoding == "UTF-8" && line.indexOf("\ufeff") == 0)
			{
				line = line.substring(1, line.length());
			}

			
			def start, end
			def text = ""
			def parts = line.split(/\t/)
			if(parts.size() <= 1)
			{
				next
			}
			else if(parts.size() >= 2)
			{
				start = parts[0]
				end = parts[1]
				
				start = Float.parseFloat(start)
				end = Float.parseFloat(end)
				
				if(parts.size() > 2)
				{
					text = parts[2]
				}
			}
			
			//Create a variable entry for the starting point of the subtitle file
			def variable = "start"
			if(!variables.containsKey(variable))
			{
				variables[variable] = [:]
				def v = variables[variable]
				v[0] = 0
				v["start"] = 0
				v[1] = 0
				v["end"] = 0
				v["size"] = 2
				v["duration"] = 0
			}

			
			//Deal with variable declarations e.g. {c11v3}.
			//These set start and end times associated with the variable name.
			//The variable name can be used in a script file (e.g. used by ScriptProducerExcelExport) to
			//base an event on the subtitle timing
			def varRegex = /\{[_a-zA-Z][_a-zA-Z0-9]*\}/
			while(text =~ varRegex)
			{
				def matcher = (text =~ varRegex)
				variable = matcher[0]
				variable = (variable - "{") - "}"
				text = matcher.replaceFirst("")
				
				//Create hash map with keys named:
				//start: start time of the subtitle
				//end: end time of the subtitle
				//size: number of numbered indexes (e.g. 0, 1, 2...)
				//0: numbered index holding the start time of the subtitle
				//1: numbered index that initially holds the end time of the subtitle
				if(!variables.containsKey(variable))
				{
					variables[variable] = [:]
					def v = variables[variable] 
					v[0] = start
					v["start"] = start
					v[1] = end
					v["end"] = end
					v["size"] = 2
					v["duration"] = end - start
				}
				else //the variable already exists. update the "end" time and add more numbered indexes, e.g. 2&3, 4&5, etc.
				{
					def v = variables[variable]
					def sz = v["size"]
					v[sz] = start
					v[sz+1] = end
					v["end"] = end
					v["size"] = v["size"] + 2
					v["duration"] = v["end"] - v["start"]
				}
			}
			
			//If the text portion of the entry starts with a +, add this line to the previous line.
			def plus = text.startsWith("+")
			if(plus){text -= "+"}

			//If the text contains the character |, replace it with \r\n
			text = text.replaceAll(/\|/, "\r\n")

			//text = XMLUtils.escape(text)
			
			//If the text is more than whitespace, add a subtitle entry. Otherwise go on to the next line.
			if(text.trim())
			{
				if(plus)
				{
					def entry = subtitles[-1]
					entry.end = end
					entry.text += " " + text
				}
				else
				{
					//Make sure start and end are not the same, and that end is after start
					if(start < end)
					{
						subtitles << new SubtitleFileEntry(start:start, end:end, text:text)
					}
				}
			}
		}
	}
	
	public SubtitleFile()
	{
	}
	
	public void offset(offset)
	{
		//Add offset to all subtitle entries
		subtitles.each{sub->
			sub.start += offset
			sub.end += offset
		}
		
		//Add offset to all variable entries
		variables.each{k,v->
			v.start += offset
			v.end += offset
			for(def i=0; i<v["size"]; i++)
			{
				v[i] += offset
			}
		}
	}
	
	public void writeFile(file)
	{
		use(DataFormat)
		{
			new File(file).withWriter("UTF-8"){writer->
				subtitles.each{sub->
					writer << sub.start.f() + "\t" + sub.end.f() + "\t" + sub.text + "\r\n"
				}
			}
		}
	}
}