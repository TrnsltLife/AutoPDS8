package org.silcongo.autopds

class EntryPoint
{
	//<ENTRYPOINT TYPE="NORMAL" INVALID="FALSE" ALIAS="Chapter 1" START="0"/>
	//<ENTRYPOINT TYPE="BITMAP" INVALID="FALSE" ALIAS="" START="0"/>
	
	def normalAlias = ""
	def bitmapAlias = ""
	def start = 0

	public EntryPoint(starttime, nalias="", balias="")
	{
		start = starttime
		normalAlias = nalias
		bitmapAlias = balias
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		output << """<ENTRYPOINT TYPE="NORMAL" INVALID="FALSE" ALIAS="$normalAlias" START="$start"/>""" +
					 """<ENTRYPOINT TYPE="BITMAP" INVALID="FALSE" ALIAS="$bitmapAlias" START="$start"/>"""
		return output.toString()
	}
}
