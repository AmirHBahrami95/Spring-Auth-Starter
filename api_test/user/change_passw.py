import sys
import time

import user.logout as logout
import user.login as login
import user
import utils
import user.typicals as tps
runTest=utils.runTest 

def change_password(n=0):
	passwDto=tps.getTypicalRegister(n);
	passwDto['oldPassword']=passwDto['passw']
	passwDto['newPassword']='please go whack yourself21341#!@'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=200,
		method='post',
		url='user/change-password',
		data=passwDto,
		bearer=f"usr/change_password/usr-{n}.json",
		# dumpPath='-'
		# dumpPath=f"usr/change_password/cp-{n}.json"
	)

def _test_new_passw(n=0):
	dto=tps.getTypicalRegister(n);
	dto['passw']='please go whack yourself21341#!@'
	logout.logout(n=0,log=True,bearerPath='usr/change_password/usr')
	login.login_w_dto(n=0,dumpFormat='usr/change_password/login_again',loginDto=dto)

def _unauthenticated(n=1):
	passwDto=tps.getTypicalRegister(n);
	passwDto['oldPassword']=passwDto['passw']
	passwDto['newPassword']='please go whack yourself21341#!@'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=401,
		method='post',
		url='user/change-password',
		data=passwDto,
		# dumpPath='-'
	)

def _as_admin(n=1):
	passwDto=utils.getJsonResource(f"usr/change_password/usr-{n}")['user']
	passwDto['newPassword']='please go whack yourself21341#!@'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=200,
		method='post',
		url='user/admin/change-password',
		data=passwDto,
		bearer="usr/usr-255.json",
		dumpPath=f"usr/change_password/cp-as-admin-{n}.json"
	)

def _as_non_admin(n=1):
	passwDto=utils.getJsonResource(f"usr/change_password/usr-{n}")
	passwDto['id']=passwDto['id']
	passwDto['newPassword']='this is set by none admin'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=403,
		method='post',
		url='user/admin/change-password',
		data=passwDto,
		bearer="usr/change_password/usr-1.json",
		dumpPath=f"usr/change_password/cp-as-admin-{n}.json"
	)

def _non_existing_as_admin():
	passwDto=dict()
	passwDto['id']='5e822d80-a55b-4cf5-a527-adb3aca23a3d'
	passwDto['newPassword']='please go whack yourself21341#!@'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=404,
		method='post',
		url='user/admin/change-password',
		data=passwDto,
		bearer="usr/usr-255.json",
		# dumpPath='-'
	)

def _wrong_old_passw(n=2):
	passwDto=tps.getTypicalRegister(n);
	passwDto['oldPassword']='this is a big fuckery'
	passwDto['newPassword']='please go whack yourself21341#!@'
	runTest(
		title=sys._getframe().f_code.co_name,
		expected=400,
		method='post',
		url='user/change-password',
		data=passwDto,
		bearer=f"usr/change_password/usr-{n}.json",
		dumpPath=f"usr/change_password/cp-{n}.json"
	)

def _init(saveDir="usr/change_password"):
	print("-------------",__name__+'.init',"------------")
	user.initUsers(n=3,saveDir=saveDir,log=False)
	user.initAdminUser() # required for testing

def _cleanup(saveDir="usr/change_password"):
	print("-------------",__name__+'.cleanup',"------------")
	user.cleanup(initAdmin=True)
	utils.cleanFiles(dir=saveDir,fileFormats=['cp-as-admin','usr','cp'])

def flow():
	_cleanup()
	_init()
	# time.sleep(1)

	print("-------------",__name__,"------------")
	change_password(0)
	_test_new_passw(0)
	time.sleep(1)
	_unauthenticated(1)
	_wrong_old_passw(2)
	_as_admin(1)
	_non_existing_as_admin()
