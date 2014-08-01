package org.silcongo.autopds

class Chapter
{
	def entryPoints = []
	
	public Chapter()
	{
		entryPoints << new EntryPoint(0, "Chapter1")
	}
	
	public add(starttime, normalAlias)
	{
		if(starttime == 0)
		{
			entryPoints = []
		}
		entryPoints << new EntryPoint(starttime, normalAlias)
	}

	public String toString()
	{
		return "<CHAPTER>" + entryPoints.collect{it.toString()}.join("") + "</CHAPTER>"
	}
}