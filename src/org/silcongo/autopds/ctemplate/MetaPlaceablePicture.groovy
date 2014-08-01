package org.silcongo.autopds.ctemplate

import org.silcongo.autopds.util.*

abstract class MetaPlaceablePicture
{
	def centerX = 0.5 //0.5 is horizontal center of the screen (0.0 - 1.0 are on-screen coordinates)
	def centerY = 0.5 //0.5 is vetical center of the screen (0.0 - 1.0 are on-screen coordinates)
	def width = 1.0 //1.0 is full width of screen
	def height = 1.0 //1.0 is full height of screen
	def degree = 0.0
	def alpha = 255
	def tangentVectorX1 = 1.0
	def tangentVectorY1 = 0.0
	def tangentVectorX2 = 0.0
	def tangentVectorY2 = 0.0
	
	public String toString()
	{
		//prints out just the variables declared in MetaPlaceablePicture. Subclasses could call this.
		def output = new StringBuilder()
		
		use(DataFormat)
		{
			output << """CenterX="${centerX.f()}" CenterY="${centerY.f()}" Width="${width.f()}" Height="${height.f()}" Degree="${degree.f()}" Alpha="${alpha.i()}" TangentVectorX1="${tangentVectorX1.f()}" TangentVectorY1="${tangentVectorY1.f()}" TangentVectorX2="${tangentVectorX2.f()}" TangentVectorY2="${tangentVectorY2.f()}""" + '"'
		}
		
		return output.toString()
	}
}