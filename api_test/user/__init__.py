import time
import utils
import user.change_passw as cp
import user.register as reg
import user.login as login
import user.logout as logout
import user.whoami as whoami
import user.promote as promote
import user.demote as demote

def initAdminUser():
	""" from now on, 255 is admin """
	login.correct(255)

def logoutAdmin(cleanFiles=True):
	logout.logout(255)
	if cleanFiles:
		utils.delJsonResource(f"usr/usr-255")
		utils.delJsonResource(f"usr/whoami-255")

def initUsers(n=10,log=False,saveDir="usr"):
	""" init users lets other test modules init well...users! you gotta remember to call
	the cleanup after you have used this method inside each module. This module is verbose 
	as of now, but will probably change in the future """

	for i in range(n):
		try:
			reg.register(i,log,saveDir)
			# whoami.get(i,saveDir)
		except:
			pass

def cleanup(n=10,initAdmin=True):

	""" this method lets modules not need to call and take care of admin stuff by themselved. 
	if u however had initialized an admin already, you can pass False as thesecond argument 
	to avoid dipshit.
	---
	'n': how many users you want cleaned up. has to be the same number of users that you
	initialize for the current session/module
	---
	'initAdmin': since a lot of modules may wanna initialize admin themselves if you leave
	this field empty (or True), an admin won't be initialzed. Note that to clean users, the
	server's endpoint requires admin rights (thus all the fuss)
	---
	'savedDir': where have you saved your user data (for the current session/module)
	---
	'cleanFiles': speaks for itself
	"""

	if initAdmin:
		initAdminUser()

	utils.runTest(
		title="cleanup-users",
		bearer='usr/usr-255.json', # now we need admin bullshit
		url="user/Tewb0.graihys",
		dumpPath='-'
	)
	
	# only logout the admin and not delete the fucker
	logoutAdmin(cleanFiles=initAdmin)

def _cleanup_user_tests():
	cleanup(initAdmin=True)
	utils.cleanFiles(n=10,dir='usr',fileFormats=["usr","whoami","demote"])

def flow_usr_stuff():

	# """
	_cleanup_user_tests()
	time.sleep(1);
	reg.register()
	whoami.get()
	reg.dups()
	reg.no_passw()
	reg.no_email()
	reg.email_dups()
	logout.logout() # can be natural to be wrong since there's no cleanup mechanism
	logout.access_sth_after_logout()
	whoami.no_access_token()
	logout.without_bearer()
	login.correct()
	login.dups()
	whoami.get()
	login.with_bearer_token_post()
	login.with_bearer_token_get()
	# logout(log=True)
	login.wrong_passw()
	login.wrong_uname()
	login.non_existent()
	logout.without_bearer()
	reg.with_uid()
	reg.fake_uid()
	reg.no_fname()
	reg.no_lname()
	reg.uname_dups()
	reg.phone_provided()
	#	"""

def flow():
	flow_usr_stuff()
	promote.flow()
	demote.flow()
	cp.flow()
