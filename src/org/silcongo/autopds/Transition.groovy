package org.silcongo.autopds

import org.silcongo.autopds.util.*

class Transition
{

/*
<TRANSITION ID="0" GUID="CES_PlugIn.dll\\DSP_TR_Fade" START="80080080" STOP="100100100">
<PARAM NAME="" VALUETYPE="FLOATL" VALUE="">
<LINEAR TIME="0" VALUE="0.000000"/>
<LINEAR TIME="20020020" VALUE="1.000000"/>
</PARAM>
</TRANSITION>
*/

	def id
	def transitionType
	def start = 0
	def stop = 0
	def duration = 0
	def fromClip = null
	def params = []
	
	public Transition(){}
	
	public Transition(String transType)
	{
		setTransitionType(transType)
		duration = Timecode.seconds(2)
		recalculateTimes()
	}

	public Transition(String transType, Number dur)
	{
		this(-1, transType, null, dur)
	}
	
	public Transition(String transType, Clip from)
	{
		this(-1, transType, from, Timecode.seconds(2))
	}
	
	public Transition(String transType, Clip from, Number dur)
	{
		this(-1, transType, from, dur)
	}
	
	public Transition(uid, transType, from, dur)
	{
		id = uid
		setTransitionType(transType)
		fromClip = from
		duration = dur
		
		recalculateTimes()
	}
	
	public void setTransitionType(transType)
	{
		if(transType == "Random")
		{
			Random r = new Random()
			def index = r.nextInt(transitionNames.size())
			def keys = transitionNames.keySet().sort()
			def key = keys[index]
			transitionType = transitionNames[key]
		}
		else if(transitionNames[transType])
		{
			transitionType = transitionNames[transType]
		}
		else
		{
			transitionType = transType
		}
	}
	
	public void setFromClip(clip)
	{
		fromClip = clip
		recalculateTimes()
	}
	
	public void recalculateTimes()
	{
		if(fromClip)
		{
			start = fromClip.stop - duration
			stop = fromClip.stop
		}
		else
		{
			start = 0
			stop = start + duration
		}
		//println("Transition.recalculateTimes() => start:" + start + " stop:" + stop)
	}
	
	public boolean stretchClip(toClip)
	{
		def toObject = toClip.mediaObject
		if(toObject instanceof ImageObject || toObject instanceof ColorObject)
		{
			toClip.start -= this.duration
			toClip.mediaDuration += this.duration
			if(toClip.start < 0)
			{
				def dif = 0 - toClip.start
				toClip.start += dif
				toClip.mediaDuration -= dif
			}
			toClip.recalculateStopTimes()
			return true
		}
		return false
	}
	
	public void moveClip(toClip)
	{
		def toObject = toClip.mediaObject
		toClip.start -= this.duration
		if(toClip.start < 0){toClip.start = 0}
		toClip.recalculateStopTimes()
	}
	
	public void stretchOrMove(toClip)
	{
		if(!stretchClip(toClip))
		{
			moveClip(toClip)
		}
	}
	
	public void createParamList()
	{
		params = []
		
		params << new Param("", "FLOATL", "", """<LINEAR TIME="0" VALUE="0.000000"/>
<LINEAR TIME="${duration}" VALUE="1.000000"/>""")
	}
	
	public String toString()
	{
		createParamList()
		
		def output = new StringBuilder()
		output << """<TRANSITION ID="${id}" GUID="${transitionType}" START="${start}" STOP="${stop}">"""
		params.each{output << it.toString()}
		output << "</TRANSITION>"

		return output.toString()
	}
	
	
	
	
	
