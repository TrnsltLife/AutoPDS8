package org.silcongo.autopds.app

import org.silcongo.autopds.*
import org.silcongo.autopds.ctemplate.*
import org.silcongo.autopds.util.*

class ScriptProducerExcelExport
{
	def infoLog = new StringBuilder()
	def infoLogHook = {}
	def void logInfo(message)
	{
		infoLog << message
		println(message)
		infoLogHook.call(message)
	}
	
	def errorLog = new StringBuilder()
	def errorLogHook = {}
	def void logError(message)
	{
		errorLog << message
		println("ERROR: " + message)
		errorLogHook.call(message)
	}
	
	def scriptFile = ""
	def encoding = ""
	def settings = [:] //settings is a Map [:] of key-value pairs, read in from the .script file
	def colors = [:]
	
	//The following values are not set from the .script file. They are used in the creation of the movie.pds file
	def project = new Project()
	def lib = project.library
	def timeline = project.title.timeline
	
	def script = []
	def segments = []

	public ScriptProducerExcelExport(file, encoding)
	{	
		scriptFile = file
		if(!(scriptFile instanceof File)){scriptFile = new File(scriptFile)}

		this.encoding = encoding
	}

	private void readScriptFile()
	{
		//*******************************************************************
		//Read in settings and the script from the script file
		//*******************************************************************
		def scriptFound = false
		scriptFile.newReader(encoding).eachLine{line->
			line = line.trim()
			
			//ignore UTF-8 byte-order mark on first line if it is present
			if(encoding == "UTF-8" && line.indexOf("\ufeff") == 0)
			{
				line = line.substring(1, line.length());
			}
			
			if(line.startsWith("//"))
			{
				//ignore this line
			}
			else
			{
				line = line.split(/\/\//)[0] //get rid of everything after the //comment marker
				if(scriptFound)
				{
					script << line			
				}
				else
				{
					if(line == "script:")
					{
						scriptFound = true
					}
					else if(line =~ /=/)
					{
						def junk
						line = line.split(/\t/)[0]
						def firstEqual = line.indexOf("=")
						def key = line[0..(firstEqual-1)]
						key = key.toLowerCase()
						def value = line[(firstEqual+1)..-1]
						
						//If this is an expected setting, set it
						if(settings[key] != null)
						{
							if(value.toLowerCase().trim() == "true"){value = true}
							else if(value.toLowerCase().trim() == "false"){value = false}
							settings[key] = value
						}
						//Otherwise it is some other key.
						//Check if it is a color definition. r,g,b
						else if(value.trim() =~ /^[0-9]+\s*,\s*[0-9]+\s*,\s*[0-9]+$/)
						{
							//This is a color definition, e.g:
							//black=0,0,0
							//yellow=255,0,255
							//Add the color to the colors [:] map with the same name as in the .script file.
							def (r,g,b) = value.trim().split(/,/)
							colors[key] = lib.add([Integer.parseInt(r.trim()), Integer.parseInt(g.trim()), Integer.parseInt(b.trim())], Timecode.seconds(Double.parseDouble(settings.colortime)))
						}
					}
				}
			}
		}


		//Numeric settings
		if(settings.colortime instanceof String){settings.colortime = Double.parseDouble(settings.colortime)}
		if(settings.transitiontime instanceof String){settings.transitiontime = Double.parseDouble(settings.transitiontime)}
		
		settings.saveas = finalizePath(settings.saveas)
		settings.audiopath = finalizePath(settings.audiopath) + "\\"
		settings.imagepath = finalizePath(settings.imagepath) + "\\"
		settings.musicpath = finalizePath(settings.musicpath) + "\\"
		settings.textpath = finalizePath(settings.textpath) + "\\"
		settings.videopath = finalizePath(settings.videopath) + "\\"
		
		settings.each{key, value-> logInfo("$key => $value")}
		
		//Set up some default colors (if they have not been defined by the user)
		if(!colors.black)
		{
			colors.black = lib.add([0, 0, 0], Timecode.seconds(settings.colortime))
		}
		if(!colors.white)
		{
			colors.white = lib.add([255,255,255], Timecode.seconds(settings.colortime))
		}

		//Path to ffmpeg
		if(settings.ffmpeg)
		{
			FFMPEG.setPath(settings.ffmpeg)
		}
		
		//Set additional properties
		project.information = new Information(author:settings.author, company:settings.company, description:settings.description)
		
		//Aspect Ratio
		if(settings.aspectratio == "4:3")
		{
			project.information.aspectRatio = false
		}
		else if(settings.aspectratio == "16:9")
		{
			project.information.aspectRatio = true
		}
		logInfo("Aspect ratio set to 16:9? " + project.information.aspectRatio)

	}
	

	public String finalizePath(path)
	{
		//If an absolute path is specified, this leaves the path alone.
		//If a relative path is specified, this makes the path be placed relative to the scriptFile (rather than relative to the program file)
		def file = new File(path)
		if(file.isAbsolute()){return path}
		else
		{
			def relativeTo = scriptFile.getParent()
			return (new File(relativeTo + "/" + path)).getCanonicalPath()
		}
	}

		
	//Set the default settings in the settings Dictionary
	public void defaultSettings()
	{
		//Set the default filename to save as
		settings.saveas = scriptFile.getName()
		settings.saveas = settings.saveas.replaceAll(/\.txt/, "") + ".pds"
		
		settings.videopath = ""; //path to video files
		settings.audiopath = ""; //path to audio files
		settings.musicpath = ""; //path to music files
		settings.imagepath = ""; //path to image files
		settings.textpath = ""; //path to subtitle text files
		
		settings.colortime = "4"; //default number of seconds a color will be displayed
		settings.transitiontime = "2"; //default number of seconds a transition will take
		
		settings.audiolimiter = true; //if this is set to true, it means that the music tracks will always be clipped so that they never extend beyond the length of the audio track
		
		settings.aspectratio = "4:3"
		
		settings.author = "";
		settings.company = "";
		settings.description = "";
		
		settings.music1volume = "1.0"
		settings.music2volume = "1.0"
		settings.music3volume = "1.0"
		settings.audiovolume = "1.0"
		settings.videovolume = "1.0"

		settings.textbottomoffset = "0"
		settings.textfont = "GentiumAlt"
		settings.textsize = "20"
		settings.textbold = "true"
		settings.textitalic = "false"
		settings.textcolor = "255,255,255"
		settings.textbordercolor = "0,0,0"
		
		settings.ffmpeg = "ffmpeg.exe"
	}
	
	
	
	
	public void processScript()
	{
		try
		{
			processTheScript()
		}
		catch(Exception e)
		{
			logError(e.toString())
			throw e
		}
	}

	private void processTheScript()
	{
		/*
		The script portion of the file consists of separate lines. Each line has 2 to 4 fields, separated by tabs
		Field 1: a command: audio, image, video, color, trans, text
		Field 2: a filename, color name, or transition name, or blank
			Color names are black and white. Other colors can be defined in the settings portion of the script file, e.g.: red=255,0,0
			Transition names are defined in the file Transition.groovy. Examples are Fade, PageCurl, Random
			If this field is blank, the command must be "audio", and this indicates an inserted silence. Specify a duration in Field 3.
		Field 3: this field can be blank, or it can be a Duration, or it can be a Start Time
			blank - if the field is blank, the duration, start time, and stop time will be calculated automatically. Field 4 should also be blank.
			Duration - if the field contains a number, and Field 4 is blank, then Field 3 is taken as a Duration - the Duration the image, color, or audio silence lasts
			Start Time - if the field contains a number and Field 4 contains a number, then Field 3 is taken as a Start Time (offset from the start of this segment).
		Field 4: this field can be blank, or it can be a Stop Time, if Field 3 contains a Start Time
		*/
		defaultSettings()
		readScriptFile() //read in settings, and read all script lines into the script list
		segmentScript() //separate lines of the script into segments (each segment starts with a # line) in the segments list
		segments.each{segment->
			def segmentStartTime = timeline.videoLatestTime
			def variables = [:] //map of variables from the subtitles file, e.g. {var1} {var2} etc.
		
			def image = null
			def video = null
			def audio = null
			def trans = null
			def color = null
			def subtl = null
			def music = []
			music[1] = null
			music[2] = null
			music[3] = null
			def pipClip = []
			pipClip[1] = null
			def pipTrans = []
			pipTrans[1] = null

			def audioT = 0.0
			def videoT = 0.0
			def imageT = 0.0
			def colorT = 0.0
			def transT = 0.0
			def subtlT = 0.0
			def titleT = 0.0
			def musicT = []
			musicT[1] = 0.0
			musicT[2] = 0.0
			musicT[3] = 0.0
			def pipT = []
			pipT[1] = 0.0

			def imageCount = 0
			def subtitleCount = 0
			def titleCount = 0
			def pipCount = []
			pipCount[1] = 0
			
			def imageTimes = []
			
			def musicTimes = []
			musicTimes[1] = []
			musicTimes[2] = []
			musicTimes[3] = []
			
			def pipTimes = []
			pipTimes[1] = []
			
			//First count up the duration time for each type of object, so we can calculate how long images should be shown, etc.
			segment.each{line->
			
				def commandList = line.split(/\t/).toList()
				def command = commandList[0] ?: ""
				def filename = commandList[1] ?: ""
				def colorname = commandList[2] ?: ""
				def transition = commandList[3] ?: ""
				def duration = commandList[4] ?: "0.0"
				def starttime = commandList[5] ?: ""
				def stoptime = commandList[6] ?: ""
				def panzoom1 = commandList[7] ?: ""
				def panzoom2 = commandList[8] ?: ""
				def volume = commandList[9] ?: ""
				def startoffset = commandList[10] ?: "" //starting point in a video clip/audio
				def stopoffset = commandList[11] ?: "" //ending point in a video clip/audio
				
				if(filename =~ /&/)
				{
					throw new Exception("Please remove & from filename: " + filename)
				}

				/*
				starttime = doubleOrNothing(starttime.trim(), null)
				stoptime = doubleOrNothing(stoptime.trim(), null)
				*/
				starttime = parseTimeExpression(starttime.trim(), variables, null)
				stoptime = parseTimeExpression(stoptime.trim(), variables, null)
				startoffset = doubleOrNothing(startoffset.trim(), 0.0)
				stopoffset = doubleOrNothing(stopoffset.trim(), 0.0)
				if(startoffset < 0.0){startoffset *= -1}
				if(stopoffset < 0.0){stopoffset *= -1}
				
				if(starttime != null && stoptime != null)
				{
					duration = stoptime - starttime
				}
				duration = parseTimeExpression(duration, variables, 0.0)
				if(starttime != null && stoptime == null && duration !=  0.0)
				{
					stoptime = starttime + duration
				}
				else if(stoptime != null && starttime == null && duration !=  0.0)
				{
					starttime = stoptime - duration
					if(starttime < 0.0){starttime = 0.0}
				}

				def format = ""
				if(filename)
				{
					format = Library.classifyFileFormat(filename)
				}

				//Check for various commands that can occur:
				//Subtitle File
				if(command == "text" && (new File(settings.textpath + filename)).exists())
				{
					def subs = new SubtitleFile(settings.textpath + filename)
					subtlT = subs.subtitles[-1].end //the ending time of the last subtitle, indicating the total length of the subtitles
					variables = subs.variables
					println("Subtitles file ${settings.textpath + filename} contains these variables:\r\n" + 
						variables.keySet().sort().collect{k-> "\t" + k + ": " + variables[k]}.join("\r\n"))
				}
							
				//Audio file
				else if(command == "audiosilence") //silence
				{
					//A number representing audio silence, in seconds
					//logInfo("Audio silence of " + duration)
					audioT +=  duration
				}
				else if(command == "audio" && format == "audio")
				{
					//def afi = new AudioFileInfo(Library.findFileOrDie(settings.audiopath + filename).getCanonicalPath())
					//def mediaDuration = afi.filedata["seconds"]
					def mediaDuration = FFMPEG.getDuration(Library.findFileOrDie(settings.audiopath + filename).getCanonicalPath())
					//If the mediaDuration is 0, and a duration was specified, set the mediaDuration to duration. This allows user to indicate the duration of media files that can't have that information automatically read.
					if(mediaDuration == null)
					{
						mediaDuration = duration
					}
					audioT += mediaDuration
					//TODO If duration parameter is present, limit the length of the audio?
				}
				
				//Music audio file
				else if(command.startsWith("music") && command.endsWith("silence"))
				{
					int musicIndex = Integer.parseInt((command - "music") - "silence")
					musicTimes[musicIndex] << [duration:duration, type:"silence"]
				}
				else if(command.startsWith("music") && !command.endsWith("silence") && format == "audio")
				{
					int musicIndex = Integer.parseInt(command - "music")
					//def afi = new AudioFileInfo(Library.findFileOrDie(settings.audiopath + filename).getCanonicalPath())
					//def mediaDuration = afi.filedata["seconds"]
					def mediaDuration = FFMPEG.getDuration(Library.findFileOrDie(settings.audiopath + filename).getCanonicalPath())
					//If the mediaDuration is 0, and a duration was specified, set the mediaDuration to duration. This allows user to indicate the duration of media files that can't have that information automatically read.
					if(mediaDuration == null)
					{
						mediaDuration = duration
					}
					duration = mediaDuration
					//If startoffset or stopoffset parameters are present, limit the length of the music
					if(startoffset){duration -= startoffset}
					if(stopoffset){duration -= (mediaDuration - stopoffset)}
					def clipStopOffset = stopoffset ? (mediaDuration - stopoffset) : 0.0
					musicTimes[musicIndex] << [duration:duration, mediaDuration:mediaDuration, clipStopOffset:clipStopOffset, type:"file"]
				}
				
				//Image file
				else if(command == "image" && format == "image")
				{
					Library.findFileOrDie(settings.imagepath + filename)
					if(starttime != null && stoptime != null)
					{
						imageT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:filename]
					}
					else if(duration == 0.0)
					{
						//the image will be given an equal portion of time to other images with no duration specified
						imageCount++
						imageTimes << [start:starttime, stop:stoptime, duration:null, name:filename]
					}
					else
					{
						imageT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:filename]
					}
				}
				
				//Video file
				else if(command == "video" && format == "video")
				{
					Library.findFileOrDie(settings.videopath + filename)
					def mediaDuration = FFMPEG.getDuration(Library.findFileOrDie(settings.videopath + filename).getCanonicalPath())
					if(mediaDuration == null)
					{
						mediaDuration = duration
					}
					duration = mediaDuration

					//If startoffset or stopoffset parameters are present, limit the length of the video
					def clipDuration = duration
					if(startoffset){clipDuration -= startoffset}
					if(stopoffset){clipDuration -= (duration - stopoffset)}

					videoT += clipDuration
					def clipStopOffset = stopoffset ? (duration - stopoffset) : 0.0
					if(starttime != null && stoptime != null)
					{
						imageTimes << [start:starttime, stop:stoptime, duration:duration, mediaDuration:mediaDuration, clipStopOffset:clipStopOffset, name:filename]
					}
					else
					{
						imageTimes << [start:starttime, stop:stoptime, duration:duration, mediaDuration:mediaDuration, clipStopOffset:clipStopOffset, name:filename]
					}
					//Duration is required to know how long the video lasts
					//TODO It would be nice to be able to detect the video length automatically
				}
				
				//Color board
				else if(command == "color" && colors[colorname])
				{
					if(starttime != null && stoptime != null)
					{
						colorT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:colorname]
					}
					else if(duration == 0.0)
					{
						colorT += settings.colortime
						imageTimes << [start:starttime, stop:stoptime, duration:settings.colortime, name:colorname]
					}
					else
					{
						colorT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:colorname]
					}
				}
				
