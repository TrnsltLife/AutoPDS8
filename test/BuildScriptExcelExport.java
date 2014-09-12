import org.silcongo.autopds.app.*;

class BuildScriptExcelExport
{
	public static void main(String args[])
	{
		/*
		 * "C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/kkw-Teke/JSW-ReadAndGrow-Teke.txt"
		 * "C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/mdw-Mbochi/JSW-ReadAndGrow-Mbochi.txt"
		 * "C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/bkw-Bekwel/JSW-ReadAndGrow-Bekwel.txt"
		 * "C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/beq-Beembe/JSW-ReadAndGrow-Beembe.txt"
		 */
		String[] scripts = {
				"D:/Videos-BZV5/Helloworld_script.txt",
				"D:/Videos-BZV5/06-TowerOfBabel/English/TowerOfBabel-English.txt"
				//"C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/kkw-Teke/JSW-ReadAndGrow-Teke.txt",
				//"C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/mdw-Mbochi/JSW-ReadAndGrow-Mbochi.txt",
				//"C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/bkw-Bekwel/JSW-ReadAndGrow-Bekwel.txt",
				//"C:/Users/Jeremy/Documents/SIL-Congo/ScriptureGiftMission/VideoMediaFiles/JSW/src-media/beq-Beembe/JSW-ReadAndGrow-Beembe.txt"
		};

		for(String script : scripts)
		{
			ScriptProducerExcelExport producer = new ScriptProducerExcelExport(script, "UTF-8");
			producer.processScript();
			producer.createXMLFile();
		}
	}
}