import user
import sys
import time
import utils
runTest=utils.runTest
# reg=user.register

def to_admin(n=0,log=False,saveDir="usr"):
	""" hint: you can use this method to make actual admins for further testing in 
	other modules and that's why 'saveDir=usr'. """

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_ADMIN'

	if log:
		print(f"sending : {dude}")

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/promote-user',
		expected=200,
		bearer="usr/usr-255.json",
		data=dude,
		dumpPath=f"{saveDir}/promote-{n}.json"
	)

def _dups(n=0,saveDir='usr/promote'):

	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_ADMIN'

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/promote-user',
		expected=400, # database coredump
		bearer="usr/usr-255.json",
		data=dude,
		# dumpPath='-'
	)

def _non_existing_role(n=0,saveDir='usr/promote'):
	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_HOG_RIDER'

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/promote-user',
		expected=404, # database coredump
		bearer="usr/usr-255.json",
		data=dude,
		# dumpPath='-'
	)

def _as_non_admin(n=0,saveDir='usr/promote'):
	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_ADMIN'

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/promote-user',
		expected=403, # database coredump
		bearer=f"{saveDir}/usr-1.json",
		data=dude,
		# dumpPath='-'
	)

def _promote_self(n=0,saveDir='usr/promote'):
	dude=utils.getJsonResource(f"{saveDir}/usr-{n}.json")['user'];
	dude['role']='ROLE_ADMIN'

	runTest(
		title=f"{__name__}.{sys._getframe().f_code.co_name}",
		method="post",
		url='user/admin/promote-user',
		data=dude,
		expected=403, # database coredump
		bearer=f"{saveDir}/usr-{n}.json",
		# dumpPath='-'
	)

def _init(saveDir='usr/promote'):	
	print("-------------",__name__+'.init',"------------")
	user.initUsers(n=5,saveDir=saveDir,log=False)
	user.initAdminUser() # required for testing

def _cleanup(saveDir='usr/promote'):
	print("-------------",__name__+'.cleanup',"------------")
	user.cleanup(initAdmin=True)
	utils.cleanFiles(dir=saveDir,fileFormats=['promote','usr'])

def flow():
	# print("-------------",__name__,"------------")
	saveDir='usr/promote'
	_cleanup(saveDir)
	time.sleep(1)
	_init(saveDir)
	time.sleep(1)
	print("-------------",__name__,"------------")
	usr_no=3
	to_admin(usr_no,saveDir=saveDir)
	_dups(usr_no,saveDir=saveDir)
	_non_existing_role(usr_no,saveDir=saveDir)
	_promote_self(usr_no,saveDir=saveDir)

	# new users needed
	_as_non_admin(usr_no+1,saveDir=saveDir)
	# """
