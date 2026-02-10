x_domainname="domain_name"
x_appname="app_name"
x_dbname="db_name"
x_username="db_usr"
x_password="db_passwd"

# generated randomly already
x_keystore_password=$(tr -dc A-Za-z0-9 </dev/urandom | head -c 21)
