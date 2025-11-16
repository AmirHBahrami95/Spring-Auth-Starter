import requests as rex
import utils
import user

user.flow()
# user.initUsers()
print('-----user-cleanup-----')
user.cleanup()
