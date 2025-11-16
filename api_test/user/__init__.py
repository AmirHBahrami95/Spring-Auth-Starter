import utils
from .register import *
from .login import *
from .logout import *
from .whoami import *

def initUsers(n=10):
	for i in range(n):
		try:
			register(i)
			whoami(i)
		except:
			pass

def cleanup(n=10):
	utils.runTest(
		title="cleanup-users",
		url="user/Tewb0.graihys",
		dumpPath='-'
	)
	for i in range(n):
		utils.delJsonResource(f"usr/usr-{i}")
		utils.delJsonResource(f"usr/whoami-{i}")

def flow():
	# """
	register()# TODO make a cleanup
	register_dups()
	reg_no_passw()
	reg_no_email()
	reg_email_dups()
	logout() # can be natural to be wrong since there's no cleanup mechanism
	login_correct()
	login_dups()
	whoami()
	login_with_bearer_token_post()
	login_with_bearer_token_get()
	logout()
	login_wrong_passw()
	login_wrong_uname()
	login_non_existent()
	logout_without_bearer()
	register_with_uid()
	register_fake_uid()
	reg_no_fname()
	reg_no_lname()
	reg_uname_dups()
	reg_phone_provided()
	cleanup()
	#	"""
