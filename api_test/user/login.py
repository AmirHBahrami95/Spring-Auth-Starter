from utils import runTest 
import user.typicals as tps

def login_w_dto(n=0,dumpFormat='usr/usr',loginDto=None):
	
	if loginDto == None:
		loginDto=tps.getTypicalRegister(n)
	
	runTest(
		title="login_correct",
		url='auth/login',
		method='post',
		expected=200,
		data=loginDto,
		headers={"content-type":"application/json"},
		dumpPath=f"{dumpFormat}-{n}.json"
		# dumpPath="-"
	)

def correct(n=0,saveDir='usr',fileName='usr'):
	# making up the guy (XXX do NOT remove the register-x files 
	# thinking this mechanism is enough, it'll break shit up. probably)

	runTest(
		title="login_correct",
		url='auth/login',
		method='post',
		expected=200,
		dataPath=f"usr/register-{n}.json", # XXX mind the {os.sep}! (actually fuck it
		headers={"content-type":"application/json"},
		dumpPath=f"{saveDir}/{fileName}-{n}.json"
		# dumpPath="-"
	)

def dups():
	runTest(
		title="login_dups",
		url='auth/login',
		method='post',
		expected=200,
		dataPath='usr/register-0.json', # testing for duplicates
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

# XXX this is important
def with_bearer_token_post():
	runTest(
		title="login_with_bearer_token",
		url='auth/login',
		method='post', # the poins is, the method should be post, and with bearer it fucks things up
		expected=403,
		dataPath='usr/register-0.json',
		headers={"content-type":"application/json"},
		bearer='usr/usr-0.json',
	)

def with_bearer_token_get():
	runTest(
		title="login_with_bearer_token",
		url='auth/login',
		method='get', # the poins is, the method should be post, and with bearer it fucks things up
		expected=403,
		dataPath='usr/register-0.json',
		headers={"content-type":"application/json"},
		bearer='usr/usr-0.json',
	)


def non_existent():
	runTest(
		title="login_non_existent",
		url='auth/login',
		expected=404,
		method='post',
		data={"uname":"fuck-you","passw":"fuck-you-even-more"},
		headers={"content-type":"application/json"},
	)

def wrong_passw():	
	runTest(
		title="login_wrong_passw",
		method='post',
		expected=400,
		url='auth/login',
		data={"uname":"this-is-a-test-0","passw":"fuck-you-even-more"},
		headers={"content-type":"application/json"},
		dumpPath='-'
	)

def wrong_uname():	
	runTest(
		title="login_wrong_uname",
		expected=404,
		method='post',
		url='auth/login',
		data={"uname":"idontknowyou","passw":'LetUsF0-cukthingsUp!213ses'},
		headers={"content-type":"application/json"},
	)

