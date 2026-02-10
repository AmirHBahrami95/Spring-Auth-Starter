import time
import sys
import utils
import user
import user.typicals as tps
runTest=utils.runTest

def younger_than_18():
	younger=tps.getTypicalRegister(5)
	younger['personalInfo']['birthDate']='2024-02-01'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=400,
		method='post',
		url='auth/register',
		data=younger,
		dumpPath='-'
	)	

def birth_date_in_future():
	younger=tps.getTypicalRegister(5) # user 5 must not exist!
	younger['personalInfo']['birthDate']='2045-02-01'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=400,
		url='auth/register',
		method='post',
		data=younger,
	)	

def update_my_own_info(n=0):
	me=utils.getJsonResource(f"personal_info/usr-{n}.json")['user']['personalInfo']
	me['birthDate']='1992-02-01'
	me['address']['countryIso2']='us'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=200,
		url='user/personal-info/update',
		method='post',
		data=me,
		bearer=f"personal_info/usr-{n}.json",
		dumpPath=f"personal_info/update-{n}.json"
	)

def dups(n=0):
	me=utils.getJsonResource(f"personal_info/update-{n}.json")
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=400,
		url='user/personal-info/update',
		method='post',
		bearer=f"personal_info/usr-{n}.json",
		data=me,
		# dumpPath="-"
		# dumpPath=f"personal_info/update-dups.json"
	)

def update_someone_else(n=1,bearer=0):
	me=utils.getJsonResource(f"personal_info/usr-{n}.json")['user']
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=403,
		url='user/personal-info/update',
		method='post',
		bearer=f"personal_info/usr-{bearer}.json",
		data=me,
		# dumpPath="-"
	)

def update_as_admin(n=1):
	me=utils.getJsonResource(f"personal_info/usr-{n}.json")['user']['personalInfo']
	me['address']['countryIso2']='af'
	me['address']['city']=None
	me['address']['local']=None
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=200,
		url='user/personal-info/update',
		method='post',
		bearer=f"usr/usr-{255}.json",
		data=me,
		dumpPath=f"personal_info/update-as-admin.json"
		# dumpPath='-'
	)


def update_fname(n=0):
	me=utils.getJsonResource(f"personal_info/update-{n}.json")
	me['fname']='xiaomi'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=400,
		url='user/personal-info/update',
		method='post',
		bearer=f"personal_info/usr-{n}.json",
		data=me,
		dumpPath="-"
		# dumpPath=f"personal_info/update-dups.json"
	)


def _init(saveDir="personal_info"):
	print("-------------",__name__+'.init',"------------")
	user.initUsers(n=2,saveDir=saveDir,log=False)
	user.initAdminUser() # required for testing

def _cleanup(saveDir="personal_info"):
	print("-------------",__name__+'.cleanup',"------------")
	user.cleanup(initAdmin=True)
	utils.cleanFiles(dir=saveDir,fileFormats=['update','usr'])

def flow():
	_cleanup()
	_init()
	# time.sleep(1)

	print("-------------",__name__,"------------")
	younger_than_18()
	birth_date_in_future()
	update_my_own_info()
	dups()
	update_as_admin()
	update_fname()
