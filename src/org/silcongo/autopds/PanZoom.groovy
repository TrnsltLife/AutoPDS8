package org.silcongo.autopds

class PanZoom
{

	
	/* Styles:
	Random = 0
	Pan and Zoom = 1
	Vertical Up = 2
	Vertical Down = 3
	Horizontal Right = 4
	Horizontal Left = 5
	Zoom In = 6
	Zoom Out = 7
	Left Down to Right Up = 8
	Right Up to Left Down = 9
	Right Down to Left Up = 10
	Left Up to Right Down = 11
	Rotate and Zoom In, Clockwise = 12
	Rotate and Zoom In, Counter Clockwise = 13
	Rotate and Zoom Out, Clockwise = 14
	Rotate and Zoom Out, Counter Clockwise = 15
	U Type Curve Path, Left to Right = 16
	U Type Curve Path, Right to Left = 17
	The Inverse of U Type = 18
	The Inverse of U Type, Left to Right = 19
	Zoom In and Zoom Out = 20
	User Defined = 21
	*/

	def style = 21
	def keyframes = []

	public PanZoom()
	{
	}
	
	public PanZoom(startShortcut, endShortcut)
	{
		//startShortcut and endShortcut should be values from PanZoomKeyframe.shortcuts, e.g. A2, P5, etc.
		add(startShortcut, 0.0)
		add(endShortcut, 1.0)
	}
	
	public void leftShift(keyframe)
	{
		keyframes << keyframe
	}
	
	public void add(keyframe)
	{
		keyframes << keyframe
	}
	
	public void add(shortcut, time)
	{
		keyframes << new PanZoomKeyframe(shortcut, time)
	}
	
	public int size()
	{
		if(keyframes.size() == 0){return 0}
		
		return ((keyframes.size()*9) + 1) * 4
	}
	
	public String getStyle()
	{
		if(keyframes.size() == 0){return "0"}
		
		return style
	}
	
	public String getApply()
	{
		if(keyframes.size() == 0){return "FALSE"}
		
		return "TRUE"
	}
	
	public String toString()
	{
		if(keyframes.size() == 0){return ""}
	
		def output = new StringBuilder()
		output << "02000000"
		keyframes.each{output << it.toString()}
		return output.toString()
	}
}