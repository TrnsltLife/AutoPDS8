package org.silcongo.autopds.util

class XMLUtils
{
	static String escape(text)
	{
		text = text.replaceAll(/&/, "&amp;")
		text = text.replaceAll(/</, "&lt;")
		text = text.replaceAll(/>/, "&gt;")
		text = text.replaceAll(/"/, "&quot;")
		return text
	}
	
	static String unescape(text)
	{
		text = text.replaceAll(/&quot;/, '"')
		text = text.replaceAll(/&lt;/, "<")
		text = text.replaceAll(/&gt;/, ">")
		text = text.replaceAll(/&amp;/, "&")
		return text
	}
}