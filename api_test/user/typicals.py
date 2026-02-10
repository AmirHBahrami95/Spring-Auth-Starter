def getPersonalInfoDto(fname,lname,email,phoneNo,birthDate):
	pi=dict()
	pi['fname']=fname
	pi['lname']=lname
	pi['email']=email
	pi['phoneNo']=phoneNo
	pi['birthDate']=birthDate
	return pi

def getAddressDto(countryIso2,state,city,local):
	ad=dict()
	ad['countryIso2']=countryIso2
	ad['state']=state
	ad['city']=city
	ad['local']=local
	return ad

def getRegisterDto(uname,passw,personalInfo,address,id=None):
	rd=dict()
	rd['uname']=uname
	rd['passw']=passw
	rd['personalInfo']=personalInfo
	rd['personalInfo']['address']=address
	return rd

def getTypicalPersonalInfo(n=0):
	return getPersonalInfoDto(
		fname=f"some dude",
		lname=f"test face",
		email=f"mr-test{n}@gmail.com",
		phoneNo=f"+491781513772",
		birthDate=f"2000-01-20"
	)

def getTypicalAddress(n=0):
	return getAddressDto(
		countryIso2='IR',
		state='Greater Tehran',
		city='Tehran',
		local=f"avesina alley, number 24-{n}"
	)

def getTypicalRegister(n=0):	
	return getRegisterDto(
		uname=f"this-is-a-test-{n}",
		passw=f"iamtest{n}",
		personalInfo=getTypicalPersonalInfo(n),
		address=getTypicalAddress(n)
	)

