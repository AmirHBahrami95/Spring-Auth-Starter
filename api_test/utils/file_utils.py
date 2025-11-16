import json
import os

def writeJsonF(obj,path):
	with open(path,'w') as fout:
		json.dump(obj,fout)

def readJsonF(path):
	obj=None
	with open(path,"r") as fin:
		obj=json.load(fin)
	return obj

def getSettings():
	return readJsonF("settings.json")

def getBaseUrl():
	"""protocol//base-url:port"""
	settings=getSettings()
	return f"{settings['protocol']}://{settings['baseUrl']}:{settings['port']}"

def getJsonResource(resource):
	"""uses data-dir by default for convinience. you can omit the '.json' part"""
	resource=resource.replace('/',os.sep) # for windows guys (ugh)
	settings=getSettings()
	if not resource.endswith('.json'):
		resource=f"{resource}.json"
	return readJsonF(f"{settings['dataDir']}{os.sep}{resource}") # for passwd

def delJsonResource(resource):
	resource=resource.replace('/',os.sep)
	settings=getSettings()
	if not resource.endswith('.json'):
		resource=f"{resource}.json"
	try:
		os.remove(f"{settings['dataDir']}{os.sep}{resource}")
	except:
		print(f"could not delete {settings['dataDir']}{os.sep}{resource}")
