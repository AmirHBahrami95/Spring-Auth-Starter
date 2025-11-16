import os
import requests as reqs
import json
from .file_utils import * 

def runTest(title,url,method='get',expected=200,headers=None,dataPath=None,data=None,dumpPath=None,useDefaults=True,bearer=None,dumpBearer=True):

	""" 
		run a typical test and either show or save response as json , only 
		url and title are mandatory. the response is returned for further 
		processing if needed
		---
		'title' : is shown to demostrate how the test has gone, and 'expected' is 
		what you want the test status code to be, in order to be considered
		successful
		---
		'dumpPath' : if provided, the json response will be written to it, but beware
		that there's no double check, so it might go wrong

		if set to '-' , the json response is written to stdout
		---
		'data' : is supposed to be post data, if you have get data, embed them
		manually in your url (cuz i dont give enough fucks to make that a system
		too)
		---
		'dataPath' : if set, 'data' is read from it. it's assumed to be of json
		format, no other format
		
		(if both 'dataPath' and 'data' are given, the former is ignored)
		---
		'useDefaults' : makes sure you don't need to resolve relative/absolute
		path to your data. AND you don't need to write baseUrl and they are 
		resolved automatically. file paths are automatically resolved to 
		data/{filepath}

		WARNING: for urls, leave out the leading '/' :
		[ ] /login
		[x] login

		it's True by default, and you should leave it like that for better usage.
		however you can set to False anytime to read from some other directory 

		(however know that by setting it to False, you are accepting the truth
		that you are a retarded fulltime prick who has a cluster of mental issues)
		---
		'bearer': if present should refer to some json file with any 'token' attribtue
		there's no support for 'bearer' vs 'bearerPath' yet (or ever) bc that's simply
		not needed, just pass the filename as bearer and you'll do fine
		---
		dumpBearer : by default True, adds 'bearer' to the response json. this option
		only works if bearer is set. setting this option to True later helps cleanup
		the data u put on db

		for instance bc of cascading rules, you cannot just 'delete' a 'user'. if u
		add CarryOffer co1 and co2 belonging to usr-0 and co3 belonging to usr-2
		it's very probable that you'll forget which object belonged to whom

		so in order to prevent that, set this header
	"""
	
	# defaults
	settings=getSettings()
	if useDefaults:
		url=f"{getBaseUrl()}/api/v1/{url}"
		if dumpPath!=None and dumpPath!='-':
			dumpPath=f"{settings['dataDir']}{os.sep}{dumpPath}"
		if dataPath!=None:
			dataPath=f"{settings['dataDir']}{os.sep}{dataPath}"
			if data==None:
				data=readJsonF(dataPath)
		if headers==None:
			headers={} # avoiding problems
		if bearer:
			bearerPath=bearer # for dumping it later
			bearer=readJsonF(f"{settings['dataDir']}{os.sep}{bearer}")['token']
			headers['Authorization']=f"Bearer {bearer}"
	
	"""
	print(url)
	print(method)
	print(data)
	print(headers)
	print("--------------------")
	# """
	
	# actual reading
	response=None
	if method.lower()=='get':
		response=reqs.get(url=url,json=data,headers=headers)
	elif method.lower()=='post':
		response=reqs.post(url=url,json=data,headers=headers)


	# prompt and dump
	outStr="[x]" if response.status_code==expected else "[ ]"
	outStr=f"{outStr}{title} -> {response.status_code}"
	print(outStr)

	if dumpPath == '-':
		try:
			print(response.json())
		except:
			print("could not produce JSON output")
	elif dumpPath!=None and dumpPath!= '-':
		output=obj=response.json()
		if dumpBearer and bearer!=None:
			output['bearer']=bearerPath
		if response!=None:
			writeJsonF(obj=output,path=dumpPath)

	return response # for further processing at user functions
