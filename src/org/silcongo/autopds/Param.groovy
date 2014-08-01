package org.silcongo.autopds

import org.silcongo.autopds.util.*

class Param
{
	def name
	def valueType
	def value
	def textValue
	
	public Param(){}
	
	public Param(name, valueType, value, textValue=null)
	{
		this.name = name
		this.valueType = valueType
		this.value = value
		this.textValue = textValue
	}
	
	public static formatValue(valueType, value)
	{
		def output = ""
		
		use(DataFormat)
		{
			if(value != null)
			{
				if(valueType == "FLOAT")
				{
					output = value.f()
				}
				else if(valueType == "BOOL")
				{
					output = value.B()
				}
				else if(valueType == "CHAR")
				{
					def v = value.replaceAll(/"/, "&quot;")
					v = value.replaceAll(/</, "&lt;")
					v = value.replaceAll(/>/, "&gt;")
					output = v
				}
				else
				{
					output = value
				}
			}
		}
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<PARAM NAME="${name}" VALUETYPE="${valueType}""" + '"'
		
		use(DataFormat)
		{
			if(value != null)
			{
				if(valueType == "FLOAT")
				{
					output << """ VALUE="${value.f()}""" + '"'
				}
				else if(valueType == "BOOL")
				{
					output << """ VALUE="${value.B()}""" + '"'
				}
				else if(valueType == "CHAR")
				{
					def v = value.replaceAll(/"/, "&quot;")
					v = value.replaceAll(/</, "&lt;")
					v = value.replaceAll(/>/, "&gt;")
					output << """ VALUE="${v}""" + '"'
				}
				else
				{
					output << """ VALUE="${value}""" + '"'
				}
			}
			
			if(textValue != null)
			{
				output << ">${textValue}</PARAM>"
			}
			else
			{
				output << "/>"
			}
		}
		
		return output.toString()
	}
}