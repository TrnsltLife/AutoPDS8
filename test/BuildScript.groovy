import org.silcongo.autopds.app.*

def producer = new ScriptProducer(args[0], "UTF-8")
producer.createXMLFile()
