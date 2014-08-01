package org.silcongo.autopds

class PDSFile
{

	public static void writeFile(file, Project project)
	{
		PDSFile.writeFile(file, project.toString())
	}

	public static void writeFile(file, String xml)
	{
		new File(file).withWriter("UTF-16LE"){writer->
			writer << """Content-Type: multipart/related
--- partseparator
Content-Type: text/AEFF
Content-ID: movie-layout
--- separator
<?xml version="1.0" encoding="UTF-16"?>
$xml
\0
"""
		}
	}
	
	
	
	
	

	public static void writeFile8(file, Project project)
	{
		PDSFile.writeFile8(file, project.toString())
	}
	
	
	
	
	public static void writeFile8(file, String xml)
	{
		xml = xml.replaceAll(/&/, "&amp;")
		xml = xml.replaceAll(/</, "&lt;")
		xml = xml.replaceAll(/>/, "&gt;")
		def bufferSize = xml.size()
		
		new File(file).withWriter("UTF-16LE"){writer->
			writer << """<?xml version="1.0" encoding="UTF-8"?>
<Project ActiveApplication="PowerDirector" AppVersion="8" ARAVersion="3.0.0.1930" Pack="FALSE">
<ExtraDataList/>
<Aurora DiscFormat="DVD" TVSystem="NTSC" IncludeMenu="TRUE" DiscID="" IncludeSlide="FALSE" IncludeDVDRunTime="FALSE" DiscName="" AltDiscName="" CharSet="0" Protect="FALSE" bEnableSubtitle="FALSE" TransitionEffect="-1" ActiveMovieID="0" DiscLabel="" DiscFirstPlay="FALSE">
<SPModulePath></SPModulePath>
<MenuTimeoutInfo>
	<MenuDurationByUser>150000000</MenuDurationByUser>
	<MenuTimeoutOption>ONLYFIRSTPAGE</MenuTimeoutOption>
	<MenuTimeoutNavOption>FIRSTBUTTON</MenuTimeoutNavOption>
</MenuTimeoutInfo>
<OutputProfile>
	<SpeedQuality>0</SpeedQuality>
	<VideoBitRate>0</VideoBitRate>
	<VideoMaxBitRate>0</VideoMaxBitRate>
	<VideoMinBitRate>0</VideoMinBitRate>
	<VideoResolution>D1</VideoResolution>
	<AudioBitRate>0</AudioBitRate>
	<AudioType>LPCM</AudioType>
	<AudioStream>0</AudioStream>
	<AudioChannel>0</AudioChannel>
	<VideoCompression>DEFAULT</VideoCompression>
	<VideoAspectRatio>4_3</VideoAspectRatio>
	<PreferedVideoBitRate>0</PreferedVideoBitRate>
	<VideoFrameRate>0.000000</VideoFrameRate>
	<FieldOrder>FO_UNDEFINED</FieldOrder>
	<ColorSpace>UNKNOWN</ColorSpace>
	<ProfileLevelType>MAIN</ProfileLevelType>
	<CLVSMode>NONE</CLVSMode>
</OutputProfile>"""

	writer << """
<MenuPlayable ID="87569343634">
	<Caption Font=""></Caption>
	<AltCaption Font=""></AltCaption>
	<OutputPath></OutputPath>
	<ThumbnailPos>0</ThumbnailPos>
	<Protect>FALSE</Protect>
	<MenuTypes>0</MenuTypes>
	<OutputProfile><SpeedQuality>0</SpeedQuality>
		<VideoBitRate>0</VideoBitRate>
		<VideoMaxBitRate>0</VideoMaxBitRate>
		<VideoMinBitRate>0</VideoMinBitRate>
		<VideoResolution>D1</VideoResolution>
		<AudioBitRate>0</AudioBitRate>
		<AudioType>LPCM</AudioType>
		<AudioStream>0</AudioStream>
		<AudioChannel>0</AudioChannel>
		<VideoCompression>DEFAULT</VideoCompression>
		<VideoAspectRatio>4_3</VideoAspectRatio>
		<PreferedVideoBitRate>0</PreferedVideoBitRate>
		<VideoFrameRate>0.000000</VideoFrameRate>
		<FieldOrder>FO_UNDEFINED</FieldOrder>
		<ColorSpace>UNKNOWN</ColorSpace>
		<ProfileLevelType>MAIN</ProfileLevelType>
		<CLVSMode>NONE</CLVSMode>
	</OutputProfile>
	<CLMenu Type="ROOTMENU" ID="-1" SubID="-1">
		<MaterialGroup>
			<Highlight ID="0" Topic="" ClrKey="0" ClrMask1="16777215" ClrMask2="255" ClrMask3="65280" ClrMask4="16777215" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrIcon="0" ClrText="0" v43="TRUE">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<StrIcon></StrIcon>
				<IOHighlight>
					<Image></Image>
					<Image16v9></Image16v9>
					<HighlightIcon></HighlightIcon>
					<ClrBackground>0</ClrBackground>
					<ClrText>0</ClrText>
					<Clr1>0</Clr1>
					<Clr2>0</Clr2>
					<Clr3>0</Clr3>
				</IOHighlight>
			</Highlight>
			<Background ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyExt="16711935" ClrMaskExt="65280" ClrKeyExt1="16711935" ClrMaskExt1="65280" ClrKeyThumbnail="16711935" ClrMaskThumbnail="65280" bOpenConcateVideo="FALSE" DirtyDuration="FALSE" Count="0">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<Duration>10000000</Duration>
				<Repeat>FALSE</Repeat>
				<ExtSourceFile></ExtSourceFile>
				<Ext1SourceFile></Ext1SourceFile>
				<ExtHQSourceFile></ExtHQSourceFile>
				<Ext1HQSourceFile></Ext1HQSourceFile>
				<SmartSceneSource/>
				<MenuOpeningFile></MenuOpeningFile>
				<HQMenuOpeningFile></HQMenuOpeningFile>
				<PALMenuOpeningFile></PALMenuOpeningFile>
				<PALHQMenuOpeningFile></PALHQMenuOpeningFile>
				<MenuOpeningDuration>0</MenuOpeningDuration>
			</Background>
			<NavButton ID="0" Topic="" ClrKeyPrev="16711935" ClrMaskPrev="65280" ClrKeyNext="16711935" ClrMaskNext="65280" ClrKeyHome="16711935" ClrMaskHome="65280" ClrKeyFirst="16711935" ClrMaskFirst="65280" ClrKeyPlayAll="16711935" ClrMaskPlayAll="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<PrevFile></PrevFile>
				<NextFile></NextFile>
				<HomeFile></HomeFile>
				<FirstFile></FirstFile>
				<PlayAllFile></PlayAllFile>
				<ThumbnailFile></ThumbnailFile>
				<IOPCBG>
					<HighlightFirstPage></HighlightFirstPage>
					<ClrkeyFirstPage>0</ClrkeyFirstPage>
					<HighlightPrevPage></HighlightPrevPage>
					<ClrkeyPrevPage>0</ClrkeyPrevPage>
					<HighlightNextPage></HighlightNextPage>
					<ClrkeyNextPage>0</ClrkeyNextPage>
					<HighlightHomePage></HighlightHomePage>
					<ClrkeyHomePage>0</ClrkeyHomePage>
				</IOPCBG>
			</NavButton>"""
			
			writer << """
			<Layout ID="0" GroupID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" Width="720" Height="576" TextMenu="FALSE" PhysicalID="0" SmartScene="FALSE" MultiLayer="FALSE" v43="TRUE" bReadOnly="FALSE">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<TopicFolder></TopicFolder>
				<CaptionButton Alignment="1" VAlignment="1" fAlignment="0" fVAlignment="0" Properties="0" InitPivot="FALSE" KeepOrigText="FALSE" bMotionTransition="TRUE">
					<Position x="0" y="0" w="0" h="0"/>
					<TextPosition x="0" y="0" w="0" h="0"/>
					<OutlinePosition x="0" y="0" w="0" h="0"/>
					<NumericPosition x="0" y="0" w="0" h="0"/>
					<Pivot x="0" y="0"/>
					<OrigTextRect x="0" y="0" w="0" h="0"/>
					<IconRect x="0" y="0" w="0" h="0"/>
					<DiscNavSetting Visible="FALSE" DiscNavCmd="PLAYALL">
						<Text></Text>
						<TextAttribute ClrKey="0" Space="0" Alignment="0" Shadow="FALSE" SizeX="0" SizeY="0" Text="">
							<LogFont Height="0" Width="0" Escapement="0" Orientation="0" Weight="0" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName=""/>
							<TextAttrFontStyle FaceName="" CharSet="0" Height="0" Bold="FALSE" Italic="FALSE" Underline="FALSE" StrikeOut="FALSE"/>
							<TextAttrFaceAttr Enable="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/>
							<TextAttrBorderAttr Enable="FALSE" Size="0" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/>
							<TextAttrShadowAttr EnableFace="FALSE" EnableBorder="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0" OffsetX="0" OffsetY="0" Height="FALSE"/>
							<TextAttrParagraphAttr Alignment="0" LineSpacing="0" BeforeSpacing="0" AfterSpacing="0" LeftIndentation="0" RightIndentation="0"/>
						</TextAttribute>
					</DiscNavSetting>
					<TitleTransiton></TitleTransiton>
					<TitleTransitionDuration>0</TitleTransitionDuration>
					<TitleTransitionTemplate></TitleTransitionTemplate>
					<CESTransitionID></CESTransitionID>
				</CaptionButton>
			</Layout>
			<Frame ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" v43="TRUE">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<TempExtSourceFile></TempExtSourceFile>
				<IOFrame>
					<Image></Image>
					<Image16v9></Image16v9>
					<Mask></Mask>
					<Mask16v9></Mask16v9>
					<ThumbnailRegion><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion>
					<ThumbnailRegion16v9><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion16v9>
				</IOFrame>
			</Frame>
			<GraphicButton ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
			</GraphicButton>
			<Caption ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<TextContent></TextContent>
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
			</Caption>
			<Audio ID="0" Topic="" Gain="1" RepeatAudio="TRUE" FadeIn="0" FadeOut="0" TrimIn="0" TrimOut="0" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" EnableMultiSource="FALSE" AudioSrcCount="1" DefaultAudio="TRUE" ChannelCount="2" EnableSmartSound="FALSE">
				<SourceFile></SourceFile>
				<ExtSourceFile></ExtSourceFile>
				<ThumbnailFile></ThumbnailFile>
				<Duration>0</Duration>
			</Audio>
			<PageProfile ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
			</PageProfile>
			<Text ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<TextContent></TextContent>
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
			</Text>
			<LayerTemplate ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" Duration="0" CL3DLayerCount="0" BkgEditable="FALSE" MotionSupport="FALSE" CL3DTemplate="FALSE">
				<SourceFile></SourceFile>
				<ExtSourceFile></ExtSourceFile>
				<ThumbnailFile></ThumbnailFile>
				<MenuOpeningFile></MenuOpeningFile>
			</LayerTemplate>"""
			
			writer << """
			<Motion ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<ExtSourceFile></ExtSourceFile>
			</Motion>
			<Effect ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<ExtSourceFile></ExtSourceFile>
			</Effect>
			<PlayAllText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<TextContent></TextContent>
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
			</PlayAllText>
			<SceneText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<TextContent></TextContent>
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
			</SceneText>
			<SlideShowText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
				<TextContent></TextContent>
				<SourceFile></SourceFile>
				<ThumbnailFile></ThumbnailFile>
				<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
			</SlideShowText>
		</MaterialGroup>"""
		
		writer << """
		<CLSubMenu Type="SCENEMENU" ID="-2" SubID="-1">
			<MaterialGroup>
				<Highlight ID="0" Topic="" ClrKey="0" ClrMask1="16777215" ClrMask2="255" ClrMask3="65280" ClrMask4="16777215" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrIcon="0" ClrText="0" v43="TRUE">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<StrIcon></StrIcon>
					<IOHighlight><Image></Image>
						<Image16v9></Image16v9>
						<HighlightIcon></HighlightIcon>
						<ClrBackground>0</ClrBackground>
						<ClrText>0</ClrText>
						<Clr1>0</Clr1>
						<Clr2>0</Clr2>
						<Clr3>0</Clr3>
					</IOHighlight>
				</Highlight>
				<Background ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyExt="16711935" ClrMaskExt="65280" ClrKeyExt1="16711935" ClrMaskExt1="65280" ClrKeyThumbnail="16711935" ClrMaskThumbnail="65280" bOpenConcateVideo="FALSE" DirtyDuration="FALSE" Count="0">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<Duration>10000000</Duration>
					<Repeat>FALSE</Repeat>
					<ExtSourceFile></ExtSourceFile>
					<Ext1SourceFile></Ext1SourceFile>
					<ExtHQSourceFile></ExtHQSourceFile>
					<Ext1HQSourceFile></Ext1HQSourceFile>
					<SmartSceneSource/><MenuOpeningFile></MenuOpeningFile>
					<HQMenuOpeningFile></HQMenuOpeningFile>
					<PALMenuOpeningFile></PALMenuOpeningFile>
					<PALHQMenuOpeningFile></PALHQMenuOpeningFile>
					<MenuOpeningDuration>0</MenuOpeningDuration>
				</Background>
				<NavButton ID="0" Topic="" ClrKeyPrev="16711935" ClrMaskPrev="65280" ClrKeyNext="16711935" ClrMaskNext="65280" ClrKeyHome="16711935" ClrMaskHome="65280" ClrKeyFirst="16711935" ClrMaskFirst="65280" ClrKeyPlayAll="16711935" ClrMaskPlayAll="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<PrevFile></PrevFile>
					<NextFile></NextFile>
					<HomeFile></HomeFile>
					<FirstFile></FirstFile>
					<PlayAllFile></PlayAllFile>
					<ThumbnailFile></ThumbnailFile>
					<IOPCBG><HighlightFirstPage></HighlightFirstPage>
						<ClrkeyFirstPage>0</ClrkeyFirstPage>
						<HighlightPrevPage></HighlightPrevPage>
						<ClrkeyPrevPage>0</ClrkeyPrevPage>
						<HighlightNextPage></HighlightNextPage>
						<ClrkeyNextPage>0</ClrkeyNextPage>
						<HighlightHomePage></HighlightHomePage>
						<ClrkeyHomePage>0</ClrkeyHomePage>
					</IOPCBG>
				</NavButton>
				<Layout ID="0" GroupID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" Width="720" Height="576" TextMenu="FALSE" PhysicalID="0" SmartScene="FALSE" MultiLayer="FALSE" v43="TRUE" bReadOnly="FALSE">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<TopicFolder></TopicFolder>
					<CaptionButton Alignment="1" VAlignment="1" fAlignment="0" fVAlignment="0" Properties="0" InitPivot="FALSE" KeepOrigText="FALSE" bMotionTransition="TRUE"><Position x="0" y="0" w="0" h="0"/><TextPosition x="0" y="0" w="0" h="0"/><OutlinePosition x="0" y="0" w="0" h="0"/><NumericPosition x="0" y="0" w="0" h="0"/><Pivot x="0" y="0"/><OrigTextRect x="0" y="0" w="0" h="0"/><IconRect x="0" y="0" w="0" h="0"/><DiscNavSetting Visible="FALSE" DiscNavCmd="PLAYALL">
						<Text></Text>
						<TextAttribute ClrKey="0" Space="0" Alignment="0" Shadow="FALSE" SizeX="0" SizeY="0" Text="">
						<LogFont Height="0" Width="0" Escapement="0" Orientation="0" Weight="0" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName=""/>
						<TextAttrFontStyle FaceName="" CharSet="0" Height="0" Bold="FALSE" Italic="FALSE" Underline="FALSE" StrikeOut="FALSE"/>
						<TextAttrFaceAttr Enable="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/><TextAttrBorderAttr Enable="FALSE" Size="0" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/>
						<TextAttrShadowAttr EnableFace="FALSE" EnableBorder="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0" OffsetX="0" OffsetY="0" Height="FALSE"/>
						<TextAttrParagraphAttr Alignment="0" LineSpacing="0" BeforeSpacing="0" AfterSpacing="0" LeftIndentation="0" RightIndentation="0"/>
						</TextAttribute>
						</DiscNavSetting>
						<TitleTransiton></TitleTransiton>
						<TitleTransitionDuration>0</TitleTransitionDuration>
						<TitleTransitionTemplate></TitleTransitionTemplate>
						<CESTransitionID></CESTransitionID>
					</CaptionButton>
				</Layout>
				<Frame ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" v43="TRUE"><SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<TempExtSourceFile></TempExtSourceFile>
					<IOFrame><Image></Image>
					<Image16v9></Image16v9>
					<Mask></Mask>
					<Mask16v9></Mask16v9>
					<ThumbnailRegion><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion>
					<ThumbnailRegion16v9><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion16v9>
					</IOFrame>
				</Frame>"""
				
				writer << """
				<GraphicButton ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
				</GraphicButton>
				<Caption ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</Caption>
				<Audio ID="0" Topic="" Gain="1" RepeatAudio="TRUE" FadeIn="0" FadeOut="0" TrimIn="0" TrimOut="0" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" EnableMultiSource="FALSE" AudioSrcCount="1" DefaultAudio="TRUE" ChannelCount="2" EnableSmartSound="FALSE">
					<SourceFile></SourceFile>
					<ExtSourceFile></ExtSourceFile>
					<ThumbnailFile></ThumbnailFile>
					<Duration>0</Duration>
				</Audio>
				<PageProfile ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
				</PageProfile>
				<Text ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</Text>
				<LayerTemplate ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" Duration="0" CL3DLayerCount="0" BkgEditable="FALSE" MotionSupport="FALSE" CL3DTemplate="FALSE">
					<SourceFile></SourceFile>
					<ExtSourceFile></ExtSourceFile>
					<ThumbnailFile></ThumbnailFile>
					<MenuOpeningFile></MenuOpeningFile>
				</LayerTemplate>
				<Motion ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<ExtSourceFile></ExtSourceFile>
				</Motion>
				<Effect ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<ExtSourceFile></ExtSourceFile>
				</Effect>
				<PlayAllText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</PlayAllText>
				<SceneText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</SceneText>
				<SlideShowText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</SlideShowText>
			</MaterialGroup>
		</CLSubMenu>"""
		
		writer << """
		<CLSubMenu Type="SUBTITLEMENU" ID="-3" SubID="-1">
			<MaterialGroup>
				<Highlight ID="0" Topic="" ClrKey="0" ClrMask1="16777215" ClrMask2="255" ClrMask3="65280" ClrMask4="16777215" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrIcon="0" ClrText="0" v43="TRUE">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<StrIcon></StrIcon>
					<IOHighlight><Image></Image>
						<Image16v9></Image16v9>
						<HighlightIcon></HighlightIcon>
						<ClrBackground>0</ClrBackground>
						<ClrText>0</ClrText>
						<Clr1>0</Clr1>
						<Clr2>0</Clr2>
						<Clr3>0</Clr3>
					</IOHighlight>
				</Highlight>
				<Background ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyExt="16711935" ClrMaskExt="65280" ClrKeyExt1="16711935" ClrMaskExt1="65280" ClrKeyThumbnail="16711935" ClrMaskThumbnail="65280" bOpenConcateVideo="FALSE" DirtyDuration="FALSE" Count="0">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<Duration>10000000</Duration>
					<Repeat>FALSE</Repeat>
					<ExtSourceFile></ExtSourceFile>
					<Ext1SourceFile></Ext1SourceFile>
					<ExtHQSourceFile></ExtHQSourceFile>
					<Ext1HQSourceFile></Ext1HQSourceFile>
					<SmartSceneSource/><MenuOpeningFile></MenuOpeningFile>
					<HQMenuOpeningFile></HQMenuOpeningFile>
					<PALMenuOpeningFile></PALMenuOpeningFile>
					<PALHQMenuOpeningFile></PALHQMenuOpeningFile>
					<MenuOpeningDuration>0</MenuOpeningDuration>
				</Background>
				<NavButton ID="0" Topic="" ClrKeyPrev="16711935" ClrMaskPrev="65280" ClrKeyNext="16711935" ClrMaskNext="65280" ClrKeyHome="16711935" ClrMaskHome="65280" ClrKeyFirst="16711935" ClrMaskFirst="65280" ClrKeyPlayAll="16711935" ClrMaskPlayAll="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<PrevFile></PrevFile>
					<NextFile></NextFile>
					<HomeFile></HomeFile>
					<FirstFile></FirstFile>
					<PlayAllFile></PlayAllFile>
					<ThumbnailFile></ThumbnailFile>
					<IOPCBG>
						<HighlightFirstPage></HighlightFirstPage>
						<ClrkeyFirstPage>0</ClrkeyFirstPage>
						<HighlightPrevPage></HighlightPrevPage>
						<ClrkeyPrevPage>0</ClrkeyPrevPage>
						<HighlightNextPage></HighlightNextPage>
						<ClrkeyNextPage>0</ClrkeyNextPage>
						<HighlightHomePage></HighlightHomePage>
						<ClrkeyHomePage>0</ClrkeyHomePage>
					</IOPCBG>
				</NavButton>
				<Layout ID="0" GroupID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" Width="720" Height="576" TextMenu="FALSE" PhysicalID="0" SmartScene="FALSE" MultiLayer="FALSE" v43="TRUE" bReadOnly="FALSE">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<TopicFolder></TopicFolder>
					<CaptionButton Alignment="1" VAlignment="1" fAlignment="0" fVAlignment="0" Properties="0" InitPivot="FALSE" KeepOrigText="FALSE" bMotionTransition="TRUE">
					<Position x="0" y="0" w="0" h="0"/><TextPosition x="0" y="0" w="0" h="0"/>
					<OutlinePosition x="0" y="0" w="0" h="0"/><NumericPosition x="0" y="0" w="0" h="0"/>
					<Pivot x="0" y="0"/><OrigTextRect x="0" y="0" w="0" h="0"/>
					<IconRect x="0" y="0" w="0" h="0"/>
					<DiscNavSetting Visible="FALSE" DiscNavCmd="PLAYALL">
					<Text></Text>
					<TextAttribute ClrKey="0" Space="0" Alignment="0" Shadow="FALSE" SizeX="0" SizeY="0" Text="">
						<LogFont Height="0" Width="0" Escapement="0" Orientation="0" Weight="0" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName=""/>
						<TextAttrFontStyle FaceName="" CharSet="0" Height="0" Bold="FALSE" Italic="FALSE" Underline="FALSE" StrikeOut="FALSE"/><TextAttrFaceAttr Enable="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/>
						<TextAttrBorderAttr Enable="FALSE" Size="0" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0"/><TextAttrShadowAttr EnableFace="FALSE" EnableBorder="FALSE" ClrKey1="0" ClrKey2="0" GradType="NORTH" Alpha="0" BlurRadius="0" OffsetX="0" OffsetY="0" Height="FALSE"/>
						<TextAttrParagraphAttr Alignment="0" LineSpacing="0" BeforeSpacing="0" AfterSpacing="0" LeftIndentation="0" RightIndentation="0"/>
					</TextAttribute>
					</DiscNavSetting>
					<TitleTransiton></TitleTransiton>
					<TitleTransitionDuration>0</TitleTransitionDuration>
					<TitleTransitionTemplate></TitleTransitionTemplate>
					<CESTransitionID></CESTransitionID>
					</CaptionButton>
				</Layout>"""
				
				writer << """
				<Frame ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" v43="TRUE">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<TempExtSourceFile></TempExtSourceFile>
					<IOFrame><Image></Image>
						<Image16v9></Image16v9>
						<Mask></Mask>
						<Mask16v9></Mask16v9>
						<ThumbnailRegion><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion>
						<ThumbnailRegion16v9><p1 x="0" y="0"/><p2 x="0" y="0"/><p3 x="0" y="0"/><p4 x="0" y="0"/></ThumbnailRegion16v9>
					</IOFrame>
				</Frame>
				<GraphicButton ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
				</GraphicButton>
				<Caption ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</Caption>
				<Audio ID="0" Topic="" Gain="1" RepeatAudio="TRUE" FadeIn="0" FadeOut="0" TrimIn="0" TrimOut="0" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" EnableMultiSource="FALSE" AudioSrcCount="1" DefaultAudio="TRUE" ChannelCount="2" EnableSmartSound="FALSE">
					<SourceFile></SourceFile>
					<ExtSourceFile></ExtSourceFile>
					<ThumbnailFile></ThumbnailFile>
					<Duration>0</Duration>
				</Audio>
				<PageProfile ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
				</PageProfile>
				<Text ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</Text>
				<LayerTemplate ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280" Duration="0" CL3DLayerCount="0" BkgEditable="FALSE" MotionSupport="FALSE" CL3DTemplate="FALSE">
					<SourceFile></SourceFile>
					<ExtSourceFile></ExtSourceFile>
					<ThumbnailFile></ThumbnailFile>
					<MenuOpeningFile></MenuOpeningFile>
				</LayerTemplate>
				<Motion ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<ExtSourceFile></ExtSourceFile>
				</Motion>"""
				
				writer << """
				<Effect ID="0" Topic="" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280" ClrKeyExt="16711935" ClrMaskExt="65280">
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<ExtSourceFile></ExtSourceFile>
				</Effect>
				<PlayAllText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</PlayAllText>
				<SceneText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280">
					<TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</SceneText>
				<SlideShowText ID="0" Topic="" Shadow="TRUE" Effect="0" ClrText="0" Alignment="0" Spacing="100" ClrKey="16711935" ClrMask="65280" ClrKeyThumb="16711935" ClrMaskThumb="65280"><TextContent></TextContent>
					<SourceFile></SourceFile>
					<ThumbnailFile></ThumbnailFile>
					<LogFont Height="-11" Width="0" Escapement="0" Orientation="0" Weight="400" Italic="0" Underline="0" StrikeOut="0" CharSet="0" OutPrecision="0" ClipPrecision="0" Quality="0" PitchAndFamily="0" FaceName="MS Shell Dlg"/>
				</SlideShowText>"""
				
				writer << """
			</MaterialGroup>
		</CLSubMenu>
	</CLMenu>
</MenuPlayable>
<Playable ID="87569529807" PlayableType="MOVIE" Dummy="FALSE">
	<Caption Font=""></Caption>
	<AltCaption Font=""></AltCaption>
	<OutputPath></OutputPath>
	<ThumbnailPos>0</ThumbnailPos>
	<Protect>FALSE</Protect>
	<Duration>50050050</Duration>
	<DRM>FALSE</DRM>
	<PathName></PathName>
	<RVFileID>00000000000000000000000000000000000000000000000000000000000000000000000000</RVFileID>
	<MovieAttribute>BASIC</MovieAttribute>
	<EnableAudio>TRUE</EnableAudio>
	<OutputProfile><SpeedQuality>0</SpeedQuality>
		<VideoBitRate>0</VideoBitRate>
		<VideoMaxBitRate>0</VideoMaxBitRate>
		<VideoMinBitRate>0</VideoMinBitRate>
		<VideoResolution>D1</VideoResolution>
		<AudioBitRate>0</AudioBitRate>
		<AudioType>LPCM</AudioType>
		<AudioStream>0</AudioStream>
		<AudioChannel>2</AudioChannel>
		<VideoCompression>DEFAULT</VideoCompression>
		<VideoAspectRatio>4_3</VideoAspectRatio>
		<PreferedVideoBitRate>0</PreferedVideoBitRate>
		<VideoFrameRate>0.000000</VideoFrameRate>
		<FieldOrder>FO_UNDEFINED</FieldOrder>
		<ColorSpace>UNKNOWN</ColorSpace>
		<ProfileLevelType>MAIN</ProfileLevelType>
		<CLVSMode>NONE</CLVSMode>
	</OutputProfile>"""
	
	writer << """
	<VideoProperty>
		<Type>DEFAULT</Type>
		<Width>0</Width>
		<Height>0</Height>
		<VideoBitRate>0</VideoBitRate>
		<AudioSampleRate>0</AudioSampleRate>
		<AudioBitRate>0</AudioBitRate>
		<AudioChannel>0</AudioChannel>
		<AudioType>LPCM</AudioType>
		<AspectRatio>4_3</AspectRatio>
		<TLAspectRatio16_9>FALSE</TLAspectRatio16_9>
		<PreferredResolutionType>4</PreferredResolutionType>
		<PreferredProfileLevel>0</PreferredProfileLevel>
	</VideoProperty>"""
	
	writer << """
	<Chapter Type="DEFAULT" ChapterPoint="0">
		<Caption Font="">Chapter 1</Caption>
		<AltCaption Font=""></AltCaption>
		<ThumbnailPos>0</ThumbnailPos>
		<Thumbnail>
			<BitmapInfo>
				<BITMAPINFOHEADER Size="40" Width="320" Height="240" Planes="1" BitCount="24" Compression="0" SizeImage="230400" XPelsPerMeter="0" YPelsPerMeter="0" ClrUsed="0" ClrImportant="0"/>
				<RGBQUAD Red="0" Green="0" Blue="0" Reserved="0"/>
			</BitmapInfo>
			<ThumbnailSize>230444</ThumbnailSize>
			<Buffer>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</Buffer>
		</Thumbnail>
	</Chapter>"""
	
	writer << """
	<Thumbnail>
		<BitmapInfo>
			<BITMAPINFOHEADER Size="40" Width="320" Height="240" Planes="1" BitCount="24" Compression="0" SizeImage="230400" XPelsPerMeter="0" YPelsPerMeter="0" ClrUsed="0" ClrImportant="0"/>
			<RGBQUAD Red="0" Green="0" Blue="0" Reserved="0"/>
		</BitmapInfo>
		<ThumbnailSize>230444</ThumbnailSize>
		<Buffer>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</Buffer>
	</Thumbnail>"""
	
	writer << """
	<ExtraDataList/>
	<TimelineChunk>
		<TimelineSize>${bufferSize}</TimelineSize>
		<Buffer>&lt;?xml version="1.0"?&gt;
		${xml}
		</Buffer>
	</TimelineChunk>
	<Subtitle/>
</Playable>
</Aurora>
</Project>
"""
		}
	}

}