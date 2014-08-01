package org.silcongo.autopds

class Project
{
	Information information = new Information()
	Library library = new Library()
	Title title = new Title(library)

	//def version = "0601"
	def version = "0700"
	def aeffVersion = "8.00.1903"
	def projectType = "0"
	
	public String toString()
	{
		def output = """<PROJECT VERSION="$version" AEFF_VERSION="$aeffVersion" PROJECT_TYPE="$projectType">"""
		output += information.toString()
		output += library.toString()
		output += title.toString()
		output += "</PROJECT>"
		output = output.replaceAll(/>/, ">\n")
		output = output.replaceAll(/\n\n/, "\n")
		return output
	}
}