import requests as rex
import utils
import user
import tkn
import personal_info as pi
# from . import tkn

# XXX NOTE please ALWAYS do the cleanup in each section BEFORE doing
# the tests in that section and use namespaces to save files from
# last testing round so that you can observe the data before running
# another test

# XXX ALSO NOTE that each test flow should take care of it's own 
# resources, so if you make new files and wanna clean them up, 
# do that inside a local cleanup() method

# utils.mkdirP("/shit/wank/deep/fuck")
# utils.mkdirP("/shit/wank/deep/jack")

user.flow()
pi.flow()
tkn.flow()
