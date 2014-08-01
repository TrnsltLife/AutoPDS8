package org.silcongo.autopds.util

class FFMPEG
{
	static path = "ffmpeg.exe"
	
	static setPath(path)
	{
		this.path = path
	}
	
	static getPath(){return path}
	
	static getDuration(mediaFile)
	{
		def duration = getDurationString(mediaFile)
		if(duration)
		{
			def (hours, mins, secs) = duration.split(/:/).toList()
			println("Hours: $hours, Minutes: $mins, Seconds: $secs")
			return (hours as int) * 360 + (mins as int) * 60 + (secs as double)
		}
		return 0
	}
	
	static getDurationString(mediaFile)
	{
		StringBuffer out = new StringBuffer()
		StringBuffer err = new StringBuffer()
		def process = [path, "-i", mediaFile].execute()
		process.consumeProcessOutput(out, err)
		process.waitForOrKill(10000)
		def duration = ""
		err.toString().eachLine{line->
			if(line =~ /\s*Duration:/)
			{
				line = line.replaceAll(/\s*Duration:\s*/, "")
				line = line.replaceAll(/,.*/, "")
				duration = line
			}
		}
		return duration
	}
}