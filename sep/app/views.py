from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from functools import wraps
from .forms import *
import json



import datetime

###############   LOG IN ROUTES   ##############################################

# #Check if user is needed to be logged in for a page:
def loginRequired(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        if 'loggedIn' in session:
            return f(*args, **kwargs)

        else:
            return redirect(url_for('webLogin'))

    return decorated

# #Check is already logged through sessions:
def loginPresent(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        if 'loggedIn' in session:
            return redirect(url_for('webIndex'))
        else:
            return f(*args, **kwargs)

    return decorated


@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('webLogin'))


@app.route('/')
@loginPresent
def webLogin():
    return render_template("staffLogin.html")

@app.route('/index')
@loginRequired
def webIndex():
    return render_template("index.html", name = session["name"])


@app.route('/resetPassword')
@loginPresent
def webResetPassword():
    return render_template("resetPassword.html")

@app.route('/resetRequest', methods=['POST'])
@loginPresent
def webResetRequest():
    # Send a password reset email to user
    email = str(request.form['email'])

    if function.resetRequest(email=email):
        message = "A reset link has been sent to to the provided email."
        return render_template("resetPassword.html", success = message)
    else:
        message = "Invalid email provided. Please try again."
        return render_template("resetPassword.html", fail = message)


@app.route('/loginRequest', methods=['POST'])
def webLoginRequest():
    # Hand username and password to login function
    # Return 1 if valid login is found
    username = str(request.form['username'])
    password = str(request.form['password'])

    loginData = function.login(username=username, password=password)


    # log(str(loginData))

    if loginData[0]:
        session["userType"] = loginData[1]
        session["username"] = loginData[2]
        session["name"] = loginData[3]
        session["loggedIn"] = True
        return redirect(url_for('webIndex'))
    else:
        message = "Error: The User Name or Password entered is incorrect. Please try again."
        return render_template("staffLogin.html", message = message)

###############   END OG LOG IN ROUTES   #######################################



###############   ADD USER ROUTES   ############################################

@app.route('/addUser',methods=['GET','POST'])
@loginRequired
def addUser():
    form=addUserForm(request.form)
    if request.method=='POST' and form.validate_on_submit():
        usertype="customer"
        name=form.name.data
        email=form.email.data
        phone=form.phone.data
        username=form.username.data
        password=form.password.data
        e = models.User(name=name,
                        email=email,
                        phone=phone,
                        username=username,
                        password=password,
                        user_type=usertype)
        db.session.add(e)
        db.session.commit()
        flash("User added!")
    return render_template('addUser.html',
                            form=form)


###############   END OF ADD USER ROUTES   #####################################



###############   ADD BIKES ROUTES   ###########################################


@app.route('/addBikes',methods=['GET','POST'])
@loginRequired
def addBikes():
    form=addBikesForm(request.form)
    form.location.choices=[(l.id,l.name) for l in models.Location.query.all()]
    if request.method=="POST" and form.validate_on_submit():
        amount = form.amount.data
        location = form.location.data

        l = models.Location.query.get(location)
        max = l.max_capacity
        bike_amount = l.bike_amount
        amount_added=0

        for i in range(amount):
            if bike_amount<max:
                bike=models.Bike(location_id=location,in_use=0,status="new")
                db.session.add(bike)
                db.session.commit()
                amount_added+=1
                bike_amount+=1
        l = models.Location.query.get(location)
        l.bike_amount+=amount_added
        db.session.add(l)
        db.session.commit()
        if amount_added<amount:
            message = "Location full."+str(amount_added)+"/"+str(amount)+" bikes added."
            flash(message)
        else:
            flash("All bikes successfully added!")

    return render_template('addBikes.html',
                            form=form)

###############   END OF ADD BIKES ROUTES   ####################################



###############   ADD EMPLOYEE ROUTES   ########################################

@app.route('/addEmployee',methods=['GET','POST'])
@loginRequired
def addEmployee():
    form=addUserForm(request.form)
    if request.method=='POST' and form.validate_on_submit():
        usertype="employee"
        name=form.name.data
        email=form.email.data
        phone=form.phone.data
        username=form.username.data
        password=form.password.data
        e = models.User(name=name,
                        email=email,
                        phone=phone,
                        username=username,
                        password=password,
                        user_type=usertype)
        db.session.add(e)
        db.session.commit()
        flash("Employee added!")
    return render_template('addEmployee.html',
                            form=form)


###############   END OF ADD EMPLOYEE ROUTES   #################################



###############   ADD LOCATION ROUTES   ########################################

@app.route('/addLocation',methods=['GET','POST'])
@loginRequired
def addLocation():
    form=addLocationForm(request.form)
    if request.method=="POST" and form.validate_on_submit():
        flash("Location added!")
        name=form.name.data
        addr=form.addr.data
        max_capacity=form.max_capacity.data
        longt=float(form.longt.data)
        latt=float(form.latt.data)
        l = models.Location(name=name,
                            bike_amount=0,
                            max_capacity=max_capacity,
                            addr=addr,
                            longt=longt,
                            latt=latt)
        db.session.add(l)
        db.session.commit()

    return render_template('newLocation.html',
                            form=form)


###############   END OF ADD LOCATION ROUTES   #################################



###############   LOCATION STATS ROUTES   ######################################

@app.route('/locationStats')
@loginRequired
def locationStats():
    locations = models.Location.query.all()
    return render_template('locationStats.html',
                            locations=locations)

################# END OF LOCATION STATS ROUTES ##################


################## CREATE BOOKING ROUTES #########


@app.route('/newBooking', methods=['GET','POST'])
@loginRequired
def newBooking():
    form=addBookingForm()

    form.slocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    form.elocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    if request.method=="POST" and form.validate_on_submit():
        email = form.email.data
        stime=request.form['stime']
        etime=request.form['etime']
        slocation = form.slocation.data
        elocation = form.elocation.data
        numbikes = form.numbikes.data
        # if user is not None:
        message = createBooking(email,stime,etime,slocation,elocation,numbikes)
        flash(message)
    return render_template("newBooking.html", form=form)

def createBooking(email,stime,etime,slocation,elocation,numbikes):
    user = models.User.query.filter_by(email=email).first()
    cost = 13.44
    bookingTime = datetime.datetime.now()
    startloc = models.Location.query.filter_by(id=slocation).first()
    sdatetime = datetime.datetime.strptime(stime,"%Y-%m-%dT%H:%M")
    edatetime = datetime.datetime.strptime(etime,"%Y-%m-%dT%H:%M")

    b = models.Booking( cost= cost,
                        start_time=sdatetime,
                        end_time=edatetime,
                        bike_amount=numbikes,
                        booking_time= bookingTime,
                        paid=False,
                        user_id= user.id,
                        end_location=elocation,
                        start_location=startloc.id
                        )

    db.session.add(b)
    db.session.commit()

    message="Booking successfully created! Booking confirmation has been emailed to "+email+"."

    return message

@app.route('/')
@loginRequired
def index():
    return render_template("index.html")

def takeBike(bike_id,booking_id):
    bike = models.Bike.query.get(bike_id)
    bike.in_use=True
    booking = models.Booking.query.get(booking_id)
    booking.bikes.append(bike)
    db.session.add(bike)
    db.session.add(booking)
    db.session.commit()

def returnBike(bike_id,booking_id):
    bike = models.Bike.query.get(bike_id)
    bike.in_use=False
    db.session.add(bike)
    db.session.commit()


######## API

import datetime
#def log(*args):
#    for line in args:
#       with open('log.log', 'a') as the_file:
#           time = f"{datetime.datetime.now():%Y/%m/%d - %H:%M:%S}"
#           the_file.write("\n["+str(time)+"] "+line)

def api_loginRequired(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'api_logged_in' in session:
            return f(*args, **kwargs)
        return jsonify({'error': 'Authentication failed'})
    return decorated_function

@app.route('/api/login', methods=['POST'])
def apiLogin():
    """
    Handles json request for login, provides authentication.
    updates cookies if details are correct.
    return json responce
    """

    content = request.get_json(force=True)

    username = content['username']
    password = content['password']

    loginData = function.login(username=username, password=password)

    if loginData[0]:
        session["api_logged_in"] = True
        session["username"] = loginData[2]
        session["userType"] = loginData[1]
        session["name"] = loginData[3]

        user = models.User.query.filter_by(username=username).first()
        session["userId"] = user.id

        data =  {
                "username":"",
                "password":"",
                "status":"Login Accepted"
            }

    else:
        data =  {
            "username":"",
            "password":"",
            "status":"Incorrect Login Information"
        }


    jsonifiedData = json.dumps(data)
    return jsonifiedData


@app.route('/api/register', methods=['POST'])
def apiRegister():
    """
    Attempt to create a new account for the request
    return the message to the device depending on the outcome
    """

    content = request.get_json(force=True)

    username = content['username']
    password = content['password']
    email = content['email']
    phone = content['phone']
    name = content['username']   #!!!!!! add name field to register activity
    usertype="customer"

    if validate_email(email):
        data =  {
                "registrationStatus":"That email is taken"
                }
        jsonifiedData = json.dumps(data)
        return jsonifiedData
    elif validate_username(username):
        data =  {
                "registrationStatus":"That username is taken"
                }
        jsonifiedData = json.dumps(data)
        return jsonifiedData
    else:
        data =  {
                "registrationStatus":"User Registered"
                }

    e = models.User(name=name,
                    email=email,
                    phone=phone,
                    username=username,
                    password=password,
                    user_type=usertype)
    db.session.add(e)
    db.session.commit()

    jsonifiedData = json.dumps(data)
    return jsonifiedData

def validate_username(username):
    for u in User.query.all():
        if username.data==u.username:
            return True
        else:
            return False

def validate_email(email):
    for u in User.query.all():
        if email.data==u.email:
            return True
        else:
            return False

@app.route('/api/getlocations', methods=['GET'])
def apiGetLocations():
    """
    Returns all the locations bikes can be taken out from
    Also returns number of bikes available
    """

    data = []

    locations = models.Location.query.all()

    for location in locations:
        locData =  {
                "name":str(location.name),
                "latitude":str(location.latt),
                "longitude":str(location.longt),
                "bikesAvailable":str(location.bike_amount)
            }

        data.append(locData)

    jsonifiedData = json.dumps(data)
    return jsonifiedData

@app.route('/api/booking', methods=['POST'])
@api_loginRequired
def apiBooking():
    """
    Creates a booking by invoking the create booking function
    Handle card details
    """
    content = request.get_json(force=True)

    username = content['username']
    password = content['password']
    email = content['email']
    phone = content['phone']
    name = content['username']   #!!!!!! add name field to register activity
    usertype="customer"

    message = createBooking(email,stime,etime,slocation,elocation,numbikes)

    json = request.get_json()
    return jsonify({'error': 'Authentication failed'})


@app.route('/api/getOrders', methods=['POST'])
@api_loginRequired
def apiGetOrders():
    """
    Returns all orders tied to a specific account
    """
    userid = session['userId']
    orders=models.Booking.query.filter_by(user_id=userid).all()

    data = []

    for order in orders:
        returned = False
        bikeNumber = 0
        locations = models.Location.query.filter_by(id=order.start_location).first()
        for bike in order.bikes:
            if bike.in_use:
                bikeNumber += 1
                returned = True

        orderData = {
            "id":str(order.id),
            "cost":str(order.cost),
            "startTime":str(order.start_time),
            "endTime":str(order.end_time),
            "bikeNumber":str(bikeNumber),
            "location":str(location.name),
            "bikesReturned":str(returned)
        }
        data.append(orderData)

    print(data)

    jsonifiedData = json.dumps(data)
    return jsonifiedData

@app.route('/api/collectbikes', methods=['POST'])
@api_loginRequired
def apiCollectBikes():
    """
    Marks a bike as unavailable
    """

    json = request.get_json()
    return jsonify({'error': 'Authentication failed'})

@app.route('/api/returnBike', methods=['POST'])
@api_loginRequired
def apiReturnBike():
    """
    Marks a bike as available
    """

    json = request.get_json()
    return jsonify({'error': 'Authentication failed'})


@app.route('/api/logout', methods=['POST'])
@api_loginRequired
def apiLogout():

    session.clear()
    return jsonify({'message': 'Complete'})
