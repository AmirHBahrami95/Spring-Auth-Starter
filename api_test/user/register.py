import utils
import user.typicals as tps
runTest=utils.runTest

def register(n=0,log=False,saveDir="usr"):

	""" 
		register users for testing and save their data to
		corresponding 'usr/register-n.json' file. 
		n is not seqeuential so if you give n=16 without having
		initialized n0-15 , it's ok.
		call user.cleanup() to reset the fuckery that have been
		done during this session
	"""
	
	# might happen (if u're a prick)
	if n<0:
		n=0
	
	if log:
		print(f"sending : {data}")
		# print(f"writing to {saveDir}/usr-{n}.json")
	
	runTest(
		title=f"register-{n}",
		expected=200,
		url='auth/register',
		method='post',
		data=tps.getTypicalRegister(n),
		headers={"content-type":"application/json"},
		dumpPath=f"{saveDir}/usr-{n}.json"
	)

def dups():
	runTest(
		title='register_dups',
		expected=400,
		url='auth/register',
		method='post',
		#dataPath='usr/register-0.json',
		data=tps.getTypicalRegister(0),
		headers={"content-type":"application/json"}
	)

def with_uid():
	# TODO test if with actual existing uid it still works or not 
	registerJ=utils.getJsonResource('usr/register-0.json')
	r2=utils.getJsonResource('usr/usr-0.json')['user']
	r2['passw']=registerJ['passw']
	runTest(
		title='register_with_uid',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def fake_uid():	
	r2=utils.getJsonResource('usr/register-1.json')
	r2['id']='a4e2711d-1172-4723-b84f-271f445830f1'
	runTest(
		title='register_fake_uid',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		# dumpPath='-',
		headers={"content-type":"application/json"},
	)

def no_passw():
	r2=utils.getJsonResource('usr/register-1.json')
	# r2['id']='a4e2711d-1172-4723-b84f-271f445830f1'
	del r2['passw']
	runTest(
		title='register_no_passw',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
	)

def no_email():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['personalInfo']['email']
	runTest(
		title='register_no_email',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		# dumpPath='-',
		headers={"content-type":"application/json"},
	)

def email_dups():
	r1=utils.getJsonResource('usr/register-0.json')
	r2=utils.getJsonResource('usr/register-1.json')
	r2['personalInfo']['email']=r1['personalInfo']['email']
	# print(r2)
	runTest(
		title='register_email_dups',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

	# "phone_no":""

def no_lname():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['personalInfo']['lname']
	runTest(
		title='register_no_lname',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def no_fname():
	r2=utils.getJsonResource('usr/register-1.json')
	del r2['personalInfo']['fname']
	runTest(
		title='register_no_fname',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def uname_dups():
	r1=utils.getJsonResource('usr/register-0.json')
	r2=utils.getJsonResource('usr/register-1.json')
	r2['uname']=r1['uname']
	runTest(
		title='register_uname_dups',
		expected=400,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)

def phone_provided():
	r2=utils.getJsonResource('usr/register-0.json')
	r2['uname']='this-is-a-test-nigga'
	r2['personalInfo']['email']='ohhellnuhbro@gmail.com'
	r2['personalInfo']['phoneNo']="+491781513772"
	# print(f"sending:{r2}")
	runTest(
		title='register_phone_provided',
		expected=200,
		url='auth/register',
		method='post',
		data=r2,
		headers={"content-type":"application/json"},
		# dumpPath='-'
	)


