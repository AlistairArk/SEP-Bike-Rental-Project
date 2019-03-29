from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from functools import wraps
from .forms import *


#Check if user is needed to be logged in for a page:
def loginRequired(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'loggedIn' in session:
            return f(*args, **kwargs)
        return redirect(url_for(''))
    return decorated_function

#Check is already logged through sessions:
def loginPresent(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'loggedIn' in session:
            return redirect(url_for('index'))
        return f(*args, **kwargs)
    return decorated_function


# @loginPresent
@app.route('/')
def index():
    # Just rendering login as a test
    return render_template("staffLogin.html")




@loginRequired
@app.route('/resetPassword')
def webResetPassword():
    return render_template("resetPassword.html")

@loginRequired
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

@loginRequired
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
        return redirect(url_for('index'))
    else:
        session["loggedIn"] = False
        message = "Error: The User Name or Password entered is incorrect. Please try again."
        return render_template("staffLogin.html", message = message)

@loginRequired
@app.route('/index')
def webIndex():
    return render_template("index.html", name = session["name"])



@loginRequired
@app.route('/logout')
def webLogout():
    session["loggedIn"] = False
    session["userType"] = None
    session["username"] = None
    session["name"] = None
    return render_template("staffLogin.html")



### ### ###

@loginRequired
@app.route('/addUser',methods=['GET','POST'])
def addUser():
    form=addUserForm(request.form)
    return render_template('addUser.html',
                            form=form)

@loginRequired
@app.route('/userAdded',methods=['GET','POST'])
def userAdded():
    if request.method == 'POST':
        userInfo = request.form
        usertype = "customer"
        for key,value in userInfo.items():
            if key=='name':
                name=value
            elif key=='email':
                email=value
            elif key=='phone':
                phone=value
            elif key=='username':
                username=value
            elif key=='password':
                password=value
        u = models.User(name=name,
                            email=email,
                            phone=phone,
                            username=username,
                            password=password,
                            user_type=usertype)
        db.session.add(u)
        db.session.commit()
        return render_template('userAdded.html')









@loginRequired
@app.route('/bikesAdded',methods=['GET','POST'])
def bikesAdded():
    # bikeForm=addBikesForm(request.form)
    if request.method == 'POST':
        bikeInfo = request.form
        for key,value in bikeInfo.items():
            if key=='amount':
                amount=int(value)
            elif key=='location':
                locationid=int(value)
        l = models.Location.query.get(locationid)
        max = l.max_capacity
        bike_amount = l.bike_amount
        amount_added=0
        for i in range(amount):
            if bike_amount<max:
                bike=models.Bike(location_id=locationid,in_use=0,status="new")
                db.session.add(bike)
                db.session.commit()
                amount_added+=1
                bike_amount+=1
        l = models.Location.query.get(locationid)
        l.bike_amount+=amount_added
        db.session.add(l)
        db.session.commit()
        if amount_added<amount:
            all_added=False
            message = "Location full."+amount_added+"/"+amount+" bikes added."
            flash(message)
        else:
            all_added=True
            flash("All bikes successfully added!")
        return render_template('bikesAdded.html',
                                amount=amount,
                                amount_added=amount_added,
                                all_added=all_added,
                                location=l.name)


@loginRequired
@app.route('/addBikes',methods=['GET','POST'])
def addBikes():
    # bikes=models.Bike.query.all()
    form=addBikesForm(request.form)
    form.location.choices=[(l.id,l.name) for l in models.Location.query.all()]
    # form.location.choices=[(1,'Leeds'),(2,'Manchester'),(3,'Newcastle'),(4,'Durham'),(5,'Sheffield')]
    flash("hello")
    # if form.validate_on_submit():
    #     amount = form.amount.data
    #     location = form.location.data

        # flash("Successfully received form data: %s at %s"%(form.amount.data,form.location.data))
        # return render_template('dbtesting.html',
        #                         amount=amount,
        #                         location=location)
    return render_template('addBikes.html',
                            form=form)

#
# @app.route('/addEmployee',methods=['GET','POST'])
# def addEmployee():
#     form = addEmployeeForm(request.form)
#     return render_template('addEmployee.html', form = from)
#
#
# @app.route('/employeeAdded',methods=['GET','POST'])
# def employeeAdded():
#     # return render_template('employeeAdded.html')
#     if request.method == 'POST':
#         userInfo = request.form
#         usertype = "employee"
#         for key,value in userInfo.items():
#             if key=='name':
#                 name=value
#             elif key=='email':
#                 email=value
#             elif key=='phone':
#                 phone=value
#             elif key=='username':
#                 username=value
#             elif key=='password':
#                 password=value
#         u = models.User(name=name,
#                             email=email,
#                             phone=phone,
#                             username=username,
#                             password=password,
#                             user_type=usertype)
#         db.session.add(u)
#         db.session.commit()
#         return render_template('employeeAdded.html')


@loginRequired
@app.route('/addLocation',methods=['GET','POST'])
def addLocation():
    form=addLocationForm(request.form)
    return render_template('newLocation.html',
                            form=form)



@loginRequired
@app.route('/locationAdded',methods=['GET','POST'])
def locationAdded():
    if request.method == 'POST':
        locationInfo = request.form
        for key,value in locationInfo.items():
            if key=='name':
                name=value
            elif key=='addr':
                addr=value
            elif key=='max_capacity':
                max_capacity=value
            elif key=='longt':
                longt=float(value)
            elif key=='latt':
                latt=float(value)
        l = models.Location(name=name,
                            bike_amount=0,
                            max_capacity=max_capacity,
                            addr=addr,
                            longt=longt,
                            latt=latt)
        db.session.add(l)
        db.session.commit()
        flash("Location added!")
        return render_template('locationAdded.html',
                                name=name)


@loginRequired
@app.route('/locationStats')
def locationStats():
    locations = models.Location.query.all()
    # locations = [[1,'leeds','123 house',5],[2,'headingley','44 drive',6],[3,'burley','77 street',9]]
    return render_template('locationStats.html',
                            locations=locations)
