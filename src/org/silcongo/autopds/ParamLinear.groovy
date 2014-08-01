package org.silcongo.autopds

import org.silcongo.autopds.util.*

class ParamLinear extends Param
{
	def linearValueList = [] //each list entry should be a Map with keys time: and value:

	public ParamLinear(name, valueType)
	{
		this.name = name
		this.valueType = valueType
		this.linearValueList = []
	}
	
	public void add(time, value)
	{
		linearValueList << [time:time, value:value]
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		use(DataFormat)
		{
			output << """<PARAM NAME="${name}" VALUETYPE="${valueType}" VALUE="">"""
			linearValueList.each{map->
				output << """<LINEAR TIME="${map.time.i()}" VALUE="${formatValue(valueType, map.value)}"/>"""
			}
			output << "</PARAM>"
		}
		return output.toString()
	}
}