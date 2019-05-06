from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session, make_response
from flask_weasyprint import HTML, render_pdf
from flask_mail import Mail, Message
from app import app, models, db
from functools import wraps
from .forms import *
import json


mail = Mail(app)



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
    return render_template("index.html", topname = session["name"])


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
    return render_template('addUser.html',
                            form=form, topname = session["name"])

@app.route('/userAdded',methods=['GET','POST'])
@loginRequired
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
        return render_template('userAdded.html', topname = session["name"])

###############   END OF ADD USER ROUTES   #####################################



###############   ADD BIKES ROUTES   ###########################################

@app.route('/bikesAdded',methods=['GET','POST'])
@loginRequired
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
                                location=l.name, topname = session["name"])


@app.route('/addBikes',methods=['GET','POST'])
@loginRequired
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
                            form=form, topname = session["name"])

###############   END OF ADD BIKES ROUTES   ####################################



###############   ADD EMPLOYEE ROUTES   ########################################

@app.route('/addEmployee',methods=['GET','POST'])
@loginRequired
def addEmployee():
    form=addUserForm(request.form)
    return render_template('addEmployee.html',
                            form=form, topname = session["name"])

@app.route('/employeeAdded',methods=['GET','POST'])
@loginRequired
def employeeAdded():
    if request.method == 'POST':
        employeeInfo = request.form
        usertype = "employee"
        for key,value in employeeInfo.items():
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
        e = models.User(name=name,
                        email=email,
                        phone=phone,
                        username=username,
                        password=password,
                        user_type=usertype)
        db.session.add(e)
        db.session.commit()
        return render_template('employeeAdded.html', topname = session["name"])

###############   END OF ADD EMPLOYEE ROUTES   #################################



###############   ADD LOCATION ROUTES   ########################################

@app.route('/addLocation',methods=['GET','POST'])
@loginRequired
def addLocation():
    form=addLocationForm(request.form)
    return render_template('newLocation.html',
                            form=form, topname = session["name"])



@app.route('/locationAdded',methods=['GET','POST'])
@loginRequired
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
                                name=name, topname = session["name"])

###############   END OF ADD LOCATION ROUTES   #################################



###############   LOCATION STATS ROUTES   ######################################

@app.route('/locationStats')
@loginRequired
def locationStats():
    locations = models.Location.query.all()
    # locations = [[1,'leeds','123 house',5],[2,'headingley','44 drive',6],[3,'burley','77 street',9]]
    return render_template('locationStats.html',
                            locations=locations, topname = session["name"])

################# END OF LOCATION STATS ROUTES ##################



###############   BOOKING PDF ROUTES   ##########################

@app.route('/<sdatetime>/<booking>.pdf')
def receipt(sdatetime, booking):

    bookingob = models.Booking.query.filter_by(id=booking).first()
    userid = bookingob.user_id
    user = models.User.query.filter_by(id=userid).first()
    useremail= user.email
    datebooked = bookingob.booking_time
    name = user.name
    endtime = bookingob.end_time
    starttime = bookingob.start_time

    # strdatetime = datetime.datetime.strptime(sdatetime,"%Y-%m-%dT%H:%M")
    duration=endtime-starttime
    duration_hours=duration.total_seconds()/3600.0

    time = duration_hours
    numbikes = bookingob.bike_amount
    total = bookingob.cost

    html = render_template('receipt.html', datebooked = datebooked, booking=booking, name=name, useremail=useremail, starttime=starttime, endtime=endtime, time=time, numbikes=numbikes, latefee=0, total=total)


    return render_pdf(HTML(string=html))

###############   END OF BOOKING PDF ROUTES   ##################################



###############   AVAILABILITY ROUTES   ########################################

@app.route('/availability', methods=['GET','POST'])
@loginRequired
def availability():
    form=availabilityForm()

    form.slocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    form.elocation.choices=[(l.id,l.name) for l in models.Location.query.all()]

    if request.method=="POST" and form.validate_on_submit():
        stime=request.form['stime']
        etime=request.form['etime']
        slocation = form.slocation.data
        elocation = form.elocation.data

        sdatetime = datetime.datetime.strptime(stime,"%Y-%m-%dT%H:%M")
        edatetime = datetime.datetime.strptime(etime,"%Y-%m-%dT%H:%M")

        message=checkAvailability(sdatetime,edatetime,slocation,elocation,1)
        if message == "Success":
            flash("There are at least 1 bike available at that time.")
        else:
            flash(message)

    return render_template("availability.html", form=form, topname = session["name"])

