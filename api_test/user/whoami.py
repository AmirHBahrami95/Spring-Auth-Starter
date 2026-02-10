from utils import runTest 

def get(n=0,log=False,saveDir="usr"):
	if log:
		print(f"whoami/{n}")
	runTest(
		title="whoami",
		url='user/whoami',
		expected=200,
		# dataPath='usr/register-0.json',
		bearer=f"{saveDir}/usr-{n}.json",
		dumpPath=f"{saveDir}/whoami-{n}.json"
	)

def no_access_token(n=0):	
	""" run this test after you call logout on user number {n} """

	runTest(
		title="whoami_no_access_token",
		url='user/whoami',
		expected=401,
		# dataPath='usr/register-0.json',
		bearer=f"usr/usr-{n}.json",
		dumpPath=f"usr/whoami-{n}.json"
	)
