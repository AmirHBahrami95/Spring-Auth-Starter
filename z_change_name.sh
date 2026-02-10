# this script initializes a working spring application with jwt authentication,
# ready to be developed, tested and deployed. it's a monolith but with a few 
# tweaks you may be able to use it as a microservice, too!

# reading creds
source ./creds.sh

echo "x_domain_name: $x_domainname"
echo "x_appname: $x_appname"
echo "x_dbname: $x_dbname"
echo "x_username: $x_username"
echo "x_password: $x_password"
echo "x_keystore_password: $x_keystore_password"

echo "do you confirm? (y/n)" 
confirm=$(python3 -c "print(input())")

if [[ "$confirm" == "n" || "$confirm" == "N" ]] ; then
	echo "ok then; edit 'creds.sh' then run this script again."
else 

	keytool -genkeypair \
	  -keystore ./src/main/resources/jwt-keystore.jks \
	  -alias client-cert \
	  -keyalg RSA \
	  -keysize 2048 \
	  -validity 365 \
	  -storepass $x_keystore_password \
	  -keypass $x_keystore_password \
	  -dname "CN=cn,OU=ou,O=o,L=l,ST=st,C=us" \
	  -noprompt
		
	function changeName(){
		grep --color=NEVER -HR "$1" 2>/dev/null | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/$1/$2/g"
	}
	
	# replacing package names
	changeName XX_DOMAIN_NAME $x_domainname
	changeName XX_APP_NAME $x_appname

	# doing the same with pom.xml (didn't work with changeName)
	sed -i "s/XX_DOMAIN_NAME/$x_domainname/g" pom.xml

	# replacing db creds
	changeName XX_APP_DBNAME $x_dbname
	changeName XX_APP_USERNAME $x_username
	changeName XX_APP_PASSWORD $x_password
	
	# replacing keystore password
	changeName XX_KEYSTORE_PASSWORD $x_keystore_password

	# changing app directory
	dirs=$(find . -name \*XX_APP_NAME\* | xargs realpath )
	for d in $dirs; do
		destname=$(echo $d| sed "s/XX_APP_NAME/$x_appname/")
		mv $d $destname
	done

	# changing domain directory
	dirs=$(find . -name \*XX_DOMAIN_NAME\* | xargs realpath )
	for d in $dirs; do
		destname=$(echo $d| sed "s/XX_DOMAIN_NAME/$x_domainname/")
		mv $d $destname
	done
	
	# cleaning
	rm -f z_change_name.sh
	rm -f creds.sh
	rm -rf .git

	# some advice for after the mess
	echo
	echo "some followup things to do for the app to run:"
	echo "1. install redis (optionally change it's credentials in application.properties)"
	echo "2. install mariadb (optionally install an alternative but change change the driver and db path in application.properties)"
	echo "3. launch the database and execute the init_db.sql ('source [path/to/script]' command in mariadb)"
	echo "4. integrate project lombok in your IDE or set it up with maven"
	echo "5. run the spring boot application"
	echo "6. cd 'api_test'"
	echo "7. read the '__init__.py' file"
	echo "8. if you are impatient, just 'python3 .'  while inside the 'api_test' directory"
	echo
	echo "*9. DON'T FORGET TO initialize this directory as a git repository!"
	echo
	echo "--cheers!"

fi
