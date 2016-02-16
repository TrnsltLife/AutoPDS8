package org.silcongo.autopds.util

import java.text.DecimalFormat
import java.text.NumberFormat

class SRTFile
{
	StringBuffer buffer = new StringBuffer("")
	int counter = 0
	
	public String secondsToSRTTimecode(seconds)
	{
		int h = seconds / (60*60)
		int m = (seconds / 60) - (h*60)
		double s = seconds - (h * 60 * 60) - (m * 60)
		
		NumberFormat d2 = new DecimalFormat("00")
		NumberFormat d2_3 = new DecimalFormat("00.000")
		String tc = d2.format(h) + ":" +
					d2.format(m) + ":" +
					d2_3.format(s).replaceAll(/\./, ",")
		return tc
	}
	
	public void addSubtitle(start, end, text)
	{
		def t1 = secondsToSRTTimecode(start)
		def t2 = secondsToSRTTimecode(end)
		text = text.replaceAll(/\|/, "\r\n")
		buffer << "${counter}\r\n"
		buffer << "${t1} --> ${t2}\r\n"
		buffer << "${text}\r\n"
		buffer << "\r\n"
		counter++
	}
	
	public void writeFile(filename)
	{
		new File(filename).withWriter("UTF-8"){writer->
			writer << buffer.toString()
		}
	}
}
