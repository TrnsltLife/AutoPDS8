package org.silcongo.autopds

class Title
{
	Library library
	Timeline timeline
	Chapter chapter = new Chapter()
	Playlist playlist = new Playlist()
	
	public Title(lib)
	{
		library = lib
		timeline = new Timeline(library)
	}

	public String toString()
	{
		return "<TITLE>" + timeline.toString() + chapter.toString() + "<PLAYLIST/>" + "</TITLE>"
	}
}