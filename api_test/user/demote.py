import user
import sys
import time
import utils
import user.promote as promote
runTest=utils.runTest 
reg=user.register

def demote_another_admin(n=0,adminNo=255,log=False,saveDir='usr'):
	""" hint: you can use this method to make actual admins for further testing"""

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_USER'

	if log:
		print(f"sending : {dude}")

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/demote-user',
		expected=200,
		bearer=f"usr/usr-{adminNo}.json",
		data=dude,
		dumpPath=f"{saveDir}/demote-{n}.json"
	)

def _dups(n=0,adminNo=255,log=False,saveDir='usr'):
	""" hint: you can use this method to make actual admins for further testing"""

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_USER'

	if log:
		print(f"sending : {dude}")

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/demote-user',
		expected=400,
		bearer=f"usr/usr-{adminNo}.json",
		data=dude,
		dumpPath='-'
	)

def _as_non_admin(n=0,bearer=1,log=False,saveDir='usr'):
	""" hint: you can use this method to make actual admins for further testing"""

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_USER'

	if log:
		print(f"sending : {dude}")

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/demote-user',
		expected=403,
		bearer=f"{saveDir}/usr-{bearer}.json",
		data=dude,
		# dumpPath=f"usr/demote-{n}.json"
		dumpPath='-',
	)

def _non_existing(n=0,adminNo=255,log=False,saveDir='usr'):
	""" hint: you can use this method to make actual admins for further testing"""

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['id']='d46184b5-46e0-4a52-b072-8b8694601d28'
	dude['role']='ROLE_USER'

	if log:
		print(f"sending : {dude}")

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/demote-user',
		expected=404,
		bearer=f"{saveDir}/usr-{adminNo}.json",
		data=dude,
		dumpPath='-'
	)

def _cleanup(saveDir='usr/demote'):
	print("-------------",__name__+'.cleanup',"------------")
	user.cleanup(initAdmin=True)
	utils.cleanFiles(dir=saveDir,fileFormats=['promote','usr'])

def _init(saveDir='usr/demote'):
	print("-------------",__name__+'.init',"------------")
	user.initUsers(n=5,saveDir=saveDir,log=False)
	user.initAdminUser() # required for testing


def flow():
	saveDir='usr/demote'
	_cleanup(saveDir)
	_init(saveDir)

	print("-------------",__name__,"------------")
	promote.to_admin(1,saveDir=saveDir)
	demote_another_admin(1,saveDir=saveDir)
	_dups(1,saveDir=saveDir)

	promote.to_admin(2,saveDir=saveDir)
	_as_non_admin(n=2,bearer=1,saveDir=saveDir)
