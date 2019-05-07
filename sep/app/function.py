from flask import Flask
from flask_mail import Mail, Message

import MySQLdb
from . import db, models
from passlib.hash import sha256_crypt



def login(*args, **kwargs):
    username = kwargs.get("username", 0)
    password = kwargs.get("password", 0)


    # Check if username exists
    user = models.User.query.filter_by(username=username).first()

    # If user not found, or, passwords don't match
    if user==None or sha256_crypt.verify(password, user.password)==False:
        return [False, 0, 0, 0]

    else: # confirm valid user
        return [True, user.user_type, user.username, user.name]  # return user type





def resetRequest(*args, **kwargs):
    email = kwargs.get("email", 0)
    if emailExists(email):
        # Generate and send email
        return 1
    else:
        return 0 # Email could not be found
