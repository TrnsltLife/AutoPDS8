package org.silcongo.autopds.ctemplate

abstract class MetaPlaceable
{
	def index = 0
	def left = "omit"
	def top = "omit"
	def width = "omit"
	def height = "omit"
	
	public MetaPlaceable(){}
	
	public MetaPlaceable(l, t)
	{
		left = l
		top = t
	}
	
	public MetaPlaceable(l, t, w, h)
	{
		left = l
		top = t
		width = w
		height = h
	}
}