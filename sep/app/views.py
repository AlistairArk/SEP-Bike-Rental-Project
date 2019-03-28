
from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from .forms import *





@app.route('/newBooking',methods=['GET','POST'])
def newBooking():
    form=addLocationForm(request.form)
    return render_template("newBooking.html", form=form)

@app.route('/bookingAdded',methods=['GET','POST'])
def bookingAdded():
    if request.method == 'POST':
    #     bookingInfo = request.form
    #     for key,value in bookingInfo.items():
    #         if key=='email':
    #             email=value
    #         elif key=='phone':
    #             phone=value
    #         elif key=='date':
    #             date=value
    #         elif key=='stime':
    #             stime=value
    #         elif key=='etime':
    #             etime=value
    #         elif key=='slocation':
    #             slocation=value
    #         elif key=='elocation':
    #             elocation=value
    #     b = models.Booking( cost= ADD THIS,
    #                         start_time=stime,
    #                         end_time=etime,
    #                         bike_amount=numbikes,
    #                         booking_time= ADD THIS,
    #                         paid=False,
    #                         user_id= ADD THIS,
    #                         card_id= ADD THIS,
    #                         end_location=elocation,
    #                         bikes= ADD THIS
    #                         )
    #     db.session.add(b)
    #     db.session.commit()
        return render_template('index.html')



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
