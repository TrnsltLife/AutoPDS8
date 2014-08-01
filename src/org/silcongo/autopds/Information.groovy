package org.silcongo.autopds

import org.silcongo.autopds.util.*

class Information
{
//<INFORMATION PROJECT="" AUTHOR="Jeremy" DESCRIPTION="" CREATEDATE="2009/ 8/ 4 12:30: 3" CLASS="" COMPANY="Brown" KEYWORD="" ASPECTRATIO="FALSE" CLVSMODE="2" AUTHORINGDATA="" PACKPROJECT="FALSE"/>


	def project = ""
	def author = ""
	def description = ""
	def createDate = DateUtils.now("yyyy/MM/dd HH:mm:ss") //   2009/ 8/ 4 12:30: 3
	def klass = ""
	def company = ""
	def keyword = ""
	def aspectRatio = false //true is 16:9, false is 4:3
	def clvsMode = 2
	def authoringData = ""
	def packProject = false
	
	public String toString()
	{
		def output = new StringBuilder()
		use(DataFormat)
		{
			output << """<INFORMATION PROJECT="$project" AUTHOR="$author" DESCRIPTION="$description" CREATEDATE="$createDate" CLASS="$klass" COMPANY="$company" KEYWORD="$keyword" ASPECTRATIO="${aspectRatio.B()}" CLVSMODE="$clvsMode" AUTHORINGDATA="$authoringData" PACKPROJECT="${packProject.B()}"/>"""
		}
		return output.toString()
	}
}