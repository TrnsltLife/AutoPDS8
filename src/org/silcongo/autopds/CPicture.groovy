package org.silcongo.autopds

import org.silcongo.autopds.ctemplate.*
import org.silcongo.autopds.util.*

class CPicture
{
	def extensionPath = "" //e.g.: C:\Users\Jeremy\Documents\CyberLink\PowerDirector\8.0\MyPinPs\PinP_000\
	def pictures = []

	public Object leftShift(it)
	{
		if(it instanceof MetaPicture)
		{
			pictures << it
		}
		else
		{
			return pictures[-1] << it
		}
		return it
	}




	public String toEscapedString()
	{
		def output = this.toString()
		output = output.replaceAll(/\r|\n/, "")
		output = output.replaceAll(/<\?xml version="1\.0" encoding="UTF-16"\?>/, """<?xml version="1.0" encoding="UTF-16"?>\r\n""")
		output = output.replaceAll(/&/, "&amp;")
		output = output.replaceAll(/</, "&lt;")
		output = output.replaceAll(/>/, "&gt;")
		output = output.replaceAll(/"/, "&quot;")
		return output
	}


	public String toString()
	{
		def output = new StringBuilder()
		output << """<?xml version="1.0" encoding="UTF-16"?>
<CPicture Version="1.0">
<Header>
<Information Creator="CPicture Exported XML" CreateDate="2009/8/31" LastModified="2009/8/31"/>"""

		if(extensionPath)
		{
			output << """<Extension ReferencePath="${new File(extensionPath).getCanonicalPath()}\\"/>"""
		}

		output << """
</Header>
<PictureList>"""

		pictures.each{output << it.toString()}
		
		output << """</PictureList>
</CPicture>"""

		return output.toString()
	}


}