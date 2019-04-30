from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from .forms import *
import datetime


@app.route('/newBooking', methods=['GET','POST'])
def newBooking():
    form=addBookingForm(request.form)

    form.slocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    form.elocation.choices=[(l.id,l.name) for l in models.Location.query.all()]

    return render_template("newBooking.html", form=form)


# @app.route('/bookingAdded',methods=['GET','POST'])
# def bookingAdded():
#     if request.method == 'POST':
#         bookingInfo = request.form
#         for key,value in bookingInfo.items():
#             if key=='email':
#                 email=value
#             elif key=='phone':
#                 phone=value
#             elif key=='date':
#                 date=value
#             elif key=='stime':
#                 stime=value
#             elif key=='etime':
#                 etime=value
#             elif key=='slocation':
#                 slocation=value
#             elif key=='elocation':
#                 elocation=value
#             elif key=='numbikes':
#                 numbikes=value
#
#         user = models.User.query.filter_by(email=email).first()
#
#         if user is not None:
#             sdatetime = datetime.datetime.combine(date, stime)
#             edatetime = datetime.datetime.combine(date, etime)
#             bookingTime = edatetime - sdatetime
#
#
#             #cost = (numbikes*3.5)+(bookingTime/2*numbikes*0.1)
#             cost = (numbikes*3.5)+(1.5*numbikes*0.1)
#
#             b = models.Booking( cost= cost,
#                                 start_time=stime,
#                                 end_time=etime,
#                                 bike_amount=numbikes,
#                                 booking_time= bookingTime,
#                                 paid=False,
#                                 user_id= user.id,
#                                 end_location=elocation,
#                                 # bikes= ADD THIS
#                                 )
#             db.session.add(b)
#             db.session.commit()
#             return render_template('index.html')


@app.route('/')
def index():
    #Just rendering index as a test
    return render_template("index.html")

@app.route('/owner')
def ownerMainPage():
    #Just rendering reset password as a test
    return render_template("ownerMainPage.html")

@app.route('/addEmployee')
def addEmployee():
    #Just rendering addEmployee as a test
    return render_template("addEmployee.html")

@app.route('/staffLogin')
def staffLogin():
    #Just rendering login as a test
    return render_template("staffLogin.html")

@app.route('/resetPassword')
def resetPassword():
    #Just rendering reset password as a test
    return render_template("resetPassword.html")

@app.route('/addBikes')
def addBikes():
    #Just rendering addBikes as a test
    return render_template("addBikes.html")

@app.route('/newLocation')
def newLocation():
    #Just rendering newLocation as a test
    return render_template("newLocation.html")

@app.route('/test')
def test():
    #Just rendering test as a test
    return render_template("test.html")

# @app.route('/newBooking',methods=['GET','POST'])
# def newBooking():
#     form=addBookingForm(request.form)
#     #locations = [[1, "Leeds"],[2, "London"], [3, "Manchester"]]
#     form.slocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
#     form.elocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
#     #form.slocation.choices=locations
#     #form.elocation.choices=locations
#
#     return render_template("newBooking.html", form=form)
