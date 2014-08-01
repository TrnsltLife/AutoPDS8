/*
 *	AudioFileInfo.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2004 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/
package org.silcongo.autopds.util;


import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

import java.net.URL;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class AudioFileInfo
{
	private static final int	LOAD_METHOD_STREAM = 1;
	private static final int	LOAD_METHOD_FILE = 2;
	private static final int	LOAD_METHOD_URL = 3;

	public Map<String, Object> filedata = new HashMap();

	public AudioFileInfo(String[] args)
	{
		if (args.length == 0)
		{
			return;
		}
		int	nLoadMethod = LOAD_METHOD_FILE;
		boolean	bCheckAudioInputStream = false;
		boolean	bOutputProperties = false;
		int	nCurrentArg = 0;
		while (nCurrentArg < args.length)
		{
			if (args[nCurrentArg].equals("-s"))
			{
				nLoadMethod = LOAD_METHOD_STREAM;
			}
			else if (args[nCurrentArg].equals("-f"))
			{
				nLoadMethod = LOAD_METHOD_FILE;
			}
			else if (args[nCurrentArg].equals("-u"))
			{
				nLoadMethod = LOAD_METHOD_URL;
			}
			else if (args[nCurrentArg].equals("-i"))
			{
				bCheckAudioInputStream = true;
			}
			else if (args[nCurrentArg].equals("-p"))
			{
				bOutputProperties = true;
			}

			nCurrentArg++;
		}
		String	strSource = args[nCurrentArg - 1];
		String	strFilename = null;
		AudioFileFormat	aff = null;
		AudioInputStream ais = null;
		try
		{
			switch (nLoadMethod)
			{
			case LOAD_METHOD_STREAM:
				InputStream	inputStream = System.in;
				aff = AudioSystem.getAudioFileFormat(inputStream);
				strFilename = "<standard input>";
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(inputStream);
				}
				break;

			case LOAD_METHOD_FILE:
				File	file = new File(strSource);
				aff = AudioSystem.getAudioFileFormat(file);
				strFilename = file.getCanonicalPath();
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(file);
				}
				break;

			case LOAD_METHOD_URL:
				URL	url = new URL(strSource);
				aff = AudioSystem.getAudioFileFormat(url);
				strFilename = url.toString();
				if (bCheckAudioInputStream)
				{
					ais = AudioSystem.getAudioInputStream(url);
				}
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		if (aff == null)
		{
			System.out.println("Cannot determine format");
		}
		else
		{
			outputFileFormat(strFilename, aff);
			if (bCheckAudioInputStream)
			{
				outputAudioInputStream(ais);
			}
			if (bOutputProperties)
			{
				Map<String, Object> p = aff.properties();
				filedata.putAll(p);
				p = aff.getFormat().properties();
				filedata.putAll(p);
			}
		}
	}


	private void outputFileFormat(String strFilename,
										 AudioFileFormat aff)
	{
		AudioFormat format = aff.getFormat();
		filedata.put("filename", strFilename);
		filedata.put("type", aff.getType());
		filedata.put("format", format);

		if (aff.getFrameLength() != AudioSystem.NOT_SPECIFIED)
		{
			filedata.put("framelength", aff.getFrameLength());
			filedata.put("framesize", format.getFrameSize());
			filedata.put("framerate", format.getFrameRate());
			filedata.put("bytelength", aff.getFrameLength() * format.getFrameSize());
			filedata.put("seconds", (aff.getFrameLength() / format.getFrameRate()));
		}
		else
		{
			filedata.put("framelength", null);
			filedata.put("framesize", null);
			filedata.put("framerate", null);
			filedata.put("bytelength", null);
			filedata.put("seconds", null);
		}

		String	strFileLength = null;
		if (aff.getByteLength() != AudioSystem.NOT_SPECIFIED)
		{
			filedata.put("filebytelength", aff.getByteLength());
		}
		else
		{
			filedata.put("filebytelength", null);
		}
	}


	private void outputAudioInputStream(AudioInputStream ais)
	{
		AudioFormat format = ais.getFormat();
		if (ais.getFrameLength() != AudioSystem.NOT_SPECIFIED)
		{
			filedata.put("framelength", ais.getFrameLength());
			filedata.put("framesize", format.getFrameSize());
			filedata.put("framerate", format.getFrameRate());
			filedata.put("bytelength", ais.getFrameLength() * format.getFrameSize());
			filedata.put("seconds", (ais.getFrameLength() / format.getFrameRate()));
		}
		else
		{
			filedata.put("framelength", null);
			filedata.put("framesize", null);
			filedata.put("framerate", null);
			filedata.put("bytelength", null);
			filedata.put("seconds", null);
		}
	}
}



/*** AudioFileInfo.java ***/

