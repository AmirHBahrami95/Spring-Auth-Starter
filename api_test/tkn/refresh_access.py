import user
import user.register as reg
import sys
import utils
import time
runTest=utils.runTest

def with_bearer():
	ref=dict()
	ref['refreshToken']=utils.getJsonResource('/usr/usr-1.json')['refreshToken']
	runTest(
		title=sys._getframe().f_code.co_name,
		method="post",
		url="auth/refresh-access",
		bearer='usr/usr-1.json',
		data=ref,
		# dumpPath='-',
		expected=403,
	)

def not_revoked_yet():
	ref=dict()
	ref['refreshToken']=utils.getJsonResource('/usr/usr-1.json')['refreshToken']
	runTest(
		title=sys._getframe().f_code.co_name,
		method="post",
		url="auth/refresh-access",
		data=ref,
		# dumpPath='-',
		expected=403,
	)

def refresh_after_revoke():
	runTest(
		title="revoking_before_refresh",
		method="get",
		url="auth/test/revoke-access",
		bearer='usr/usr-1.json',
		# dumpPath='-',
		expected=200,
	)

	ref=dict()
	ref['refreshToken']=utils.getJsonResource('/usr/usr-1.json')['refreshToken']
	runTest(
		title=sys._getframe().f_code.co_name,
		method="post",
		url="auth/refresh-access",
		data=ref,
		dumpPath='usr/usr-1-bearer.json',
		expected=200,
	)

def non_existing():
	ref=dict()
	ref['refreshToken']="f5a4f640-7bf0-43ba-b9be-d3e4ef1be331"
	runTest(
		title=sys._getframe().f_code.co_name,
		method="post",
		url="auth/refresh-access",
		data=ref,
		# dumpPath='-',
		expected=404,
	)

def whoami_new_token():
	runTest(
		title=sys._getframe().f_code.co_name,
		method="get",
		url="user/whoami",
		bearer='usr/usr-1-bearer.json',
		# dumpPath='-',
		expected=200,
	)

def use_new_token():
	runTest(
		title=sys._getframe().f_code.co_name,
		method="get",
		url="country/ir",
		bearer='usr/usr-1-bearer.json',
		# dumpPath='-',
		expected=200,
	)

def logout_after_succesfull_refresh():
	runTest(
		title=sys._getframe().f_code.co_name,
		method="get",
		url="auth/logout",
		bearer='usr/usr-1-bearer.json',
		# dumpPath='-',
		expected=200,
	)

def use_sth_after_logout():
	runTest(
		title=sys._getframe().f_code.co_name,
		method="get",
		url="auth/logout",
		bearer='usr/usr-1-bearer.json',
		# dumpPath='-',
		expected=401,
	)

def refresh_burnt_session():
	ref=dict()
	ref['refreshToken']=utils.getJsonResource('/usr/usr-1.json')['refreshToken']
	runTest(
		title="refresh_burnt_session (after logout) ",
		method="post",
		data=ref,
		url="auth/refresh-access",
		# dumpPath='-',
		expected=404,
	)

def flow():
	print("-------------",__name__,"------------")
	user.cleanup(initAdmin=True)
	user.initUsers(3)
	non_existing()
	with_bearer()
	not_revoked_yet()
	
	print("WAITING FOR INTEGRITY (without it, tests will fail bc of reaction time in server)...")
	time.sleep(1)

	refresh_after_revoke()
	whoami_new_token()
	use_new_token()
	logout_after_succesfull_refresh()
	use_sth_after_logout()

	print("WAITING FOR INTEGRITY (without it, tests will fail bc of reaction time in server)...")
	time.sleep(1)

	refresh_burnt_session()
	user.cleanup(initAdmin=True)