				//Transition
				else if(command == "trans" && (Transition.transitionNames[transition] || transition == "Random"))
				{
					if(duration == 0.0)
					{
						transT += settings.transitiontime
					}
					else
					{
						transT += duration
					}
				}
				
				//PIP video/image
				else if(command.startsWith("pip"))
				{
					int pipIndex = Integer.parseInt(command - "pip")
					if(filename == "") //silence
					{
						pipTimes[pipIndex] << [duration:duration]
					}
					else if(format == "image")
					{
						Library.findFileOrDie(settings.imagepath + filename)
						if(starttime != null && stoptime != null)
						{
							pipT[pipIndex] += duration
							pipTimes[pipIndex] << [start:starttime, stop:stoptime, duration:duration, name:filename]
						}
						else if(duration == 0.0)
						{
							//the image will be given an equal portion of time to other images with no duration specified
							pipCount[pipIndex]++
							pipTimes[pipIndex] << [start:starttime, stop:stoptime, duration:null, name:filename]
						}
						else
						{
							pipT[pipIndex] += duration
							pipTimes[pipIndex] << [start:starttime, stop:stoptime, duration:duration, name:filename]
						}
					}
				}
			}
			
			
			//*******************************************************************
			//Use the longest audio track (audio, music1, music2, music3) to determine the duration of the images
			//*******************************************************************
			//By default (when settings.audiolimiter is true), use the audio track as the limiter for music1,music2,music3 tracks.
			def longestT = audioT
			
			//Calculate the length of each music track, limited by the longestT, but only if longestT > 0
			boolean musicTCalculated = false;
			if((settings.audiolimiter && longestT > 0.0) || !settings.audiolimiter)
			{
				musicT[1] = calculateMusicTimes(musicTimes[1], longestT, settings.audiolimiter)
				musicT[2] = calculateMusicTimes(musicTimes[2], longestT, settings.audiolimiter)
				musicT[3] = calculateMusicTimes(musicTimes[3], longestT, settings.audiolimiter)
				musicTCalculated = true;
			}
			
			if(!settings.audiolimiter)
			{
				//When settings.audiolimiter is false, use the longest audio track (audio, music1, music2, or music3) to determine the duration of the images
				if(musicT[1] > longestT)
				{
					longestT = musicT[1]
				}
				if(musicT[2] > longestT)
				{
					longestT = musicT[2]
				}
				if(musicT[3] > longestT)
				{
					longestT = musicT[3]
				}
			}

			//If longestT is still 0.0, set to the length of imageT + videoT + colorT
			if(longestT == 0.0)
			{
				longestT = imageT + colorT + videoT
			}

			//If the musicT haven't been calculated yet, calculate them now, but make the audiolimiter setting be true
			if(!musicTCalculated)
			{
				musicT[1] = calculateMusicTimes(musicTimes[1], longestT, true)
				musicT[2] = calculateMusicTimes(musicTimes[2], longestT, true)
				musicT[3] = calculateMusicTimes(musicTimes[3], longestT, true)
			}
			
			//*******************************************************************
			//Calculate how much time each image should be displayed
			//*******************************************************************
			calculateImageTimes(imageTimes, longestT)
			calculateImageTimes(pipTimes[1], longestT)
			
			
			//*******************************************************************
			//Now set longestT to be the longest audio or the sum of imageT + colorT + videoT, whichever is longer
			//*******************************************************************
			if(longestT < imageT + colorT + videoT)
			{
				longestT = imageT + colorT + videoT
			}
			
			
			//*******************************************************************
			//Now add the objects to the timeline
			//*******************************************************************
			def visualObjectCount = 0
			def musicObjectCount = []
			musicObjectCount[1] = 0
			musicObjectCount[2] = 0
			musicObjectCount[3] = 0
			def pipObjectCount = []
			pipObjectCount[1] = 0
			segment.each{line->
				def commandList = line.split(/\t/).toList()
				def command = commandList[0] ?: ""
				def filename = commandList[1] ?: ""
				def colorname = commandList[2] ?: ""
				def transition = commandList[3] ?: ""
				def duration = commandList[4] ?: "0.0"
				def starttime = commandList[5] ?: ""
				def stoptime = commandList[6] ?: ""
				def panzoom1 = commandList[7] ?: ""
				def panzoom2 = commandList[8] ?: ""
				def volume = commandList[9] ?: ""
				def startoffset = commandList[10] ?: ""
				def stopoffset = commandList[11] ?: ""

				starttime = doubleOrNothing(starttime.trim(), null)
				stoptime = doubleOrNothing(stoptime.trim(), null)
				startoffset = doubleOrNothing(startoffset.trim(), 0.0)
				stopoffset = doubleOrNothing(stopoffset.trim(), 0.0)
				if(startoffset < 0.0){startoffset *= -1}
				if(stopoffset < 0.0){stopoffset *= -1}

				if(starttime != null && stoptime != null)
				{
					duration = stoptime - starttime
				}
				duration = doubleOrNothing(duration, 0.0)
				if(starttime != null && stoptime == null && duration != 0.0)
				{
					stoptime = starttime + duration
				}
				else if(stoptime != null && starttime == null && duration !=  0.0)
				{
					starttime = stoptime - duration
					if(starttime < 0.0){starttime = 0.0}
				}
				
				def format = ""
				if(filename)
				{
					format = Library.classifyFileFormat(filename)
				}

				//logInfo(line + " duration: $duration")
				//Check for various commands that can occur:
				//Subtitle File
				if(command == "text" && (new File(settings.textpath + filename)).exists())
				{
					def subs = new SubtitleFile(settings.textpath + filename)
					//logInfo("Segment Start Time: " + segmentStartTime)
					subs.subtitles.each{sub->
						subtl = new EffectSubtitle(Timecode.seconds(sub.end - sub.start))
						subtl.start = Timecode.seconds(sub.start) + segmentStartTime
						subtl.stop = Timecode.seconds(sub.end) + segmentStartTime
						addDefaultSubtitleFormatting(subtl)
						subtl << sub.text
						timeline << subtl
					}
				}
				
				//Chapter point
				else if(command == "chapter")
				{
					project.title.chapter.add(timeline.videoTrack.videoLatestTime, filename)
				}
			
				//Audio file
				else if(command == "audiosilence")
				{
					//A number representing audio silence, in seconds
					audio = timeline << Timecode.seconds(duration)
					//logInfo("Audio quiet, duration=" + audio.duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				else if(command == "audio" && format == "audio")
				{
					//Add the audio clip
					audio = timeline << lib.add(Library.findFileOrDie(settings.audiopath + filename))
					if(volume == null || volume == ""){volume = settings["audiovolume"]}
					audio.setConstantVolume(Float.parseFloat(volume))
					//logInfo("Added audio, duration=" + audio.duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//Music file
				else if(command.startsWith("music") && command.endsWith("silence"))
				{
					def musicIndex = Integer.parseInt((command - "music") - "silence")
					def listItem = (musicTimes[musicIndex])[musicObjectCount[musicIndex]]
					musicObjectCount[musicIndex]++
					if(listItem.add)
					{
						//A number representing audio silence, in seconds
						music[musicIndex] = timeline.musicTrack[musicIndex] << Timecode.seconds(listItem.duration)
						//logInfo("Music quiet, duration=" + music[musicIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
				}
				else if(command.startsWith("music") && format == "audio")
				{
					def musicIndex = Integer.parseInt(command - "music")
					def listItem = (musicTimes[musicIndex])[musicObjectCount[musicIndex]]
					musicObjectCount[musicIndex]++
					if(listItem.add)
					{
						//Add the music file to the timeline using the addClippedClip() method, which allows the music clip to be clipped to the size of the audio track for this segment
						music[musicIndex] = lib.add(Library.findFileOrDie(settings.musicpath + filename))

						//TODO: Change this back if it doesn't work.						
						//Changed this to allow duration of audio to be clipped by script file
						//music[musicIndex] = timeline.musicTrack[musicIndex].addClippedClip(music[musicIndex], 0, Timecode.seconds(listItem.stopOffset))
						def thisStopOffset = listItem.stopOffset
						if(listItem.clipStopOffset > thisStopOffset){thisStopOffset = listItem.clipStopOffset}
						music[musicIndex] = timeline.musicTrack[musicIndex].addClippedClip(music[musicIndex], Timecode.seconds(startoffset), Timecode.seconds(thisStopOffset))
						
						if(volume == null || volume == ""){volume = settings["music" + musicIndex + "volume"]}
						music[musicIndex].setConstantVolume(Float.parseFloat(volume))
						//logInfo("Added music[$musicIndex], duration=" + music[musicIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
				}
				
				//Image file
				else if(command == "image" && format == "image")
				{
					def thisImageTime = imageTimes[visualObjectCount].duration
					visualObjectCount++
					image = timeline << lib.add(Library.findFileOrDie(settings.imagepath + filename), Timecode.seconds(thisImageTime))

					//Pan/Zoom command
					if(panzoom1 != "")
					{
						if(panzoom2 == ""){panzoom2 = panzoom1}
						if(image != null)
						{
							//logInfo("panzoom from $panzoom1 to $panzoom2")
							image.panZoom = new PanZoom(panzoom1, panzoom2)
						}
					}
					
					//logInfo("Added image, duration=" + Timecode.seconds(thisImageTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//Video file
				else if(command == "video" && format == "video")
				{
					//def thisVideoTime = imageTimes[visualObjectCount].duration
					def thisVideoTime = imageTimes[visualObjectCount].mediaDuration
					def listItem = imageTimes[visualObjectCount]
					visualObjectCount++
					
					//TODO: Verify that commenting out the next line and adding the next 2 lines allows a Video clip to be added propery, and allows its start and stop times to be clipped
					//video = timeline << lib.add(Library.findFileOrDie(settings.videopath + filename), Timecode.seconds(thisVideoTime))
					video = lib.add(Library.findFileOrDie(settings.videopath + filename), Timecode.seconds(thisVideoTime))
					def thisStopOffset = listItem.stopOffset
					if(listItem.clipStopOffset > thisStopOffset){thisStopOffset = listItem.clipStopOffset}
					if(thisStopOffset == null){thisStopOffset = 0.0}
					//logInfo("startoffset: $startoffset ; stopoffset: $thisStopOffset")
					video = timeline.videoTrack.addClippedClip(video, Timecode.seconds(startoffset), Timecode.seconds(thisStopOffset))
					video = timeline.videoTrack.videoTimeline[-1]
					
					if(volume == null || volume == ""){volume = settings["videovolume"]}
					video.clipMAudio.setConstantVolume(Float.parseFloat(volume))
					//logInfo("Added video, duration=" + Timecode.seconds(duration).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}

				
				//Color board
				else if(command == "color" && colors[colorname])
				{
					def thisColorTime = imageTimes[visualObjectCount].duration
					visualObjectCount++
					color = timeline << colors[colorname].clone(Timecode.seconds(thisColorTime))
					
					//logInfo("Added color, duration=" + Timecode.seconds(thisColorTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//Transition
				if(command == "trans" && (Transition.transitionNames[transition] || transition == "Random"))
				{

					def thisTransTime
					if(duration == 0.0)
					{
						thisTransTime = settings.transitiontime
					}
					else
					{
						thisTransTime = duration
					}
					trans = timeline << new Transition(transition, Timecode.seconds(thisTransTime))
					
					//logInfo("Added trans, duration=" + Timecode.seconds(thisTransTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//PIP image/video file
				else if(command.startsWith("pip"))
				{
					def pipIndex = Integer.parseInt(command - "pip")
					def thisPIPTime = (pipTimes[pipIndex])[pipObjectCount[pipIndex]]
					pipObjectCount[pipIndex]++
					if(format == "image")
					{
						//Add the image file to the PIP timeline using the addClipAt() method, and adding in the segmentStartTime

						//first create the image for the ClipPIP
						pipClip[pipIndex] = timeline.pipTrack[pipIndex].addClipAt(lib.add(Library.findFileOrDie(settings.imagepath + filename)), Timecode.seconds(thisPIPTime.start) + segmentStartTime, Timecode.seconds(thisPIPTime.duration))

						//now create the obligagory TransitionPIP, and assigne the default style information to it (stretch the image to full-screen)
						pipTrans[pipIndex] = timeline.pipTrack[pipIndex] << new TransitionPIP(pipClip[pipIndex])
						addFullScreenImageFormatting(pipTrans[pipIndex], settings.imagepath + filename)

						//logInfo("Added pipimg[$pipIndex], duration=" + pip[pipIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
				}
			}
			
			//*******************************************************************
			//Calculate extra silence that should be inserted at the end of the audio track
			//*******************************************************************
			def extraAudioTime = 0.0
			if(audioT < longestT)
			{
				extraAudioTime = longestT - audioT

				//Power Director requires hidden audio objects to fill in dead air in the audio track.
				//This does that, adding extra blank audio time to pad the audio track out to the same length as the video track.
				audio = timeline << Timecode.seconds(extraAudioTime)

				//logInfo("Added quiet, duration=" + Timecode.seconds(extraAudioTime).toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
			}
			
			//*******************************************************************
			//Calculate extra silence that should be inserted at the end of the music track
			//*******************************************************************
			def extraMusicTime = []
			extraMusicTime[1] = 0.0
			extraMusicTime[2] = 0.0
			extraMusicTime[3] = 0.0
			for(int musicIndex=1; musicIndex<=3; musicIndex++)
			{
				if(musicT[musicIndex] < longestT)
				{
					extraMusicTime[musicIndex] = longestT - musicT[musicIndex]

					//Power Director requires hidden audio objects to fill in dead air in the audio track.
					//This does that, adding extra blank audio time to pad the audio track out to the same length as the video track.
					music[musicIndex] = timeline.musicTrack[musicIndex] << Timecode.seconds(extraMusicTime[musicIndex])

					//logInfo("Added quiet, duration=" + Timecode.seconds(extraMusicTime[musicIndex]).toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
			}
			
			//logInfo("Segment:")
			//logInfo("visualT: " + (imageT + colorT + videoT) + " latest: " + timeline.videoTrack.videoLatestTime)
			//logInfo("audioT: $audioT extra: $extraAudioTime latest: " + timeline.audioTrack.audioLatestTime)
			//logInfo("musicT[1]: ${musicT[1]} extra: ${extraMusicTime[1]} latest: " + timeline.musicTrack[1].audioLatestTime)
			//logInfo("musicT[2]: ${musicT[2]} extra: ${extraMusicTime[2]} latest: " + timeline.musicTrack[2].audioLatestTime)
			//logInfo("musicT[3]: ${musicT[3]} extra: ${extraMusicTime[3]} latest: " + timeline.musicTrack[3].audioLatestTime)
			//logInfo("transT: $transT")

			//logInfo(("") //end of a segment)
		}
	}
	
	public void segmentScript()
	{
		def segment = []
		script.each{line->
			if(line.startsWith("#"))
			{
				if(segment)
				{
					segments << segment
				}
				segment = []
			}
			else
			{
				segment << line
			}
		}
		if(segment)
		{
			segments << segment
		}
	}


	public void calculateImageTimes(List imageTimes, Number longestT)
	{
		//logInfo("calculateImageTimes: longestT $longestT")
		
		def borders = new ArrayList(imageTimes.size()+1)
		for(int i = 0; i<imageTimes.size()+1; i++)
		{
			borders[i] = null
		}
		borders[0] = 0.0
		borders[-1] = longestT

		//logInfo("Image time borders Created:")
		//logInfo(borders.join(" [IMAGE] "))
		//logInfo("")

		//Create the borders that can be entered based on start, stop, and duration data
		for(int i = 0; i<imageTimes.size()+1; i++)
		{
			if(borders[i] == null)
			{
				//logInfo("borders[$i] ")
				if(imageTimes[i].start != null)
				{
					borders[i] = imageTimes[i].start
					//logInfo("set to ${borders[i]} at A")
				}
				else if(i > 0 && imageTimes[i-1].stop != null)
				{
					borders[i] = imageTimes[i-1].stop
					//logInfo("set to ${borders[i]} at B")
				}
				else if(i > 0 && borders[i-1] != null && imageTimes[i-1].duration != null && imageTimes[i-1].duration != 0.0)
				{
					borders[i] = borders[i-1] + imageTimes[i-1].duration
					//logInfo("set to ${borders[i]} at C")
				}
				//logInfo("")
			}
		}

		//logInfo("Image time borders First Pass:")
		//logInfo(borders.join(" [IMAGE] "))
		//logInfo("")

		
		//Create the borders that can be entered based on start, stop, and duration data, and based on the previous loop
		for(int i = imageTimes.size()+1; i>=0; i--)
		{
			if(borders[i] == null)
			{
				if(i < imageTimes.size() && borders[i+1] != null && imageTimes[i].duration != null && imageTimes[i].duration != 0.0)
				{
					borders[i] = borders[i+1] - imageTimes[i].duration
					//logInfo("borders[$i] set to ${borders[i]} at D")
				}
			}
		}

		//logInfo("Image time borders Second Pass:")
		//logInfo(borders.join(" [IMAGE] "))
		//logInfo("")


		//Loop over the data and find all places where borders are missing
		def firstEmpty = 0
		def lastEmpty = 0
		def isCounting = false
		for(int i=0; i<borders.size(); i++)
		{
			//We found a null border. Start or continue counting null borders
			if(borders[i] == null)
			{
				//a null border
				if(isCounting)
				{
					lastEmpty = i
				}
				else
				{
					isCounting = true
					firstEmpty = i
					lastEmpty = i
				}
			}
			//We're already counting null borders, and we've reached a non-null border. We need to fill in the null borders now.
			else if(isCounting && borders[i] != null)
			{
				def firstStart = borders[firstEmpty-1] //get the first starting time
                def lastStop = borders[lastEmpty+1] //get the last stopping time
                
				//get the total time period covered by firstStart to lastStop
				def rangeTotalTime = lastStop - firstStart

				//loop over every image represented by the range without a start/stop time, and get its duration
				def rangeImageT = 0
				def rangeUnassignedImageCount = 0
				for(int j=firstEmpty-1; j <= lastEmpty; j++)
				{
					if(imageTimes[j].duration != null){rangeImageT += imageTimes[j].duration}
					else{rangeUnassignedImageCount++}
				}
				//logInfo("")
				
				//calculate the amount of time each image in the range without a duration should get
				def rangeEachImageTime = 0
				if(rangeUnassignedImageCount > 0)
				{
					rangeEachImageTime = (rangeTotalTime - rangeImageT) / rangeUnassignedImageCount
					//logInfo("($rangeTotalTime - $rangeImageT) / $rangeUnassignedImageCount = $rangeEachImageTime")
				}

				//logInfo("firstEmpty: $firstEmpty - lastEmpty: $lastEmpty")
				//logInfo("rangeImageT: $rangeImageT")
				//logInfo("rangeUnassignedImageCount: $rangeUnassignedImageCount")
				//logInfo("rangeEachImageTime: $rangeEachImageTime")
				
				//loop over every null border in the range and set its value
				def nextStart = firstStart
				for(int j=firstEmpty; j <= lastEmpty; j++)
				{
					if(borders[j] == null)
					{
						if(imageTimes[j].duration != null)
						{
							borders[j] = nextStart + imageTimes[j].duration
						}
						else
						{
							borders[j] = nextStart + rangeEachImageTime
						}
					}
					nextStart = borders[j]
				}
				
				//stop gathering a range of empty images, and start looking for the next range
				isCounting = false
			}
		}


		//Special case that allows the first item to specify a negative starting time
		if(imageTimes.size() > 0 && imageTimes[0].start != null && imageTimes[0].start < 0)
		{
			borders[0] = imageTimes[0].start
		}


		//logInfo("Image time borders Final Pass:")
		//logInfo(borders.join(" [IMAGE] "))
		//logInfo("")

		//loop over every image/video/color in imageTimes and set its start, stop, and duration
		for(int i=0; i<imageTimes.size(); i++)
		{
			imageTimes[i].start = borders[i]
            imageTimes[i].stop = borders[i+1]
            imageTimes[i].duration = imageTimes[i].stop - imageTimes[i].start
		}
	}
	
		
	
	public Number calculateMusicTimes(musicTimes, longestT, audiolimiter)
	{
		def musicT = 0.0
		//logInfo("MusicTimes")
		if(audiolimiter == true)
		{
			//To make sure the length of every silence is respected, we subtract the silence durations here
			def longestTMinusSilence = longestT
			musicTimes.each{listItem->
				if(listItem.type == "silence")
				{
					longestTMinusSilence -= listItem.duration
				}
			}
			
			musicTimes.each{listItem->
				//Limit the music track to not extend beyond the audio track
				if(listItem.type == "silence")
				{
					def duration = listItem.duration
					//add the length of the silence back into longestTMinusSilence
					longestTMinusSilence += duration
					musicT += duration
					listItem.add = true
					listItem.stopOffset = 0
				}
				//Sound files, unlike silences, are allowed to be clipped, or removed entirely. Check if that is needed.
				else
				{
					if(musicT < longestTMinusSilence)
					{
						def originalDuration = listItem.duration
						def duration = originalDuration

						if((musicT + originalDuration) > longestTMinusSilence)
						{
							//logInfo("Limiting music/silence from $originalDuration to ${longestTMinusSilence - musicT}")
							duration = longestTMinusSilence - musicT
						}
						musicT +=  duration
						listItem.add = true
						listItem.duration = duration
						listItem.stopOffset = (originalDuration - duration)
					}
					else
					{
						//logInfo("Removing music/silence, not enough room")
						listItem.add = false
					}
				}
				//logInfo(listItem)
			}
		}
		else
		{
			musicTimes.each{listItem->
				musicT += listItem.duration
				listItem.add = true
				listItem.stopOffset = 0
				//logInfo(listItem)
			}
		}
		//logInfo("")
		return musicT
	}
	
	
	
	public String createXML()
	{
		return project.toString()
	}
	
	public void createXMLFile()
	{
		PDSFile.writeFile(settings.saveas, project)
	}
	
	public Double parseTimeExpression(String s, Map variables, Double defaultValue)
	{
		Binding binding = new Binding(variables)
		GroovyShell shell = new GroovyShell(binding)
		Object value = shell.evaluate(s)
		return doubleOrNothing(value, defaultValue)
	}
	
	public Double doubleOrNothing(String s, Double nothing)
	{
		Double d
		//Converts a string to a double. If the conversion throws an error, or the string is empty, returns the contents of nothing
		if(s != null && s != "")
		{
			try
			{
				d = Double.parseDouble(s)
			}
			catch(Exception e){d = nothing}
		}
		else
		{
			d = nothing
		}
		return d
	}
	
	public Double doubleOrNothing(Double inD, Double nothing)
	{
		Double d
		//Converts a string to a double. If the conversion throws an error, or the string is empty, returns the contents of nothing
		if(inD != null)
		{
			try
			{
				d = inD.doubleValue()
			}
			catch(Exception e){d = nothing}
		}
		else
		{
			d = nothing
		}
		return d
	}

	public List colorStringToList(String colors)
	{
		return colors.split(/,/).collect{Integer.parseInt(it.trim())}
	}
	
	public void addDefaultSubtitleFormatting(subtl)
	{
		subtl << new MetaLayer(MetaLayer.TEXT_MEDIA)
		subtl << new MetaString(left:0, top:0, width:320, height:240, xOffset:10.0, yOffset:Float.parseFloat(settings.textbottomoffset))
		subtl << new MetaParagraph(width:320, height:240, align:MetaParagraph.ALIGN_LEFT)
		subtl << new MetaLine(width:320, height:240)
		subtl << new MetaFont(settings.textfont, Integer.parseInt(settings.textsize), Boolean.valueOf(settings.textbold), Boolean.valueOf(settings.textitalic))
		//subtl << new MetaFace(color1:[255,255,255])
		subtl << new MetaFace(color1:colorStringToList(settings.textcolor))
		subtl << new MetaBorder(color1:colorStringToList(settings.textbordercolor))
		subtl << new MetaShadow(enableFace:false, enableBorder:false, color1:[0,0,0], gradType:5, alpha:255, blurRadius:5, offsetX:2.095541, offsetY:-2.096768, height:false)
		
		subtl << new MetaPath(index:1, keyFrame:0.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
		subtl << new MetaPath(index:2, keyFrame:0.333333, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:3, keyFrame:0.666667, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:4, keyFrame:1.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
	}
	
	public void addDefaultTitleFormatting(subtl)
	{
		subtl << new MetaLayer(MetaLayer.TEXT_MEDIA)
		subtl << new MetaString(left:0, top:0, width:320, height:240, xOffset:10.0, yOffset:-10.0)
		subtl << new MetaParagraph(width:320, height:240)
		subtl << new MetaLine(width:320, height:240)
		subtl << new MetaFont("Gentium", 20, true, false)
		subtl << new MetaFace(color1:[255,255,255])
		subtl << new MetaBorder(color1:[0,0,0])
		subtl << new MetaShadow(enableFace:true, enableBorder:true, color1:[0,0,0], gradType:5, alpha:255, blurRadius:5, offsetX:2.095541, offsetY:-2.096768, height:false)
		
		subtl << new MetaPath(index:1, keyFrame:0.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
		subtl << new MetaPath(index:2, keyFrame:0.333333, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:3, keyFrame:0.666667, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:4, keyFrame:1.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
	}

	public void addFullScreenTitleImageFormatting(template, imageFile)
	{
		template << new MetaLayer(MetaLayer.IMAGE_MEDIA)
		template << new MetaPicture()
		template << new MetaSource(imageFile)
		template << new MetaFace(enable:true, alpha:255, blurRadius:0, enableBlendingColor:false, blendingColor:ColorUtils.getColorValue(255,255,255))
		template << new MetaBorder(enable:false, size:5, color1:[255,255,255], color2:[255,255,255], alpha:255, blurRadius:0, borderType:1, valueType:0)
		template << new MetaShadow(enableShape:false, enableBorder:false, color1:[0,0,0], color2:[0,0,0], gradType:7, alpha:128, blurRadius:0, offsetX:3.0, offsetY:3.0, height:false)
		template << new MetaPosition1()
		/*
		template << new MetaMotion1(index:1, keyframe:0.0)
		template << new MetaMotion1(index:2, keyframe:0.333333)
		template << new MetaMotion1(index:3, keyframe:0.666667)
		template << new MetaMotion1(index:4, keyframe:1.0)
		*/
	}
	
	public void addFullScreenImageFormatting(cpicture, imageFile)
	{
		cpicture << new MetaPicture()
		cpicture << new MetaSource(imageFile)
		cpicture << new MetaFace(enable:true, alpha:255, blurRadius:0, enableBlendingColor:false, blendingColor:ColorUtils.getColorValue(255,255,255))
		cpicture << new MetaBorder(enable:false, size:5, color1:[255,255,255], color2:[255,255,255], alpha:255, blurRadius:0, borderType:1, valueType:0)
		cpicture << new MetaShadow(enableShape:false, enableBorder:false, color1:[0,0,0], color2:[0,0,0], gradType:7, alpha:128, blurRadius:0, offsetX:3.0, offsetY:3.0, height:false)
		cpicture << new MetaPosition1()
	}
}