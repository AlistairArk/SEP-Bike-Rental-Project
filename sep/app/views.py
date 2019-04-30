from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from .forms import *
import json

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

'''
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
    if function.login(username=username, password=password):
        session["loggedIn"] = True
        return render_template("index.html")
    else:
        session["loggedIn"] = False
        message = "Error: The User Name or Password entered is incorrect. Please try again."
        return render_template("staffLogin.html", message = message)





def appLogin(*args):
    pass

def appResetPassword(*args):
    pass








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
'''

@app.route('/addUser',methods=['GET','POST'])
def addUser():
    form=addUserForm(request.form)
    return render_template('addUser.html',
                            form=form)

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

@app.route('/addEmployee',methods=['GET','POST'])
def addEmployee():
    return render_template('addEmployee.html')

@app.route('/addLocation',methods=['GET','POST'])
def addLocation():
    form=addLocationForm(request.form)
    return render_template('newLocation.html',
                            form=form)

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

@app.route('/locationStats')
def locationStats():
    locations = models.Location.query.all()
    # locations = [[1,'leeds','123 house',5],[2,'headingley','44 drive',6],[3,'burley','77 street',9]]
    return render_template('locationStats.html',
                            locations=locations)


######## API 

def api_loginRequired(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'api_logged_in' in session:
            return f(*args, **kwargs)
        return jsonify({'error': 'Authentificaton failed'})
    return decorated_function

@app.route('/api/login', methods=['POST'])
def apiLogin():
    """
    Handles json request for login, provides authentification.
    updates cookies if details are correct.
    return json responce
    """

    json = request.get_json()

    if json['username'] == 'admin' and json['password'] == 'password':
        return jsonify({'message': 'Success'})

    return jsonify({'error': 'Authentificaton failed'})


@app.route('/api/register', methods=['POST'])
def apiRegister():
    """
    Attempt to create a new account for the request
    return the message to the device depending on the outcome
    """

    json = request.get_json()

    if json['username'] == 'admin' and json['password'] == 'password':
        return jsonify({'message': 'Success'})

    return jsonify({'error': 'Authentificaton failed'})

@app.route('/api/getlocations', methods=['POST'])
def apiGetLocations():
    """
    Returns all the locations bikes can be taken out from
    Also returns number of bikes available
    """

    json = request.get_json()
    return jsonify({'error': 'Authentificaton failed'})

@app.route('/api/booking', methods=['POST'])
def apiBooking():
    """
    Creates a booking by invoking the create booking function
    Handle card details
    """

    json = request.get_json()
    return jsonify({'error': 'Authentificaton failed'})


@app.route('/api/getOrders', methods=['POST'])
def apiGetOrders():
    """
    Returns all orders tied to a specific account
    """

    json = request.get_json()
    return jsonify({'error': 'Authentificaton failed'})


@app.route('/api/collectbikes', methods=['POST'])
def apiCollectBikes():
    """
    Marks a bike as unavailable 
    """

    json = request.get_json()
    return jsonify({'error': 'Authentificaton failed'})

@app.route('/api/returnBike', methods=['POST'])
def apiReturnBike():
    """
    Marks a bike as available
    """

    json = request.get_json()
    return jsonify({'error': 'Authentificaton failed'})





