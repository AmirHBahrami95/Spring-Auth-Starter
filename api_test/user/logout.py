from utils import runTest 

def logout(n=0):
	runTest(
		title="logout",
		url='user/logout',
		expected=200,
		bearer=f"usr/usr-{n}.json",
		# dumpPath='-'
	)

def logout_without_bearer():
	runTest(
		title="logout_no_bearer",
		url='user/logout',
		expected=403
	)
