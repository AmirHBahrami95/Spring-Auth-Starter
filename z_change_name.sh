# this script initializes a new spring application with correct names ready 
# to be deployed with {config,discovery,gateway}-services (along with anything new)
# having changed their appname and db creds for the new environment

# reading creds
source ./creds.sh

echo "x_domain_name: $x_domainname"
echo "x_appname: $x_appname"
echo "x_dbname: $x_dbname"
echo "x_username: $x_username"
echo "x_password: $x_password"

echo "do you confirm? (y/n)" 
confirm=$(python3 -c "print(input())")

if [[ "$confirm" == "n" || "$confirm" == "N" ]] ; then
	echo "ok then. edit 'creds.sh' if you want"
else 
	
	# inline editing everything
	# grep -Hnre "XX_APP" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq
	# TODO 'com.' in com.$x_domainname should be dealt with in a healthy way! (or not, who gives a fuck)
	grep --color=NEVER -Hr "XX_DOMAIN_NAME" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/XX_DOMAIN_NAME/com.$x_domainname/g"
	grep --color=NEVER -Hr "XX_APP_NAME" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/XX_APP_NAME/$x_appname/g"
	grep --color=NEVER -Hr "XX_APP_USERNAME" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/XX_APP_USERNAME/$x_username/g"
	grep --color=NEVER -Hr "XX_APP_PASSWORD" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/XX_APP_PASSWORD/$x_password/g"
	grep --color=NEVER -Hr "XX_APP_DBNAME" | awk '{print $1}' | awk 'BEGIN{FS=":";} {print "./" $1}' | uniq | xargs sed -i "s/XX_APP_DBNAME/$x_dbname/g"

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
	rm -rf z_change_name.sh
	rm -rf creds.sh
	rm -rf .git

fi
