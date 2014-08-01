package org.silcongo.autopds.app

import org.silcongo.autopds.*
import org.silcongo.autopds.ctemplate.*
import org.silcongo.autopds.util.*

class ScriptProducer
{
	def scriptFile = ""
	def settings = [:] //settings is a Map [:] of key-value pairs, read in from the .script file
	def colors = [:]
	
	//The following values are not set from the .script file. They are used in the creation of the movie.pds file
	def project = new Project()
	def lib = project.library
	def timeline = project.title.timeline
	
	def script = []
	def segments = []

	
	public ScriptProducer(file, encoding)
	{	
		scriptFile = file
		if(!(scriptFile instanceof File)){scriptFile = new File(scriptFile)}
		
		defaultSettings()
		
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
		
		//Aspect Ratio
		if(settings.aspectratio == "4:3")
		{
			project.information.aspectRatio = false
		}
		else if(settings.aspectratio == "16:9")
		{
			project.information.aspectRatio = true
		}

settings.each{key, value-> println("$key => $value")}
		
		//Set up some default colors (if they have not been defined by the user)
		if(!colors.black)
		{
			colors.black = lib.add([0, 0, 0], Timecode.seconds(settings.colortime))
		}
		if(!colors.white)
		{
			colors.white = lib.add([255,255,255], Timecode.seconds(settings.colortime))
		}

		
		//Set additional properties
		project.information = new Information(author:settings.author, company:settings.company, description:settings.description)
		

		//*******************************************************************
		//Process the script
		//*******************************************************************
		processScript()
	}
	
	

		
	//Set the default settings in the settings Dictionary
	public void defaultSettings()
	{
		//Set the default filename to save as
		settings.saveas = scriptFile.getName()
		settings.saveas = settings.saveas.replaceAll(/\.script/, "") + ".pds"
		
		settings.videopath = ""; //path to video files
		settings.audiopath = ""; //path to audio files
		settings.music1path = ""; //path to music files
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
	}
	
	
	
	
	public void processScript()
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
		segmentScript()
		segments.each{segment->
			def segmentStartTime = timeline.videoLatestTime
		
			def image = null
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
			
				def command = ""
				def file = ""
				Double starttime = null
				Double stoptime = null
				Double duration = 0.0
				def fields = line.split(/\t/)
				if(fields.size() > 0){command = fields[0].trim()}
				if(fields.size() > 1){file = fields[1].trim()}
				if(fields.size() > 3)
				{
					starttime = doubleOrNothing(fields[2].trim(), null)
					stoptime = doubleOrNothing(fields[3].trim(), null)
					if(starttime != null && stoptime != null)
					{
						duration = stoptime - starttime
					}
				}
				else if(fields.size() > 2)
				{
					starttime = null
					stoptime = null
					duration = doubleOrNothing(fields[2].trim(), 0.0)
				}


				//Check for various commands that can occur:
				
				def format = Library.classifyFileFormat(file)

				//Subtitle File
				if(command == "text" && (new File(settings.textpath + file)).exists())
				{
					def subs = new SubtitleFile(settings.textpath + file)
					subtlT = subs.subtitles[-1].end //the ending time of the last subtitle, indicating the total length of the subtitles
				}
							
				//Audio file
				else if(command == "audio")
				{
					if(file == "") //silence
					{
						//A number representing audio silence, in seconds
						//println("Audio silence of " + duration)
						audioT +=  duration
					}
					else if(format == "audio")
					{
						def afi = new AudioFileInfo(Library.findFileOrDie(settings.audiopath + file).getCanonicalPath())
						def mediaDuration = afi.filedata["seconds"]
						//If the mediaDuration is 0, and a duration was specified, set the mediaDuration to duration. This allows user to indicate the duration of media files that can't have that information automatically read.
						if(mediaDuration == null)
						{
							mediaDuration = duration
						}
						audioT += mediaDuration
						//TODO If duration parameter is present, limit the length of the audio?
					}
				}
				
				//Music audio file
				else if(command.startsWith("music"))
				{
					int musicIndex = Integer.parseInt(command - "music")
					if(file == "") //silence
					{
						musicTimes[musicIndex] << [duration:duration, type:"silence"]
					}
					else if(format == "audio")
					{
						def afi = new AudioFileInfo(Library.findFileOrDie(settings.audiopath + file).getCanonicalPath())
						def mediaDuration = afi.filedata["seconds"]
						//If the mediaDuration is 0, and a duration was specified, set the mediaDuration to duration. This allows user to indicate the duration of media files that can't have that information automatically read.
						if(mediaDuration == null)
						{
							mediaDuration = duration
						}
						duration = mediaDuration			
						musicTimes[musicIndex] << [duration:duration, type:"file"]
						//TODO If duration parameter is present, limit the length of the music?
					}
				}
				
				//Image file
				else if(command == "image" && format == "image")
				{
					Library.findFileOrDie(settings.imagepath + file)
					if(starttime != null && stoptime != null)
					{
						imageT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:file]
					}
					else if(duration == 0.0)
					{
						//the image will be given an equal portion of time to other images with no duration specified
						imageCount++
						imageTimes << [start:null, stop:null, duration:null, name:file]
					}
					else
					{
						imageT += duration
						imageTimes << [start:null, stop:null, duration:duration, name:file]
					}
				}
				
