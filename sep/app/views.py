from app import app
from flask import render_template, redirect, url_for, flash, request, jsonify, session

"""
#Check if user is needed to be logged in for a page:
def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'logged_in' in session:
            return f(*args, **kwargs)
        return redirect(url_for('login'))
    return decorated_function

#Check is already logged through sessions:
def login_present(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'logged_in' in session:
            return redirect(url_for('dashboard'))
        return f(*args, **kwargs)
    return decorated_function

"""

@app.route('/')
def index():
    #Just rendering login as a test
    return render_template("staffLogin.html")


# Logging function used for testing
import datetime
logging = 1
def log(*args):
    if logging:
        for line in args:
            if isinstance(line, str):
                with open('log.log', 'a') as the_file:
                    time = f"{datetime.datetime.now():%Y/%m/%d - %H:%M:%S}"
                    the_file.write("\n["+str(time)+"] "+line)
            elif debug:
                raise line
