from app import app, function
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
    # Just rendering login as a test
    return render_template("staffLogin.html")


@app.route('/resetPassword')
def webResetPassword():
    return render_template("resetPassword.html")


@app.route('/resetRequest', methods=['POST'])
def webResetRequest():
    # Send a password reset email to user
    email = str(request.form['email'])

    if function.resetRequest(email=email):
        message = "A reset link has been sent to to the provided email."
        return render_template("resetPassword.html", success = message)
    else:
        message = "Invalid email provided. Please try again."
        return render_template("resetPassword.html", fail = message)


@app.route('/login', methods=['POST'])
def webLogin():
    # Hand username and password to login function
    # Return 1 if valid login is found
    username = str(request.form['username'])
    password = str(request.form['password'])

    loginData = function.login(username=username, password=password)


    if loginData[0]:
        session["userType"] = loginData[1]
        session["username"] = loginData[2]
        session["name"] = loginData[3]
        session["loggedIn"] = True
        return webIndex()
    else:
        session["loggedIn"] = False
        message = "Error: The User Name or Password entered is incorrect. Please try again."
        return render_template("staffLogin.html", message = message)


@app.route('/index')
def webIndex():
    if session["loggedIn"]:
        return render_template("index.html", name = session["name"])
    else:
        message = "Error: You must be logged in to peform that action."
        return render_template("staffLogin.html", name = message)


@app.route('/logout', methods=['POST'])
def webLogout():
    session["loggedIn"] = False
    session["userType"] = None
    session["username"] = None
    session["name"] = None
    return render_template("index.html", name = session["name"])


@app.route('/addEmployee', methods=['POST'])
def webAddEmployee():
    # Take employee details from add employee form and add the new employee

    name      = str(request.form['name'])
    username  = str(request.form['username'])
    email     = str(request.form['email'])
    password  = str(request.form['password'])
    phone     = str(request.form['phone'])


    # Call add employee function
    result = function.addUser(name=name, username=username, email=email, password=password, phone=phone, userType="employee" )

    message = "Error: The User Name or Password entered is incorrect. Please try again."
    return render_template("staffLogin.html", message = message)



# # Logging function used for testing
# import datetime
# logging = 0
# def log(*args):
#     if logging:
#         for line in args:
#             if isinstance(line, str):
#                 with open('log.log', 'a') as the_file:
#                     time = f"{datetime.datetime.now():%Y/%m/%d - %H:%M:%S}"
#                     the_file.write("\n["+str(time)+"] "+line)
#             elif debug:
#                 raise line