	public static transitionNames = [Airplane:"CES_PlugIn_5.dll\\Ces_DSP_TR_Airplane",
		Blizzard:"CES_PlugIn.dll\\DSP_TR_BlizzardS",
		Blur:"CES_PlugIn_5.dll\\Ces_DSP_TR_Blur",
		BlurDown:"CES_PlugIn.dll\\DSP_TR_BlurDown",
		BlurLeft:"CES_PlugIn.dll\\DSP_TR_BlurLeft",
		BlurRight:"CES_PlugIn.dll\\DSP_TR_BlurRight",
		BlurUp:"CES_PlugIn.dll\\DSP_TR_BlurUp",
		BoxIn:"CES_PlugIn.dll\\DSP_TR_BoxIn",
		BoxOut:"CES_PlugIn.dll\\DSP_TR_BoxOut",
		BrokenGlass:"CES_PlugIn.dll\\DSP_TR_BrokenGlass",
		Burning:"CES_PlugIn.dll\\DSP_TR_Burn",
		CenterCurl:"CES_PlugIn.dll\\DSP_TR_CenterCurl",
		CenterRoll:"CES_PlugIn.dll\\DSP_TR_CenterRoll",
		ChainReactionLeft:"CES_PlugIn.dll\\DSP_TR_3DChainReactLeft",
		ChainReactionRight:"CES_PlugIn.dll\\DSP_TR_3DChainReactRight",
		CollapseI:"CES_PlugIn.dll\\DSP_TR_Collapse1",
		CollapseII:"CES_PlugIn.dll\\DSP_TR_Collapse2",
		CollapseIII:"CES_PlugIn.dll\\DSP_TR_Collapse3",
		CollapsIV:"CES_PlugIn.dll\\DSP_TR_Collapse4",
		ColorCircle:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorCircle",
		ColorLoveHeart:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorLoveheart",
		ColorSplit:"CES_PlugIn.dll\\DSP_TR_ColorSplit",
		ColorStar:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorStar",
		Cross:"CES_PlugIn.dll\\DSP_TR_Cross",
		DissolveCoarse:"CES_PlugIn.dll\\DSP_TR_Dissolve1",
		DissolveFine:"CES_PlugIn.dll\\DSP_TR_Dissolve2",
		DissolveMid:"CES_PlugIn.dll\\DSP_TR_Dissolve3",
		DominoesLeft:"CES_PlugIn.dll\\DSP_TR_3DDominotesLeft",
		DominoesRight:"CES_PlugIn.dll\\DSP_TR_3DDominotesRight",
		Drain:"CES_PlugIn.dll\\DSP_TR_Drain",
		Fade:"CES_PlugIn.dll\\DSP_TR_Fade",
		FilmClipI:"CES_PlugIn.dll\\DSP_TR_FilmScroll1",
		FilmClipII:"CES_PlugIn.dll\\DSP_TR_FilmScroll2",
		FlipDown:"CES_PlugIn.dll\\DSP_TR_3DFlipDown",
		FlipLeft:"CES_PlugIn.dll\\DSP_TR_3DFlipLeft",
		FlipRight:"CES_PlugIn.dll\\DSP_TR_3DFlipRight",
		FlipUp:"CES_PlugIn.dll\\DSP_TR_3DFlipUp",
		Flocking:"CES_PlugIn_4.dll\\DSP_TR_Flocking",
		FoldI:"CES_PlugIn.dll\\DSP_TR_3DFold1",
		FoldII:"CES_PlugIn.dll\\DSP_TR_3DFold2",
		FragmentsClockwise:"CES_PlugIn.dll\\DSP_TR_3DFragmentsCW",
		FragmentsCounterclockwise:"CES_PlugIn.dll\\DSP_TR_3DFragmentsCCW",
		FragmentsHorizontal:"CES_PlugIn.dll\\DSP_TR_3DFragmentsH",
		FullFlip:"CES_PlugIn.dll\\DSP_TR_3DFullFlip",
		FullTwist:"CES_PlugIn.dll\\DSP_TR_FullTwistS",
		Glare:"CES_PlugIn.dll\\DSP_TR_Glare",
		Glass1:"Ces_Dsp_Tr_3D_Glass_1",
		Glass2:"Ces_Dsp_Tr_3D_Glass_2",
		Glass3:"Ces_Dsp_Tr_3D_Glass_3",
		Glow:"CES_PlugIn_5.dll\\Ces_DSP_TR_Glow",
		GrandOpening:"CES_PlugIn.dll\\DSP_TR_Door",
		Granulate:"CES_PlugIn.dll\\DSP_TR_GranulateS",
		GridCircle:"CES_PlugIn_2.dll\\DSP_TR_GridCircle",
		GridDiagonal:"CES_PlugIn_2.dll\\DSP_TR_GridDiagonal",
		GridFalling:"CES_PlugIn_2.dll\\DSP_TR_GridFalling",
		GridRandom:"CES_PlugIn_2.dll\\DSP_TR_GridRandom",
		GridSequential:"CES_PlugIn_2.dll\\DSP_TR_GridSequential",
		GridSliding:"CES_PlugIn_2.dll\\DSP_TR_GridSliding",
		Handspring:"CES_PlugIn.dll\\DSP_TR_3DHandspring",
		HeadsOrTails:"CES_PlugIn.dll\\DSP_TR_3DHeadTail",
		Interfere:"CES_PlugIn.dll\\DSP_TR_Interfere",
		Laser:"CES_PlugIn.dll\\DSP_TR_Laser",
		MagicBlocks:"CES_PlugIn.dll\\DSP_TR_3DMagicBlocksS",
		Mirror:"CES_PlugIn.dll\\DSP_TR_Mirror",
		Mosaic:"CES_PlugIn_5.dll\\Ces_DSP_TR_Mosaic",
		NoEffect:"CES_PlugIn.dll\\DSP_TR_NoEffect",
		PageCurl:"CES_PlugIn.dll\\DSP_TR_PageCurl",
		PageFold:"CES_PlugIn.dll\\DSP_TR_PageFold",
		PageRoll:"CES_PlugIn.dll\\DSP_TR_PageRoll",
		Painting:"CES_PlugIn_7.dll\\Ces_Dsp_Tr_Painting2",
		Paper1:"Ces_Dsp_Tr_3D_Paper_1",
		Paper2:"Ces_Dsp_Tr_3D_Paper_2",
		PaperAirplane:"Ces_Dsp_Tr_3D_PaperAirplane_2",
		Pinwheel:"CES_PlugIn.dll\\DSP_TR_PinWheel1",
		PinwheelCurved:"CES_PlugIn.dll\\DSP_TR_PinWheel2",
		PinwheelCurvedSoft:"CES_PlugIn.dll\\DSP_TR_PinWheel3",
		PIP:"CES_PlugIn.dll\\DSP_PI_Script",
		RainDrops:"CES_PlugIn.dll\\DSP_TR_RainDropS",
		RippleI:"CES_PlugIn.dll\\DSP_TR_Ripple1",
		RippleII:"CES_PlugIn.dll\\DSP_TR_Ripple2",
		RippleIII:"CES_PlugIn.dll\\DSP_TR_Ripple3",
		RotateCW:"CES_PlugIn_4.dll\\DSP_TR_RotateCW",
		SeismI:"CES_PlugIn.dll\\DSP_TR_3DSeism1",
		SeismII:"CES_PlugIn.dll\\DSP_TR_3DSeism2",
		ShoveDown:"CES_PlugIn.dll\\DSP_TR_ShoveDown",
		ShoveLeft:"CES_PlugIn.dll\\DSP_TR_ShoveLeft",
		ShoveRight:"CES_PlugIn.dll\\DSP_TR_ShoveRight",
		ShoveUp:"CES_PlugIn.dll\\DSP_TR_ShoveUp",
		ShutterDown:"CES_PlugIn.dll\\DSP_TR_ShutterDown",
		ShutterLeft:"CES_PlugIn.dll\\DSP_TR_ShutterLeft",
		ShutterRight:"CES_PlugIn.dll\\DSP_TR_ShutterRight",
		ShutterUp:"CES_PlugIn.dll\\DSP_TR_ShutterUp",
		SlicesHorizontalI:"CES_PlugIn.dll\\DSP_TR_SlicesH1",
		SlicesHorizontalII:"CES_PlugIn.dll\\DSP_TR_SlicesH2",
		SlicesVerticalI:"CES_PlugIn.dll\\DSP_TR_SlicesV1",
		SlicesVerticalII:"CES_PlugIn.dll\\DSP_TR_SlicesV2",
		SlideDown:"CES_PlugIn.dll\\DSP_TR_SlideDown",
		SlideLeft:"CES_PlugIn.dll\\DSP_TR_SlideLeft",
		SlideRight:"CES_PlugIn.dll\\DSP_TR_SlideRight",
		SlideSpinHorizontalI:"CES_PlugIn.dll\\DSP_TR_SlideSpin1",
		SlideSpinHorizontalII:"CES_PlugIn.dll\\DSP_TR_SlideSpin2",
		SlideUp:"CES_PlugIn.dll\\DSP_TR_SlideUp",
		Sparkle:"CES_PlugIn_5.dll\\Ces_DSP_TR_Sparkle",
		SpinHorizontalLeft:"CES_PlugIn.dll\\DSP_TR_SpinLeft",
		SpinHorizontalRight:"CES_PlugIn.dll\\DSP_TR_SpinRight",
		Spiral:"CES_PlugIn_4.dll\\DSP_TR_Spiral",
		SplicesHorizontal:"CES_PlugIn.dll\\DSP_TR_SplicesH",
		SplicesVertical:"CES_PlugIn.dll\\DSP_TR_SplicesV",
		Star:"CES_PlugIn_3.dll\\DSP_TR_AlphaTransition",
		SwapDown:"CES_PlugIn.dll\\DSP_TR_SwapDown",
		SwapLeft:"CES_PlugIn.dll\\DSP_TR_SwapLeft",
		SwapRight:"CES_PlugIn.dll\\DSP_TR_SwapRight",
		SwapUp:"CES_PlugIn.dll\\DSP_TR_SwapUp",
		SweetHeart:"CES_PlugIn_3.dll\\DSP_TR_TestAlpha",
		SwingDown:"CES_PlugIn.dll\\DSP_TR_SwingDown",
		SwingLeft:"CES_PlugIn.dll\\DSP_TR_SwingLeft",
		SwingRight:"CES_PlugIn.dll\\DSP_TR_SwingRight",
		SwingUp:"CES_PlugIn.dll\\DSP_TR_SwingUp",
		Swinging:"CES_PlugIn.dll\\DSP_TR_Swinging",
		ThresholdI:"CES_PlugIn.dll\\DSP_TR_Threshold1",
		ThresholdII:"CES_PlugIn.dll\\DSP_TR_Threshold2",
		TwistHorizontal:"CES_PlugIn.dll\\DSP_TR_HTwistS",
		TwistVertical:"CES_PlugIn.dll\\DSP_TR_VTwistS",
		TyphoonDown:"CES_PlugIn.dll\\DSP_TR_3DTyphoonDown",
		TyphoonLeft:"CES_PlugIn.dll\\DSP_TR_3DTyphoonLeft",
		TyphoonRight:"CES_PlugIn.dll\\DSP_TR_3DTyphoonRight",
		TyphoonUp:"CES_PlugIn.dll\\DSP_TR_3DTyphoonUp",
		Wave:"CES_PlugIn.dll\\DSP_TR_Wave",
		WaveWipeDown:"CES_PlugIn.dll\\DSP_TR_WaveWipeDown",
		WaveWipeLeft:"CES_PlugIn.dll\\DSP_TR_WaveWipeLeft",
		WaveWipeRight:"CES_PlugIn.dll\\DSP_TR_WaveWipeRight",
		WaveWipeUp:"CES_PlugIn.dll\\DSP_TR_WaveWipeUp",
		WindshieldLeft:"CES_PlugIn_4.dll\\DSP_TR_WindshieldLeft",
		WipeCenter:"CES_PlugIn.dll\\DSP_TR_WipeCenter",
		WipeClockwise:"CES_PlugIn.dll\\DSP_TR_WipeCW",
		WipeCounterclockwise:"CES_PlugIn.dll\\DSP_TR_WipeCCW",
		WipeDown:"CES_PlugIn.dll\\DSP_TR_WipeDown",
		WipeDownDirty:"CES_PlugIn.dll\\DSP_TR_DirtyWipeDown",
		WipeDownSoft:"CES_PlugIn.dll\\DSP_TR_WipeSoftDown",
		WipeLeft:"CES_PlugIn.dll\\DSP_TR_WipeLeft",
		WipeLeftDirty:"CES_PlugIn.dll\\DSP_TR_DirtyWipeLeft",
		WipeLeftSoft:"CES_PlugIn.dll\\DSP_TR_WipeSoftLeft",
		WipeRight:"CES_PlugIn.dll\\DSP_TR_WipeRight",
		WipeRightDirty:"CES_PlugIn.dll\\DSP_TR_DirtyWipeRight",
		WipeRightSoft:"CES_PlugIn.dll\\DSP_TR_WipeSoftRight",
		WipeUp:"CES_PlugIn.dll\\DSP_TR_WipeUp",
		WipeUpDirty:"CES_PlugIn.dll\\DSP_TR_DirtyWipeUp",
		WipeUpSoft:"CES_PlugIn.dll\\DSP_TR_WipeSoftUp",
		WormHole:"CES_PlugIn.dll\\DSP_TR_WormHole",
		XRay:"CES_PlugIn.dll\\DSP_TR_XRay"]
}