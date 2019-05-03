from app import app, function
from flask import render_template, redirect, url_for, flash, request, jsonify, session
from app import app, models, db
from .forms import *
import datetime
# from datetime import datetime

@app.route('/test', methods=['GET','POST'])
def test():
    form=testForm(request.form)
    if request.method=='POST':
        stime=request.form['stime']
        etime=request.form['etime']
        sdatetime = datetime.datetime.strptime(stime,"%Y-%m-%dT%H:%M")
        edatetime = datetime.datetime.strptime(etime,"%Y-%m-%dT%H:%M")
        m3="sdatetime type: ",type(sdatetime)," | sdatetime: ",sdatetime
        m4="edatetime type: ",type(edatetime)," | edatetime: ",edatetime
        flash(m3)
        flash(m4)

    return render_template("testForm.html", form=form)

@app.route('/newBooking', methods=['GET','POST'])
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
        user = models.User.query.filter_by(email=email).first()
        if user is not None:
            createBooking(email,stime,etime,slocation,elocation,numbikes,user)
    return render_template("newBooking.html", form=form)

def createBooking(email,stime,etime,slocation,elocation,numbikes,user):
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

    # m="stime type: ",type(stime)," stime: ",stime,"| etime type: ",type(etime)," etime: ",etime
    # flash(m)
    # m4="sdatetime type: ",type(sdatetime)," | edatetime type: ",type(edatetime)
    # flash(m4)
    # message="Booking: cost: ",cost," | startloc: ",slocation," | endloc: ",elocation
    # flash(message)
    # m2 = "start time: ",sdatetime," | end time: ",edatetime
    # flash(m2)
    # m3 = "booking time: ",bookingTime," | user name: "+user.name," | bike amount: ",numbikes
    # flash(m3)

    db.session.add(b)
    db.session.commit()

@app.route('/')
def index():
    return render_template("index.html")
