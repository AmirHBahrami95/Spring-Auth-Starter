import utils
runTest=utils.runTest

def register(n=0):

	""" 
		register users for testing and save their data to
		corresponding 'usr/register-n.json' file. 
		n is not seqeuential so if you give n=16 without having
		initialized n0-15 , it's ok.
		call user.cleanup() to reset the fuckery that have been
		done during this session
	"""

	data=dict()
	data['uname']=f"mr-test-{n}"
	data['passw']=f"iamtest{n}"
	data['fname']=f"mr-{n}"
	data['lname']=f"test-{n}"
	data['email']=f"mr-test{n}@gmail.com"

	runTest(
		title=f"register-{n}",
		expected=200,
		url='user/register',
		method='post',
		data=data,
		headers={"content-type":"application/json"},
		dumpPath=f"usr/usr-{n}.json"
	)

def register_dups():
	runTest(
		title='register_dups',
		expected=400,
		url='user/register',
		method='post',
		dataPath='usr/register-0.json',
		headers={"content-type":"application/json"}
	)

def register_with_uid():
	registerJ=utils.getJsonResource('usr/register-0.json')
	whoami=utils.getJsonResource('usr/whoami.json')
	whoami['passw']=registerJ['passw']
	runTest(
		title='register_with_uid',
		expected=400,
		url='user/register',
		method='post',
		data=whoami,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def register_fake_uid():	
	r2=utils.getJsonResource('usr/register-1.json')
	r2['id']='a4e2711d-1172-4723-b84f-271f445830f1'
	runTest(
		title='register_fake_uid',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		# dumpPath='-',
		headers={"content-type":"application/json"},
	)

def reg_no_passw():
	r2=utils.getJsonResource('usr/register-1.json')
	# r2['id']='a4e2711d-1172-4723-b84f-271f445830f1'
	del r2['passw']
	runTest(
		title='register_no_passw',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
	)

def reg_no_email():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['email']
	runTest(
		title='register_no_email',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		dumpPath='-',
		headers={"content-type":"application/json"},
	)

def reg_email_dups():
	r1=utils.getJsonResource('usr/register-0.json')
	r2=utils.getJsonResource('usr/register-1.json')
	r2['email']=r1['email']
	print(r2)
	runTest(
		title='register_email_dups',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		dumpPath='-'
	)

	# "phone_no":""

def reg_no_lname():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['lname']
	runTest(
		title='register_no_lname',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def reg_no_fname():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['fname']
	runTest(
		title='register_no_fname',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def reg_uname_dups():
	r1=utils.getJsonResource('usr/register-0.json')
	r2=utils.getJsonResource('usr/register-1.json')
	r2['uname']=r1['uname']
	runTest(
		title='register_uname_dups',
		expected=400,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def reg_phone_provided():
	r2=utils.getJsonResource('usr/register-1.json')
	r2['phone_no']="+491781513772"
	runTest(
		title='register_phone_provided',
		expected=200,
		url='user/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

