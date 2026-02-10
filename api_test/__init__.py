""" 
	This is how to do tests using this framework:
	this framework helps you only test rest-api and does so with the help of reading/writing
	json data to subdirectories.

	It's structured hirearchical and each module has a flow() method who calls it's submodule's
	flow() methods. each submodule's flow is responsible for initializing AND cleaning up it's
	own generated data (in form of json files)
	---
	every module has some methods in it who are called inside it's flow() method. 

	in each module there are test methods, who are called inside that module's flow()
	method

	those test methods' names are supposed to demostrate their test purpose
	---
	all test methods use utils module's methods, maily the runTest to well...run tests!
	go ahead and read that method's docstring and look at some written tests to have an idea
	of how it works.

	also modules may use methods from other (sub-)modules. for that reason, some modules may 
	have methods with default arguments, and those arguments might not necessarily make sense
	inside that module itself, but from the perspective of other modules using it, it makes
	perfect sense.

	for instance the module user.promote has a method "to_admin" and it has a "saveDir='usr'"
	but inside the user.promote's flow() method, you will give saveDir='promote'. the reason
	is what I stated above. it's bc maybe other modules may need to promote a user they 
	initialized for test reasons, so we let the default argument have 'usr' for the sake of 
	brevity (if anything).
	---
	all (sub-)modules need to use the user module's initUsers() initAdmin() and cleanup() data
	along with utils.cleanFiles() to clean their own session data

	so for instance personal_info modules is NOT supposed to read/write tkn's data. therefore
	both tkn and personal_info need to initialize and clean their own data.
	---
	some conventions:

		1.at the start of each flow() method, first print the module's name.
		then cleanup the data before initializing any user whatsoever

			--this it to make sure each session leaves some data behind for debug purposes, and
			only cleans them up when a new session begins

		2.only write in-memory data to files, if you are gonna use them again in another test
		or read their data afterwards. 

			--if however you just wanna take a peak, you can optionally just set dumpPath='-'
			to read it on the stdout while the test is being executed and save up some space.
			this will, however not write ANY data to file, so you may not use it again or peak
			at it once the tests are done.

		anyhow, the choice is yours
	
		3.at best keep user data away from other module's data. this'll make things easier
		and frankly i have NOT yet tested if users can be seamlessly written to the same 
		sub-directories as their initializing modules. 

			--so for instance instead of saving an admin user under 'personal_info/' subdir, 
			just save it under the same old 'usr/' dir

			--by 'save it' I mean just don't temper with user.initUsers or user.initAdminUser 
			or even user.cleanup data

		4.keep saving the admin user under usr/usr-255.json file and only interact with it
		via user.initAdmin() and user.logoutAdmin() or user.cleanup(initAdmin=True) which in
		turn cleans admin data too.

			--and know this: admin user is never registered (fucking DUH), you have to initialize
			your guy via spring's data.sql 
	
	---
	final note: if the framework doesn't make sense, you have to read the runTest docstring then
	a lot of behvaiours described above make perfect sense. 

	CHEERS!
"""
