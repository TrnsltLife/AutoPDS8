package org.silcongo.autopds.util;

import java.util.*;
import java.io.*;

public class MultiLinePropertyReader extends HashMap
{
	File file;
	String inputEncoding;
	
	public MultiLinePropertyReader(String path, String encoding)
	{
		this(new File(path), encoding);
	}
	
	public MultiLinePropertyReader(File f, String encoding)
	{
		file = f;
		inputEncoding = encoding;
		loadProperties();
	}
	
	public void reload()
	{
		loadProperties();
	}
	
	public void loadProperties()
	{
		this.clear();
		if(file.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, inputEncoding);
				BufferedReader in = new BufferedReader(isr);
				
				boolean firstLine = true;
				boolean multiLine = false;
				String multiLineKey = "";
				String line = "";
				
				//Read data in from file
				while(in.ready())
				{
					line = in.readLine();

					//ignore UTF-8 byte-order mark on first line if it is present
					if(firstLine && inputEncoding.equals("UTF-8") && line.indexOf("\ufeff") == 0)
					{
						line = line.substring(1, line.length());
					}
					
					//ignore comments. Comments specified by # as first character (when not already processing a multi-line property)
					if(!multiLine && line.indexOf("#") == 0)
					{
						continue;
					}
					
					if(multiLine)
					{
						if(line.indexOf("\"\"\"") >= 0)
						{
							//if line contains """, truncate before """, add that to the current multiLineKey's entry, and terminate the multiLine
							int quotes = line.indexOf("\"\"\"");
							line = line.substring(0, quotes);
							put(multiLineKey, get(multiLineKey) + line);
							multiLine = false;
							multiLineKey = "";
						}
						else
						{
							//add the current line to the current mutliLineKey's entry, and continue
							put(multiLineKey, get(multiLineKey) + line + "\n");
						}
					}
					else
					{
						if(line.trim().equals(""))
						{
							continue;
						}
						int equal = line.indexOf("=");
						if(equal < 0)
						{
							//no equal sign. treat it as a key with a value of ""
							put(line.trim(), "");
						}
						else
						{
							String key = line.substring(0, equal);
							String value = line.substring(equal+1, line.length());
							if(value.matches("^\\s*\"\"\".*"))
							{
								//start of multi-line string
								int quotes = value.indexOf("\"\"\"");
								value = value.substring(quotes+3, value.length());
								quotes = value.indexOf("\"\"\"");
								if(quotes >= 0)
								{
									//multi-line string ends on same line
									value = value.substring(0, quotes);
									put(key, value);
								}
								else
								{
									//multi-line string must end on some other line
									multiLine = true;
									multiLineKey = key;
									put(key, value + "\n");
								}
							}
							else
							{
								//single-line property
								put(key, value);
							}
						}
					}
					
					firstLine = false;
				}
				in.close();
				isr.close();
				fis.close();
			}
			catch(Exception e){System.out.println(e.toString());}
		}
	}
}