###############   END OF AVAILABILITY ROUTES   #################################



###############   CREATE BOOKING ROUTES   ######################################


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
    return render_template("newBooking.html", form=form, topname = session["name"])

def createBooking(email,stime,etime,slocation,elocation,numbikes):
    user = models.User.query.filter_by(email=email).first()
    bookingTime = datetime.datetime.utcnow()
    sdatetime = datetime.datetime.strptime(stime,"%Y-%m-%dT%H:%M")
    edatetime = datetime.datetime.strptime(etime,"%Y-%m-%dT%H:%M")

    # message=checkAvailability(sdatetime,edatetime,slocation,elocation,numbikes,email)
    message=checkAvailability(sdatetime,edatetime,slocation,elocation,numbikes)
    if message=="Success":
                                                # :
        duration=edatetime-sdatetime
        duration_hours=duration.total_seconds()/3600.0
        cost=(numbikes*3.5)+(duration_hours/2*numbikes*0.1)
        b = models.Booking( cost= cost,
                            start_time=sdatetime,
                            end_time=edatetime,
                            bike_amount=numbikes,
                            booking_time= bookingTime,
                            paid=False,
                            user_id= user.id,
                            end_location=elocation,
                            start_location=slocation
                            )

        db.session.add(b)
        db.session.commit()
        message="Booking successfully created.Booking confirmation has been emailed to "+email+". Booking cost: "+str(cost)
        send_confirmation(email, b.id, sdatetime)
    # m = "sdatetime: ",sdatetime," | edatetime: ",edatetime," | slocation: ",slocation," | elocation: ",elocation," | numbikes",numbikes
    # flash(m)
    return message

# def checkAvailability(sdatetime,edatetime,slocation,elocation,numbikes,email):
def checkAvailability(sdatetime,edatetime,slocation,elocation,numbikes):
    #simulating bike_amount at slocation between now and stime

    #checking bike amount in slocation currently (exclude bikes that are in use)
    bike_amount = 0
    for bike in models.Bike.query.all():
        if bike.location_id==slocation and bike.in_use==False:
            bike_amount+=1

    # flash("Starting bike amount: "+str(bike_amount))
    #getting current time
    now=datetime.datetime.utcnow()

    bike_amount=queries(sdatetime,edatetime,slocation,elocation,bike_amount,now)
    m3="bike_amount after previous bookings: ",bike_amount
    # flash(m3)

    futureBookingsFeasible=True
    #checking future bookings in same location - will this booking mean they won't have bikes?
    for b in models.Booking.query.all():
        m="Checking future booking Id ",b.id
        # flash(m)
        if b.start_location == slocation and b.start_time>=sdatetime and (edatetime>b.start_time or elocation!=slocation):
            m1="Start location matches and booking ",b.id," starts after new booking."
            # flash(m1)
            if edatetime>b.start_time:
                m2="new booking ends after the start of booking ",b.id
                # flash(m2)
            # if elocation!=slocation:
                # flash("new elocation!=new slocation")
            futureba = queries(b.start_time,b.end_time,b.start_location,b.end_location,bike_amount,sdatetime)
            m4="Number of bikes available before future booking ",b.id,": ",futureba
            # flash(m4)
            if (futureba-numbikes)<=b.bike_amount:
                m5="futureba ",futureba," - numbikes ",numbikes," <= b.bike_amount",b.bike_amount," SO THERE ARE NOT ENOUGH BIKES FOR FUTURE"
                # flash(m5)
                futureBookingsFeasible=False
                # m="Booking affects future booking ",b.id
                # flash(m)
                break
            else:
                m6="futureba ",futureba," - numbikes ",numbikes," >= b.bike_amount",b.bike_amount," SO THERE ARE ENOUGH BIKES FOR FUTURE"
                # flash(m6)

    #checking that there will be space in the end location on their return time
    endLocationSpace=True
    end_ba = queries(edatetime,edatetime,elocation,elocation,bike_amount,now)
    endloc=models.Location.query.get(elocation)
    if end_ba+numbikes>endloc.max_capacity:
        endLocationSpace=False

    #if after all checks there is enough bikes in our location and it doesn't affect future bookings
    #then booking is successful
    if bike_amount>=numbikes and futureBookingsFeasible==True and endLocationSpace==True:
        # flash("Bike amount "+str(bike_amount)+" > numbikes "+str(numbikes))
        message="Success"
                                                # Booking confirmation has been emailed to "+email+"."
        return message
    else:
        # flash("Bike amount "+str(bike_amount)+" < numbikes "+str(numbikes))
        if futureBookingsFeasible==False:
            message="Booking unavailable as bikes in this location are reserved for another booking."
        if endLocationSpace==False:
            message="Booking unavailable as the end location will be full at the end of this booking."
        if bike_amount<=numbikes:
            message="Booking unavailable as the start location will not have enough bikes at the start time."
        return message

