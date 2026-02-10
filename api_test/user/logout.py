from utils import runTest

def logout(n=0,log=False,bearerPath='usr/usr'):
	if log:
		print(f"sending: usr-{n}")
	runTest(
		title="logout",
		url='auth/logout',
		expected=200,
		bearer=f"{bearerPath}-{n}.json",
		dumpPath = '-' if log else None
	)

def without_bearer():
	runTest(
		title="logout_no_bearer",
		url='auth/logout',
		expected=401,
		# dumpPath='-'
	)

def access_sth_after_logout():
	runTest(
		title="logout_no_bearer",
		url='country/ir',
		expected=401,
		bearer="usr/usr-0.json",
		dumpPath='-'
	)