				//Video file
				else if(command == "video" && format == "video")
				{
					Library.findFileOrDie(settings.videopath + file)
					videoT += duration
					if(starttime != null && stoptime != null)
					{
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:file]
					}
					else
					{
						imageTimes << [start:null, stop:null, duration:duration, name:file]
					}
					//Duration is required to know how long the video lasts
					//TODO It would be nice to be able to detect the video length automatically
				}
				
				//Color board
				else if(command == "color" && colors[file])
				{
					if(starttime != null && stoptime != null)
					{
						colorT += duration
						imageTimes << [start:starttime, stop:stoptime, duration:duration, name:file]
					}
					else if(duration == 0.0)
					{
						colorT += settings.colortime
						imageTimes << [start:null, stop:null, duration:settings.colortime, name:file]
					}
					else
					{
						colorT += duration
						imageTimes << [start:null, stop:null, duration:duration, name:file]
					}
				}
				
				//Transition
				else if(command == "trans" && (Transition.transitionNames[file] || file == "Random"))
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
					if(file == "") //silence
					{
						pipTimes[pipIndex] << [duration:duration]
					}
					else if(format == "image")
					{
						Library.findFileOrDie(settings.imagepath + file)
						if(starttime != null && stoptime != null)
						{
							pipT[pipIndex] += duration
							pipTimes[pipIndex] << [start:starttime, stop:stoptime, duration:duration, name:file]
						}
						else if(duration == 0.0)
						{
							//the image will be given an equal portion of time to other images with no duration specified
							pipCount[pipIndex]++
							pipTimes[pipIndex] << [start:null, stop:null, duration:null, name:file]
						}
						else
						{
							pipT[pipIndex] += duration
							pipTimes[pipIndex] << [start:null, stop:null, duration:duration, name:file]
						}
					}
				}
			}
			
			
			//*******************************************************************
			//Use the longest audio track (audio, music1, music2, music3) to determine the duration of the images
			//*******************************************************************
			//By default (when settings.audiolimiter is true), use the audio track as the limiter for music1,music2,music3 tracks.
			def longestT = audioT
			
			//Calculate the length of each music track
			musicT[1] = calculateMusicTimes(musicTimes[1], longestT, settings.audiolimiter)
			musicT[2] = calculateMusicTimes(musicTimes[2], longestT, settings.audiolimiter)
			musicT[3] = calculateMusicTimes(musicTimes[3], longestT, settings.audiolimiter)
			
			if(!settings.audiolimiter)
			{
				//When settings.audiolimiter is false, use the longest audio track (audio, music1, music2, or music3) to determine the duration of the images
				if(musicT[1] > longestT)
				{
					longestT = musicT[1]
				}
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
				def command = ""
				def file = ""
				Double starttime = null
				Double stoptime = null
				Double duration = 0.0
				def fields = line.split(/\t/)
				if(fields.size() > 0){command = fields[0].trim()}
				if(fields.size() > 1){file = fields[1].trim()}
				if(fields.size() > 3)
				{
					starttime = doubleOrNothing(fields[2].trim(), null)
					stoptime = doubleOrNothing(fields[3].trim(), null)
					if(starttime != null && stoptime != null)
					{
						duration = stoptime - starttime
					}
				}
				else if(fields.size() > 2)
				{
					starttime = null
					stoptime = null
					duration = doubleOrNothing(fields[2].trim(), 0.0)
				}


				//Check for various commands that can occur:
				
				def format = Library.classifyFileFormat(file)

				//Subtitle File
				if(command == "text" && (new File(settings.textpath + file)).exists())
				{
					def subs = new SubtitleFile(settings.textpath + file)
					//println("Segment Start Time: " + segmentStartTime)
					subs.subtitles.each{sub->
						subtl = new EffectSubtitle(Timecode.seconds(sub.end - sub.start))
						subtl.start = Timecode.seconds(sub.start) + segmentStartTime
						subtl.stop = Timecode.seconds(sub.end) + segmentStartTime
						addDefaultSubtitleFormatting(subtl)
						subtl << sub.text
						timeline << subtl
					}
				}
			
				//Audio file
				else if(command == "audio")
				{
					if(file == "") //silence
					{
						//A number representing audio silence, in seconds
						audio = timeline << Timecode.seconds(duration)
						//println("Audio quiet, duration=" + audio.duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
					else if(format == "audio")
					{
						//Add the audio clip
						audio = timeline << lib.add(Library.findFileOrDie(settings.audiopath + file))
						audio.setConstantVolume(Float.parseFloat(settings["audiovolume"]))
						//println("Added audio, duration=" + audio.duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
				}
				
				//Music file
				else if(command.startsWith("music"))
				{
					def musicIndex = Integer.parseInt(command - "music")
					def listItem = (musicTimes[musicIndex])[musicObjectCount[musicIndex]]
					musicObjectCount[musicIndex]++
					if(listItem.add)
					{
						if(file == "") //silence
						{
							//A number representing audio silence, in seconds
							music[musicIndex] = timeline.musicTrack[musicIndex] << Timecode.seconds(listItem.duration)
							//println("Music quiet, duration=" + music[musicIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
						}
						else if(format == "audio")
						{
							//Add the music file to the timeline using the addClippedClip() method, which allows the music clip to be clipped to the size of the audio track for this segment
							music[musicIndex] = lib.add(Library.findFileOrDie(settings.music1path + file))
							music[musicIndex] = timeline.musicTrack[musicIndex].addClippedClip(music[musicIndex], 0, Timecode.seconds(listItem.stopOffset))
							music[musicIndex].setConstantVolume(Float.parseFloat(settings["music" + musicIndex + "volume"]))
							//println("Added music[$musicIndex], duration=" + music[musicIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
						}
					}
				}
				
				//Image file
				else if(command == "image" && format == "image")
				{
					def thisImageTime = imageTimes[visualObjectCount].duration
					visualObjectCount++
					image = timeline << lib.add(Library.findFileOrDie(settings.imagepath + file), Timecode.seconds(thisImageTime))

					//println("Added image, duration=" + Timecode.seconds(thisImageTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//Video file
				else if(command == "video" && format == "video")
				{
					def thisVideoTime = imageTimes[visualObjectCount].duration
					visualObjectCount++
					video = timeline << lib.add(Library.findFileOrDie(settings.videopath + file), thisVideoTime)
					
					//println("Added video, duration=" + Timecode.seconds(duration).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}

				
				//Color board
				else if(command == "color" && colors[file])
				{
					def thisColorTime = imageTimes[visualObjectCount].duration
					visualObjectCount++
					color = timeline << colors[file].clone(Timecode.seconds(thisColorTime))
					
					//println("Added color, duration=" + Timecode.seconds(thisColorTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
				
				//Transition
				else if(command == "trans" && (Transition.transitionNames[file] || file == "Random"))
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
					trans = timeline << new Transition(file, Timecode.seconds(thisTransTime))
					
					//println("Added trans, duration=" + Timecode.seconds(thisTransTime).toString().padLeft(10, " ") + line.padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
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
						pipClip[pipIndex] = timeline.pipTrack[pipIndex].addClipAt(lib.add(Library.findFileOrDie(settings.imagepath + file)), Timecode.seconds(thisPIPTime.start) + segmentStartTime, Timecode.seconds(thisPIPTime.duration))

						//now create the obligagory TransitionPIP, and assigne the default style information to it (stretch the image to full-screen)
						pipTrans[pipIndex] = timeline.pipTrack[pipIndex] << new TransitionPIP(pipClip[pipIndex])
						addFullScreenImageFormatting(pipTrans[pipIndex], settings.imagepath + file)

						//println("Added pipimg[$pipIndex], duration=" + pip[pipIndex].duration.toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
					}
				}
				
				//Pan/Zoom command
				else if(command.equals("panzoom"))
				{
				println("panzoom")
					if(image != null)
					{
						def from
						def to
						(from, to) = file.split(/-/)
				println("panzoom from $from to $to")
						image.panZoom = new PanZoom(from, to)
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

				//println("Added quiet, duration=" + Timecode.seconds(extraAudioTime).toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
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

					//println("Added quiet, duration=" + Timecode.seconds(extraMusicTime[musicIndex]).toString().padLeft(10, " ") + "".padRight(15, " ") + " V" + timeline.videoLatestTime.toString().padLeft(12, " ") + " A" + timeline.audioLatestTime.toString().padLeft(12, " "))
				}
			}
			
			//println("Segment:")
			//println("visualT: " + (imageT + colorT + videoT) + " latest: " + timeline.videoTrack.videoLatestTime)
			//println("audioT: $audioT extra: $extraAudioTime latest: " + timeline.audioTrack.audioLatestTime)
			//println("musicT[1]: ${musicT[1]} extra: ${extraMusicTime[1]} latest: " + timeline.musicTrack[1].audioLatestTime)
			//println("musicT[2]: ${musicT[2]} extra: ${extraMusicTime[2]} latest: " + timeline.musicTrack[2].audioLatestTime)
			//println("musicT[3]: ${musicT[3]} extra: ${extraMusicTime[3]} latest: " + timeline.musicTrack[3].audioLatestTime)
			//println("transT: $transT")

			//println("") //end of a segment
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
		//*******************************************************************
		//Calculate how much time each image should get. Used for images in videoTrack, also used for PinP tracks
		//*******************************************************************
		def firstEmpty = 0
		def lastEmpty = 0
		def isCounting = false
		for(int i=0; i<=imageTimes.size(); i++)
		{
			if(i == imageTimes.size() /*i.e. index after the last record in imageTimes*/ || (imageTimes[i].start != null && imageTimes[i].stop != null))
			{
				if(isCounting)
				{
					if(i == imageTimes.size())
					{
						lastEmpty = i-1
					}
					
					//Calculate the start and stop times for every element from firstEmpty to lastEmpty
					
					//get the first starting time
					def firstStart = 0.0
					if(firstEmpty != 0){firstStart = imageTimes[firstEmpty - 1].stop}

					//get the last ending time
					def lastStop = longestT
					if(lastEmpty < imageTimes.size()-1){lastStop = imageTimes[lastEmpty+1].start}

					//get the total time period covered by firstStart to lastStop
					def rangeTotalTime = lastStop - firstStart
					
					//loop over every image in the range without a start & stop time, and get its duration
					def rangeImageT = 0
					def rangeUnassignedImageCount = 0
					for(int j=firstEmpty; j <= lastEmpty; j++)
					{
						if(imageTimes[j].duration != null){rangeImageT += imageTimes[j].duration}
						else{rangeUnassignedImageCount++}
					}
					
					//calculate the amount of time each image in the range without a duration should get
					def rangeEachImageTime = 0
					if(rangeUnassignedImageCount > 0)
					{
						rangeEachImageTime = (rangeTotalTime - rangeImageT) / rangeUnassignedImageCount
						//println("($rangeTotalTime - $rangeImageT) / $rangeUnassignedImageCount = $rangeEachImageTime")
					}
					
					//loop over every image in the range without a start & stop time, and set its start and stop
					def nextStart = firstStart
					for(int j=firstEmpty; j <= lastEmpty; j++)
					{
						if(imageTimes[j].duration == null){imageTimes[j].duration = rangeEachImageTime}
						imageTimes[j].start = nextStart
						imageTimes[j].stop = nextStart + imageTimes[j].duration
						nextStart = imageTimes[j].stop
					}
					
					//stop gathering a range of empty images, and start looking for the next range
					isCounting = false
				}
			}
			else
			{
				//an image/color/video with no start and stop time
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
		}
	}
	
	
	public Number calculateMusicTimes(musicTimes, longestT, audiolimiter)
	{
		def musicT = 0.0
		//println("MusicTimes")
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
							//println("Limiting music/silence from $originalDuration to ${longestTMinusSilence - musicT}")
							duration = longestTMinusSilence - musicT
						}
						musicT +=  duration
						listItem.add = true
						listItem.duration = duration
						listItem.stopOffset = (originalDuration - duration)
					}
					else
					{
						//println("Removing music/silence, not enough room")
						listItem.add = false
					}
				}
				//println(listItem)
			}
		}
		else
		{
			musicTimes.each{listItem->
				musicT += listItem.duration
				listItem.add = true
				listItem.stopOffset = 0
				//println(listItem)
			}
		}
		//println("")
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
	
	public Double doubleOrNothing(String s, Double nothing)
	{
		double d
		//Converts a string to a double. If the conversion throws an error, or the string is empty, returns the contents of nothing
		if(s != "")
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
	
	public void addDefaultSubtitleFormatting(subtl)
	{
		subtl << new MetaLayer(MetaLayer.TEXT_MEDIA)
		subtl << new MetaString(left:0, top:0, width:320, height:240, xOffset:10.0, yOffset:-10.0)
		subtl << new MetaParagraph(width:320, height:240, align:MetaParagraph.ALIGN_LEFT)
		subtl << new MetaLine(width:320, height:240)
		subtl << new MetaFont("GentiumAlt", 20, true, false)
		subtl << new MetaFace(color1:[255,255,255])
		subtl << new MetaBorder(color1:[0,0,0])
		subtl << new MetaShadow(enableFace:false, enableBorder:false, color1:[0,0,0], gradType:5, alpha:255, blurRadius:5, offsetX:2.095541, offsetY:-2.096768, height:false)
		
		subtl << new MetaPath(index:1, keyFrame:0.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
		subtl << new MetaPath(index:2, keyFrame:0.333333, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:3, keyFrame:0.666667, pathID:"PATH_NOEFFECT", pathProgress:1.0)
		subtl << new MetaPath(index:4, keyFrame:1.0, pathID:"PATH_NOEFFECT", pathProgress:0.0)
	}
	
	public void addDefaultTitleFormatting(subtl)
	{
		subtl << new MetaLayer(MetaLayer.TEXT_MEDIA)
		subtl << new MetaString(left:0, top:0, width:320, height:240, xOffset:10.0, yOffset:-5.0)
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