def queries(sdatetime,edatetime,slocation,elocation,bike_amount,now):
    bike_amount=bike_amount
    for b in models.Booking.query.all():
        m1="booking no. "+str(b.id)
        # (PINK) checking bookings where bikes are taken out between now and sdatetime
        # and are returned after sdatetime
        if b.start_location==slocation and b.start_time>=now and b.start_time<=sdatetime and b.end_time>sdatetime:
            bike_amount-=b.bike_amount
            # flash(m1+" ---> PINK bike_amount: "+str(bike_amount))
        # (PURPLE) checking bookings which take bikes out during our booking
        # elif b.start_location==slocation and b.start_time>sdatetime and b.start_time<=edatetime:
        #     bike_amount-=b.bike_amount
            # flash(m1+" ---> PURPLE bike_amount: "+str(bike_amount))
        # (ORANGE) checking bookings which take bikes from slocation and return to a different location
        elif b.start_location==slocation and b.start_time>=now and b.start_time<=sdatetime and b.start_location!=b.end_location:
            bike_amount-=b.bike_amount
            # flash(m1+" ---> ORANGE bike_amount: "+str(bike_amount))
        # (GREEN) checking bookings where end location is our location and they're returned before sdatetime
        elif b.end_location==slocation and b.end_time>=now and b.end_time<=sdatetime and (b.start_location!=slocation or b.start_time<now) :
            # bike_amount+=b.bike_amount
            # flash(m1+" ---> GREEN bike_amount: "+str(bike_amount))
        else:
            # flash(m1+" Did not hit any colour criteria.")
    return bike_amount

def send_confirmation(recemail, bookingid, sdatetime):
   msg = Message('LEEDS RIDE BOOKING CONFIRMATION', sender = 'bikesride24@gmail.com', recipients = [recemail])

   link = url_for('receipt', sdatetime=sdatetime, booking=bookingid, _external = True)

   msg.body = "This is a link to your booking confirmation: {}".format(link)

   mail.send(msg)

@app.route('/')
@loginRequired
def index():
    return render_template("index.html", topname = session["name"])

def takeBike(bike_id,booking_id):
    bike = models.Bike.query.get(bike_id)
    bike.in_use=True
    booking = models.Booking.query.get(booking_id)
    booking.bikes.append(bike)
    loc = models.Location.query.get(booking.start_location)
    loc.bike_amount-=1
    db.session.add(loc)
    db.session.add(bike)
    db.session.add(booking)
    db.session.commit()
    #still need to mark booked_bike taken   ?

def returnBike(bike_id,booking_id):
    bike = models.Bike.query.get(bike_id)
    bike.in_use=False
    loc = models.Location.query.get(booking.start_location)
    loc.bike_amount+=1
    db.session.add(loc)
    db.session.add(bike)
    db.session.commit()
    #still need to mark booked_bike returned & add return time   ?


######## API

#
# def log(*args):
#    for line in args:
#       with open('log.log', 'a') as the_file:
#           time = f"{datetime.datetime.now():%Y/%m/%d - %H:%M:%S}"
#           the_file.write("\n["+str(time)+"] "+line)

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
