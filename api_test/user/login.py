from utils import runTest 

def login_correct(n=0):
	# making up the guy (XXX do NOT remove the register-x files 
	# thinking this mechanism is enough, it'll break shit up. probably)

	runTest(
		title="login_correct",
		url='user/login',
		method='post',
		expected=200,
		dataPath=f"usr/register-{n}.json", # XXX mind the {os.sep}! (actually fuck it) 
		# data=data,
		headers={"content-type":"application/json"},
		dumpPath=f"usr/usr-{n}.json"
	)

def login_dups():
	runTest(
		title="login_dups",
		url='user/login',
		method='post',
		expected=400,
		dataPath='usr/register-0.json', # testing for duplicates
		headers={"content-type":"application/json"},
	)

# XXX this is important
def login_with_bearer_token_post():
	runTest(
		title="login_with_bearer_token",
		url='user/login',
		method='post', # the poins is, the method should be post, and with bearer it fucks things up
		expected=400,
		dataPath='usr/register-0.json',
		headers={"content-type":"application/json"},
		bearer='usr/usr-0.json',
	)

def login_with_bearer_token_get():
	runTest(
		title="login_with_bearer_token",
		url='user/login',
		method='get', # the poins is, the method should be post, and with bearer it fucks things up
		expected=404,
		dataPath='usr/register-0.json',
		headers={"content-type":"application/json"},
		bearer='usr/usr-0.json',
	)


def login_non_existent():
	runTest(
		title="login_non_existent",
		url='user/login',
		expected=404,
		method='post',
		data={"uname":"fuck-you","passw":"fuck-you-even-more"},
		headers={"content-type":"application/json"},
	)

def login_wrong_passw():	
	runTest(
		title="login_wrong_passw",
		method='post',
		expected=400,
		url='user/login',
		data={"uname":"mr-test-0","passw":"fuck-you-even-more"},
		headers={"content-type":"application/json"}
	)

def login_wrong_uname():	
	runTest(
		title="login_wrong_uname",
		expected=404,
		method='post',
		url='user/login',
		data={"uname":"idontknowyou","passw":'LetUsF0-cukthingsUp!213ses'},
		headers={"content-type":"application/json"},
	)


#def login_flow():
#	login_non_existent();
#	login_wrong_passw();
#	login_wrong_uname();
#	login_correct(); # will also initialize user for other tests (optionally you can call user.init()
#	login_dups();
#	login_with_bearer_token(); # has the header

