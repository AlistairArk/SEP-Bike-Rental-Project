from flask import Flask
from flask_mail import Mail, Message



def login(*args, **kwargs):
    username = kwargs.get("username", 0)
    password = kwargs.get("password", 0)

    return 1 # Assume username and password are valid as a test


def emailExists(email):
    # Perform database check to validate if input email exists
    # Return 1 if found and 0 if not found.

    return 1 # Assume email exists as a test



def resetRequest(*args, **kwargs):
    email = kwargs.get("email", 0)

    
    if emailExists(email):

        # Generate and send email

        return 1
    else:
        return 0 # Email could not be found
