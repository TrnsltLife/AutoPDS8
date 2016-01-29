package org.silcongo.autopds

import org.silcongo.autopds.util.*

class EffectVolumeAdjust extends Effect
{
	def paramName
	def paramType
	def fromClip
	def paramLinear

	public EffectVolumeAdjust(id, paramName, fromClip)
	{
		this(id, paramName, fromClip, true)
	}
	
	public EffectVolumeAdjust(id, paramName, fromClip, addDefaultValue)
	{
		this.fromClip = fromClip
		
		this.id = id
		this.paramName = paramName
		if(paramName == "KEYFRAME")
		{
			paramType = "FLOAT"
			paramLinear = new ParamLinear(paramName, paramType)
			params << paramLinear
		}
		else if(paramName == "FADEIN" || paramName == "FADEOUT")
		{
			paramType = "INT"
			paramLinear = new ParamLinear(paramName, paramType)
			params << paramLinear
		}
		else if(paramName == "GAIN")
		{
			paramType = "FLOAT"
			paramLinear = null
		}
		guid = "CES_PLUGIN.DLL\\DSP_AU_VolumeAdj"
		
		start = 0
		stop = 0
		
		if(addDefaultValue)
		{
			this.addDefaultValue()
		}
	}
	
	public void addDefaultValue()
	{
		if(paramName == "KEYFRAME")
		{
			paramLinear.add(0, 1.0)
			paramLinear.add(fromClip.duration, 1.0)
		}
		else if(paramName == "FADEIN")
		{
			paramLinear.add(0, 0)
			paramLinear.add(0, 1)
		}
		else if(paramName == "FADEOUT")
		{
			paramLinear.add(fromClip.duration, 1)
			paramLinear.add(fromClip.duration, 0)
		}
		else if(paramName == "GAIN")
		{
			params << new Param("GAIN", "FLOAT", 1.0)
		}
	}
	
	public void clear()
	{
		//Clears the ParamLinear list if this is KEYFRAME, FADEIN, or FADEOUT
		if(paramName == "KEYFRAME" || paramName == "FADEIN" || paramName == "FADEOUT")
		{
			paramLinear = new ParamLinear(paramName, paramType)
			params = []
			params << paramLinear
		}
		//Clears the params list if this is GAIN
		if(paramName == "GAIN")
		{
			params = []
		}
	}
	
	public void add(time, value)
	{
		if(time < 0){time = 0}
		if(time > fromClip.duration){time = fromClip.duration}
		paramLinear << [time:time, value:value]
	}
	
	public void addFirst(value)
	{
		paramLinear.add(0, value)
	}
	
	public void addLast(value)
	{
		paramLinear.add(fromClip.duration, value)
	}
	
	public void setFadeTime(duration)
	{
		if(paramName == "FADEIN")
		{
			def linearValue = paramLinear.linearValueList[1]
			linearValue.time += duration
		}
		else if(paramName == "FADEOUT")
		{
			def linearValue = paramLinear.linearValueList[0]
			linearValue.time -= duration
		}
	}
}