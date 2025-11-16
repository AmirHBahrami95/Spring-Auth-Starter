from utils import runTest 

def whoami(n=0):
	runTest(
		title="whoami",
		url='user/whoami',
		expected=200,
		# dataPath='usr/register-0.json',
		bearer=f"usr/usr-{n}.json",
		dumpPath=f"usr/whoami-{n}.json"
	)

