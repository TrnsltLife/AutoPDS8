package org.silcongo.autopds

import org.silcongo.autopds.util.*

class Effect
{
	def id = 0
	def name = ""
	def guid = ""
	def start = 0
	def stop = 0
	def duration = 0
	def mute = false
	
	def params = []
	
	public Object leftShift(it)
	{
		if(it instanceof Param)
		{
			params << it
		}
		return it
	}
	
	public void recalculateStopTime()
	{
		stop = start + duration
	}
	
	
	public void createParamList()
	{
		//default method does nothing to override params added directly to the params List
	}
	
	
	public String toString()
	{
		createParamList()
		
		StringBuilder output = new StringBuilder()
		
		use(DataFormat)
		{
			if(params)
			{
				output << """<EFFECT ID="${id}" NAME="${name}" GUID="${guid}" START="${start.i()}" STOP="${stop.i()}" MUTE="${mute.B()}">"""
				params.each{output << it.toString()}
				output << "</EFFECT>"
			}
			else
			{
				output << """<EFFECT ID="${id}" NAME="${name}" GUID="${guid}" START="${start.i()}" STOP="${stop.i()}" MUTE="${mute.B()}"/>"""
			}
		}
		
		return output.toString()
	}
}