from app import app
from flask import render_template, redirect, url_for, flash, request, jsonify, session


'''
#Check if user is needed to be logged in for a page:
def loginRequired(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'logged_in' in session:
            return f(*args, **kwargs)
        return redirect(url_for('login'))
    return decorated_function

#Check is already logged through sessions:
def loginPresent(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'logged_in' in session:
            return redirect(url_for('dashboard'))
        return f(*args, **kwargs)
    return decorated_function
'''

@app.route('/')
def index():
    #Just rendering login as a test
    return render_template("staffLogin.html")



@app.route('/resetPassword')
def resetPassword():
    return render_template("resetPassword.html")

@app.route('/resetRequest', methods=['POST'])
def resetRequest():
    '''Send an password email to a user given an input email''' 
    email = str(request.form['email']) # Takes input password from web-page   

    log("Reset Password Test")
    log("Email: "+ email)

    # # Perform database check to validate if input email exists? 

    # # Send reset email

    return render_template("staffLogin.html")


@app.route('/login', methods=['POST'])
def login():
    log("test")
    username = str(request.form['username']) # Takes input username from web-page 
    password = str(request.form['password']) # Takes input password from web-page   
    
    log("Login Test")
    log("Username: "+ username)
    log("Password: "+ password+"\n")

    # Hand username and password to login function
    # Return 1 if valid login is found
    validLogin = 1 # Assume valid login as a test
    

    if validLogin:
        session["log_in_fail"] = False
        session["logged_in"] = True
        return render_template("index.html")

    else:
        session["log_in_fail"] = True   # Variable used to display "incorrect username or password entered" message
        return render_template("resetPassword.html")






# Logging function used for testing
import datetime
logging = 0
def log(*args):
    if logging:
        for line in args:
            if isinstance(line, str):
                with open('log.log', 'a') as the_file:
                    time = f"{datetime.datetime.now():%Y/%m/%d - %H:%M:%S}"
                    the_file.write("\n["+str(time)+"] "+line)
            elif debug:
                raise line


