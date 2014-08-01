package org.silcongo.autopds

abstract class Clip
{
	def id
	def idRef
	def mediaObject
	def name = ""
	def mediaDuration = 0 //actual duration of the un-clipped media file
	def duration = 0	//duration of the media file that is displayed in the movie (clipped)
	def start = 0
	def stop = 0
	def sceneStart = 0
	def sceneStop = 0
	def mediaStart = 0
	def mediaStop = 0
	def mute = false
	def stretchMode = ""
	def panovisionMode = 0
	def startOffset = 0 //this duration will be clipped off the start of the clip
	def stopOffset = 0 //this duration will be clipped off the end of the clip
	
	//ClipExtraData
	def aspectRatioX = -1
	def aspectRatioY = -1
	def alias = ""
	def oriFileName = "(null)"
	def autoSetFocus = false
	
	public abstract void recalculateStopTimes();
}