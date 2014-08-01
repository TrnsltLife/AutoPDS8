package org.silcongo.autopds

import org.silcongo.autopds.util.*

class PanZoomKeyframe
{
	public static shortcuts = [
		SCALE1:1.0,
		SCALE2:0.5,
		SCALE3:0.33,
		SCALE4:0.25,
		SCALE5:0.2,

		CENTER:[0.5, 0.5],

		CENTER1:[0.5, 0.5], 

		CENTER2:[0.5, 0.5],
		A2:[0.25, 0.25], B2:[0.75, 0.25],
		C2:[0.25, 0.75], D2:[0.75, 0.75],

		CENTER3:[0.5, 0.5],
		A3:[0.166666, 0.166666], B3:[0.5, 0.166666], C3:[0.833333, 0.166666],
		D3:[0.166666, 0.5], E3:[0.5, 0.5], F3:[0.833333, 0.5],
		G3:[0.166666, 0.833333], H3:[0.5, 0.833333], I3:[0.833333, 0.833333],
		
		CENTER4:[0.5, 0.5],
		A4:[0.125, 0.125], B4:[0.375, 0.125], C4:[0.625, 0.125], D4:[0.875, 0.125],
		E4:[0.125, 0.375], F4:[0.375, 0.375], G4:[0.625, 0.375], H4:[0.875, 0.375],
		I4:[0.125, 0.625], J4:[0.375, 0.625], K4:[0.625, 0.625], L4:[0.875, 0.625],
		M4:[0.125, 0.875], N4:[0.375, 0.875], O4:[0.625, 0.875], P4:[0.875, 0.875],

		CENTER5:[0.5, 0.5],
		A5:[0.1, 0.1], B5:[0.3, 0.1], C5:[0.5, 0.1], D5:[0.7, 0.1], E5:[0.9, 0.1],
		F5:[0.1, 0.3], G5:[0.3, 0.3], H5:[0.5, 0.3], I5:[0.7, 0.3], J5:[0.9, 0.3],
		K5:[0.1, 0.5], L5:[0.3, 0.5], M5:[0.5, 0.5], N5:[0.7, 0.5], O5:[0.9, 0.5],
		P5:[0.1, 0.7], Q5:[0.3, 0.7], R5:[0.5, 0.7], S5:[0.7, 0.7], T5:[0.9, 0.7],
		U5:[0.1, 0.9], V5:[0.3, 0.9], W5:[0.5, 0.9], X5:[0.7, 0.9], Y5:[0.9, 0.9]
	]
	

	def rotation = 0.0
	def x = 0.5
	def y = 0.5
	def scaleX = 1.0
	def scaleY = 1.0
	def time = 0.0
	def unknown1 = "01000000"
	def unknown2 = 0.0
	def unknown3 = 0.0
	
	public PanZoomKeyframe(lx, ly, sx, sy, t)
	{
		x = lx
		y = ly
		scaleX = sx
		scaleY = sy
		time = t
	}
	
	public PanZoomKeyframe(lx, ly, sx, sy, t, r)
	{
		x = lx
		y = ly
		scaleX = sx
		scaleY = sy
		time = t
		rotation = r
	}
	
	public PanZoomKeyframe(shortcut, t)
	{	
		this(shortcut, t, 0.0)
	}
	
	public PanZoomKeyframe(shortcut, t, r)
	{	
		if(shortcut =~ /CENTER\./)
		{
			scaleX = Float.parseFloat(shortcut - "CENTER")
			scaleY = scaleX

			x = shortcuts.CENTER[0]
            y = shortcuts.CENTER[1]
		}
		else
		{
			def zoom = "SCALE" + shortcut.replaceAll(/[A-Za-z]*/, "")
			scaleX = shortcuts[zoom]
			scaleY = scaleX
	
			x = shortcuts[shortcut][0]
			y = shortcuts[shortcut][1]
		}
		time = t
		rotation = r
	}
	
	public String toString()
	{
		def output = new StringBuilder()
		
		output << HexUtils.encodeFloat(rotation) +
			HexUtils.encodeFloat(x) + HexUtils.encodeFloat(y) +
			HexUtils.encodeFloat(scaleX) + HexUtils.encodeFloat(scaleY) +
			HexUtils.encodeFloat(time) +
			//HexUtils.encodeFloat(unknown1) +
			unknown1 +
			HexUtils.encodeFloat(unknown2) +
			HexUtils.encodeFloat(unknown3)
		
		return output.toString()
	}
}