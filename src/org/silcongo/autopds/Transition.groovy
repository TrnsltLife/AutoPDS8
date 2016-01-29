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
	def transitionData
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
			def index = r.nextInt(transitionDataMap.size())
			def keys = transitionDataMap.keySet().sort()
			def key = keys[index]
			transitionType = transitionDataMap[key]
		}
		else if(transitionDataMap[transType])
		{
			transitionData = transitionDataMap[transType]
			transitionType = transitionData.guid
			if(transitionData.params)
			{
				
			}
		}
		else
		{
			transitionType = transType
			transitionData = [guid:transType]
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
		
		for(param in transitionData.params)
		{
			params << new Param("", "${param.type}", "", """<LINEAR TIME="0" VALUE="${param.value}"/>
<LINEAR TIME="${duration}" VALUE="${param.value}"/>""")
		}
	}
	
	public String toString()
	{
		createParamList()
		
		def output = new StringBuilder()
		output << """<TRANSITION ID="${id}" GUID="${transitionType}" START="${start}" STOP="${stop}" TRANSITIONNAME="POSTTRANSITION" ADDSTILL="0">"""
		params.each{output << it.toString()}
		output << "</TRANSITION>"

		return output.toString()
	}
	
	
	
	
	
	public static transitionDataMap = [Airplane:[guid:"CES_PlugIn_5.dll\\Ces_DSP_TR_Airplane"],
		Blizzard:[guid:"CES_PlugIn.dll\\DSP_TR_BlizzardS"],
		Blur:[guid:"CES_PlugIn_5.dll\\Ces_DSP_TR_Blur"],
		BlurDown:[guid:"CES_PlugIn.dll\\DSP_TR_BlurDown"],
		BlurLeft:[guid:"CES_PlugIn.dll\\DSP_TR_BlurLeft"],
		BlurRight:[guid:"CES_PlugIn.dll\\DSP_TR_BlurRight"],
		BlurUp:[guid:"CES_PlugIn.dll\\DSP_TR_BlurUp"],
		BoxIn:[guid:"CES_PlugIn.dll\\DSP_TR_BoxIn"],
		BoxOut:[guid:"CES_PlugIn.dll\\DSP_TR_BoxOut"],
		BrokenGlass:[guid:"CES_PlugIn.dll\\DSP_TR_BrokenGlass"],
		Burning:[guid:"CES_PlugIn.dll\\DSP_TR_Burn"],
		CenterCurl:[guid:"CES_PlugIn.dll\\DSP_TR_CenterCurl"],
		CenterRoll:[guid:"CES_PlugIn.dll\\DSP_TR_CenterRoll"],
		ChainReactionLeft:[guid:"CES_PlugIn.dll\\DSP_TR_3DChainReactLeft"],
		ChainReactionRight:[guid:"CES_PlugIn.dll\\DSP_TR_3DChainReactRight"],
		CollapseI:[guid:"CES_PlugIn.dll\\DSP_TR_Collapse1"],
		CollapseII:[guid:"CES_PlugIn.dll\\DSP_TR_Collapse2"],
		CollapseIII:[guid:"CES_PlugIn.dll\\DSP_TR_Collapse3"],
		CollapsIV:[guid:"CES_PlugIn.dll\\DSP_TR_Collapse4"],
		ColorCircle:[guid:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorCircle"],
		ColorLoveHeart:[guid:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorLoveheart"],
		ColorSplit:[guid:"CES_PlugIn.dll\\DSP_TR_ColorSplit"],
		ColorStar:[guid:"CES_PlugIn_5.dll\\Ces_Dsp_Tr_ColorStar"],
		Cross:[guid:"CES_PlugIn.dll\\DSP_TR_Cross"],
		DissolveCoarse:[guid:"CES_PlugIn.dll\\DSP_TR_Dissolve1"],
		DissolveFine:[guid:"CES_PlugIn.dll\\DSP_TR_Dissolve2"],
		DissolveMid:[guid:"CES_PlugIn.dll\\DSP_TR_Dissolve3"],
		DominoesLeft:[guid:"CES_PlugIn.dll\\DSP_TR_3DDominotesLeft"],
		DominoesRight:[guid:"CES_PlugIn.dll\\DSP_TR_3DDominotesRight"],
		Drain:[guid:"CES_PlugIn.dll\\DSP_TR_Drain"],
		Fade:[guid:"CES_PlugIn.dll\\DSP_TR_Fade"],
		FilmClipI:[guid:"CES_PlugIn.dll\\DSP_TR_FilmScroll1"],
		FilmClipII:[guid:"CES_PlugIn.dll\\DSP_TR_FilmScroll2"],
		FlipDown:[guid:"CES_PlugIn.dll\\DSP_TR_3DFlipDown"],
		FlipLeft:[guid:"CES_PlugIn.dll\\DSP_TR_3DFlipLeft"],
		FlipRight:[guid:"CES_PlugIn.dll\\DSP_TR_3DFlipRight"],
		FlipUp:[guid:"CES_PlugIn.dll\\DSP_TR_3DFlipUp"],
		Flocking:[guid:"CES_PlugIn_4.dll\\DSP_TR_Flocking"],
		FoldI:[guid:"CES_PlugIn.dll\\DSP_TR_3DFold1"],
		FoldII:[guid:"CES_PlugIn.dll\\DSP_TR_3DFold2"],
		FragmentsClockwise:[guid:"CES_PlugIn.dll\\DSP_TR_3DFragmentsCW"],
		FragmentsCounterclockwise:[guid:"CES_PlugIn.dll\\DSP_TR_3DFragmentsCCW"],
		FragmentsHorizontal:[guid:"CES_PlugIn.dll\\DSP_TR_3DFragmentsH"],
		FullFlip:[guid:"CES_PlugIn.dll\\DSP_TR_3DFullFlip"],
		FullTwist:[guid:"CES_PlugIn.dll\\DSP_TR_FullTwistS"],
		Glare:[guid:"CES_PlugIn.dll\\DSP_TR_Glare"],
		Glass1:[guid:"Ces_Dsp_Tr_3D_Glass_1"],
		Glass2:[guid:"Ces_Dsp_Tr_3D_Glass_2"],
		Glass3:[guid:"Ces_Dsp_Tr_3D_Glass_3"],
		Glow:[guid:"CES_PlugIn_5.dll\\Ces_DSP_TR_Glow"],
		GrandOpening:[guid:"CES_PlugIn.dll\\DSP_TR_Door"],
		Granulate:[guid:"CES_PlugIn.dll\\DSP_TR_GranulateS"],
		GridCircle:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridCircle"],
		GridDiagonal:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridDiagonal"],
		GridFalling:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridFalling"],
		GridRandom:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridRandom"],
		GridSequential:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridSequential"],
		GridSliding:[guid:"CES_PlugIn_2.dll\\DSP_TR_GridSliding"],
		Handspring:[guid:"CES_PlugIn.dll\\DSP_TR_3DHandspring"],
		HeadsOrTails:[guid:"CES_PlugIn.dll\\DSP_TR_3DHeadTail"],
		Interfere:[guid:"CES_PlugIn.dll\\DSP_TR_Interfere"],
		Laser:[guid:"CES_PlugIn.dll\\DSP_TR_Laser"],
		MagicBlocks:[guid:"CES_PlugIn.dll\\DSP_TR_3DMagicBlocksS"],
		Mirror:[guid:"CES_PlugIn.dll\\DSP_TR_Mirror"],
		Mosaic:[guid:"CES_PlugIn_5.dll\\Ces_DSP_TR_Mosaic"],
		NoEffect:[guid:"CES_PlugIn.dll\\DSP_TR_NoEffect"],
		PageCurl:[guid:"CES_PlugIn.dll\\DSP_TR_PageCurl"],
		PageFold:[guid:"CES_PlugIn.dll\\DSP_TR_PageFold"],
		PageRoll:[guid:"CES_PlugIn.dll\\DSP_TR_PageRoll"],
		Painting:[guid:"CES_PlugIn_7.dll\\Ces_Dsp_Tr_Painting2"],
		Paper1:[guid:"Ces_Dsp_Tr_3D_Paper_1"],
		Paper2:[guid:"Ces_Dsp_Tr_3D_Paper_2"],
		PaperAirplane:[guid:"Ces_Dsp_Tr_3D_PaperAirplane_2"],
		Pinwheel:[guid:"CES_PlugIn.dll\\DSP_TR_PinWheel1"],
		PinwheelCurved:[guid:"CES_PlugIn.dll\\DSP_TR_PinWheel2"],
		PinwheelCurvedSoft:[guid:"CES_PlugIn.dll\\DSP_TR_PinWheel3"],
		PIP:[guid:"CES_PlugIn.dll\\DSP_PI_Script"],
		RainDrops:[guid:"CES_PlugIn.dll\\DSP_TR_RainDropS"],
		RippleI:[guid:"CES_PlugIn.dll\\DSP_TR_Ripple1"],
		RippleII:[guid:"CES_PlugIn.dll\\DSP_TR_Ripple2"],
		RippleIII:[guid:"CES_PlugIn.dll\\DSP_TR_Ripple3"],
		RotateCW:[guid:"CES_PlugIn_4.dll\\DSP_TR_RotateCW"],
		SeismI:[guid:"CES_PlugIn.dll\\DSP_TR_3DSeism1"],
		SeismII:[guid:"CES_PlugIn.dll\\DSP_TR_3DSeism2"],
		ShoveDown:[guid:"CES_PlugIn.dll\\DSP_TR_ShoveDown"],
		ShoveLeft:[guid:"CES_PlugIn.dll\\DSP_TR_ShoveLeft"],
		ShoveRight:[guid:"CES_PlugIn.dll\\DSP_TR_ShoveRight"],
		ShoveUp:[guid:"CES_PlugIn.dll\\DSP_TR_ShoveUp"],
		ShutterDown:[guid:"CES_PlugIn.dll\\DSP_TR_ShutterDown"],
		ShutterLeft:[guid:"CES_PlugIn.dll\\DSP_TR_ShutterLeft"],
		ShutterRight:[guid:"CES_PlugIn.dll\\DSP_TR_ShutterRight"],
		ShutterUp:[guid:"CES_PlugIn.dll\\DSP_TR_ShutterUp"],
		SlicesHorizontalI:[guid:"CES_PlugIn.dll\\DSP_TR_SlicesH1"],
		SlicesHorizontalII:[guid:"CES_PlugIn.dll\\DSP_TR_SlicesH2"],
		SlicesVerticalI:[guid:"CES_PlugIn.dll\\DSP_TR_SlicesV1"],
		SlicesVerticalII:[guid:"CES_PlugIn.dll\\DSP_TR_SlicesV2"],
		SlideDown:[guid:"CES_PlugIn.dll\\DSP_TR_Slide",  params:[[type:"UNSIGNED_INT", value:"1"]]],
		SlideLeft:[guid:"CES_PlugIn.dll\\DSP_TR_Slide",  params:[[type:"UNSIGNED_INT", value:"2"]]],
		SlideRight:[guid:"CES_PlugIn.dll\\DSP_TR_Slide", params:[[type:"UNSIGNED_INT", value:"3"]]],
		SlideSpinHorizontalI:[guid:"CES_PlugIn.dll\\DSP_TR_SlideSpin1"],
		SlideSpinHorizontalII:[guid:"CES_PlugIn.dll\\DSP_TR_SlideSpin2"],
		SlideUp:[guid:"CES_PlugIn.dll\\DSP_TR_Slide",    params:[[type:"UNSIGNED_INT", value:"0"]]],
		Sparkle:[guid:"CES_PlugIn_5.dll\\Ces_DSP_TR_Sparkle"],
		SpinHorizontalLeft:[guid:"CES_PlugIn.dll\\DSP_TR_SpinLeft"],
		SpinHorizontalRight:[guid:"CES_PlugIn.dll\\DSP_TR_SpinRight"],
		Spiral:[guid:"CES_PlugIn_4.dll\\DSP_TR_Spiral"],
		SplicesHorizontal:[guid:"CES_PlugIn.dll\\DSP_TR_SplicesH"],
		SplicesVertical:[guid:"CES_PlugIn.dll\\DSP_TR_SplicesV"],
		Star:[guid:"CES_PlugIn_3.dll\\DSP_TR_AlphaTransition"],
		SwapDown:[guid:"CES_PlugIn.dll\\DSP_TR_SwapDown"],
		SwapLeft:[guid:"CES_PlugIn.dll\\DSP_TR_SwapLeft"],
		SwapRight:[guid:"CES_PlugIn.dll\\DSP_TR_SwapRight"],
		SwapUp:[guid:"CES_PlugIn.dll\\DSP_TR_SwapUp"],
		SweetHeart:[guid:"CES_PlugIn_3.dll\\DSP_TR_TestAlpha"],
		SwingDown:[guid:"CES_PlugIn.dll\\DSP_TR_SwingDown"],
		SwingLeft:[guid:"CES_PlugIn.dll\\DSP_TR_SwingLeft"],
		SwingRight:[guid:"CES_PlugIn.dll\\DSP_TR_SwingRight"],
		SwingUp:[guid:"CES_PlugIn.dll\\DSP_TR_SwingUp"],
		Swinging:[guid:"CES_PlugIn.dll\\DSP_TR_Swinging"],
		ThresholdI:[guid:"CES_PlugIn.dll\\DSP_TR_Threshold1"],
		ThresholdII:[guid:"CES_PlugIn.dll\\DSP_TR_Threshold2"],
		TwistHorizontal:[guid:"CES_PlugIn.dll\\DSP_TR_HTwistS"],
		TwistVertical:[guid:"CES_PlugIn.dll\\DSP_TR_VTwistS"],
		TyphoonDown:[guid:"CES_PlugIn.dll\\DSP_TR_3DTyphoonDown"],
		TyphoonLeft:[guid:"CES_PlugIn.dll\\DSP_TR_3DTyphoonLeft"],
		TyphoonRight:[guid:"CES_PlugIn.dll\\DSP_TR_3DTyphoonRight"],
		TyphoonUp:[guid:"CES_PlugIn.dll\\DSP_TR_3DTyphoonUp"],
		Wave:[guid:"CES_PlugIn.dll\\DSP_TR_Wave"],
		WaveWipeDown:[guid:"CES_PlugIn.dll\\DSP_TR_WaveWipeDown"],
		WaveWipeLeft:[guid:"CES_PlugIn.dll\\DSP_TR_WaveWipeLeft"],
		WaveWipeRight:[guid:"CES_PlugIn.dll\\DSP_TR_WaveWipeRight"],
		WaveWipeUp:[guid:"CES_PlugIn.dll\\DSP_TR_WaveWipeUp"],
		WindshieldLeft:[guid:"CES_PlugIn_4.dll\\DSP_TR_WindshieldLeft"],
		WipeCenter:[guid:"CES_PlugIn.dll\\DSP_TR_WipeCenter"],
		WipeClockwise:[guid:"CES_PlugIn.dll\\DSP_TR_WipeCW"],
		WipeCounterclockwise:[guid:"CES_PlugIn.dll\\DSP_TR_WipeCCW"],
		WipeDown:[guid:"CES_PlugIn.dll\\DSP_TR_WipeDown"],
		WipeDownDirty:[guid:"CES_PlugIn.dll\\DSP_TR_DirtyWipeDown"],
		WipeDownSoft:[guid:"CES_PlugIn.dll\\DSP_TR_WipeSoftDown"],
		WipeLeft:[guid:"CES_PlugIn.dll\\DSP_TR_WipeLeft"],
		WipeLeftDirty:[guid:"CES_PlugIn.dll\\DSP_TR_DirtyWipeLeft"],
		WipeLeftSoft:[guid:"CES_PlugIn.dll\\DSP_TR_WipeSoftLeft"],
		WipeRight:[guid:"CES_PlugIn.dll\\DSP_TR_WipeRight"],
		WipeRightDirty:[guid:"CES_PlugIn.dll\\DSP_TR_DirtyWipeRight"],
		WipeRightSoft:[guid:"CES_PlugIn.dll\\DSP_TR_WipeSoftRight"],
		WipeUp:[guid:"CES_PlugIn.dll\\DSP_TR_WipeUp"],
		WipeUpDirty:[guid:"CES_PlugIn.dll\\DSP_TR_DirtyWipeUp"],
		WipeUpSoft:[guid:"CES_PlugIn.dll\\DSP_TR_WipeSoftUp"],
		WormHole:[guid:"CES_PlugIn.dll\\DSP_TR_WormHole"],
		XRay:[guid:"CES_PlugIn.dll\\DSP_TR_XRay"]
	]
}