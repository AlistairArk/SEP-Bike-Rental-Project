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

        ssplit = stime.split("T")
        sdatetime = datetime.datetime.strptime(ssplit[0]+" "+ssplit[1],"%Y-%m-%d %H:%M")
        esplit = etime.split("T")
        edatetime = datetime.datetime.strptime(esplit[0]+" "+esplit[1],"%Y-%m-%d %H:%M")
        m3="sdatetime type: ",type(sdatetime)," | sdatetime: ",sdatetime
        m4="edatetime type: ",type(edatetime)," | edatetime: ",edatetime
        flash(m3)
        flash(m4)
        m5="stime type: ",type(stime)," stime: ",stime," | etime type: ",type(etime)," etime: ",etime
        flash(m5)

    return render_template("testForm.html", form=form)

@app.route('/newBooking', methods=['GET','POST'])
def newBooking():
    form=addBookingForm()

    form.slocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    form.elocation.choices=[(l.id,l.name) for l in models.Location.query.all()]
    flash("before getting form data")
    if request.method=="POST" and form.validate_on_submit():
        email = form.email.data
        stime=request.form['stime']
        etime=request.form['etime']
        slocation = form.slocation.data
        elocation = form.elocation.data
        numbikes = form.numbikes.data
        flash("form validates on submit")
        user = models.User.query.filter_by(email=email).first()
        if user is not None:
            flash("user exists")
            cost = 13.44
            bookingTime = datetime.datetime.now()
            startloc = models.Location.query.filter_by(id=slocation).first()
            # ssplit = stime.split("T")
            # sdatetime = datetime.datetime.strptime(ssplit[0]+" "+ssplit[1],"%Y-%m-%d %H:%M")
            # esplit = etime.split("T")
            # edatetime = datetime.datetime.strptime(esplit[0]+" "+esplit[1],"%Y-%m-%d %H:%M")

            sdatetime = datetime.datetime.strptime(stime,"%Y-%m-%dT%H:%M")
            edatetime = datetime.datetime.strptime(etime,"%Y-%m-%dT%H:%M")

            b = models.Booking( cost= cost,
                                start_time=sdatetime,
                                end_time=edatetime,
                                # start_time=stime,
                                # end_time=etime,
                                bike_amount=numbikes,
                                booking_time= bookingTime,
                                paid=False,
                                user_id= user.id,
                                end_location=elocation,
                                start_location=startloc.id
                                )

            m="stime type: ",type(stime)," stime: ",stime,"| etime type: ",type(etime)," etime: ",etime
            flash(m)
            m4="sdatetime type: ",type(sdatetime)," | edatetime type: ",type(edatetime)
            flash(m4)
            message="Booking: cost: "+str(cost)+" | startloc: "+slocation+" | endloc: "+elocation
            flash(message)
            m2 = "start time: ",sdatetime," | end time: ",edatetime
            flash(m2)
            m3 = "booking time: ",bookingTime," | user name: "+user.name," | bike amount: ",numbikes
            flash(m3)

            # db.session.add(b)
            # db.session.commit()
        else:
            flash("This email is not associated with a user.")
    else:
        flash("form does not validate on submit")

    return render_template("newBooking.html", form=form)


# @app.route('/bookingAdded',methods=['GET','POST'])
# def bookingAdded():
#     if request.method == 'POST':
#         bookingInfo = request.form
#         for key,value in bookingInfo.items():
#             if key=='email':
#                 email=value
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
#         if user is not None:
#             cost = 13.44
#             bookingTime = datetime.datetime.now()
#             startloc = models.Location.query.filter_by(id=slocation).first()
#             ssplit = stime.split("T")
#             sdatetime = datetime.datetime.strptime(ssplit[0]+" "+ssplit[1],"%Y-%m-%d %H:%M")
#             esplit = etime.split("T")
#             edatetime = datetime.datetime.strptime(esplit[0]+" "+esplit[1],"%Y-%m-%d %H:%M")
#
#             b = models.Booking( cost= cost,
#                                 start_time=sdatetime,
#                                 end_time=edatetime,
#                                 bike_amount=numbikes,
#                                 booking_time= bookingTime,
#                                 paid=False,
#                                 user_id= user.id,
#                                 end_location=elocation,
#                                 start_location=startloc.id
#                                 )
#
#
#             m4="sdatetime type: ",type(sdatetime)," | edatetime type: ",type(edatetime)
#             flash(m4)
#             message="Booking: cost: "+str(cost)+" | startloc: "+slocation+" | endloc: "+elocation
#             flash(message)
#             m2 = "start time: ",sdatetime," | end time: ",edatetime," | bike amount: ",numbikes
#             flash(m2)
#             m3 = "booking time: ",bookingTime," | user name: "+user.name
#             flash(m3)
#
#             db.session.add(b)
#             db.session.commit()
#         else:
#             flash("This email is not associated with a user.")
#             bookingForm=addBookingForm(bookingInfo)
#             # return render_template('{{ url_for(\'newBooking\')}}')
#             return render_template("newBooking.html", form=bookingForm)
#             # newBooking()
#
#     else:
#         message="request method: "+request.method
#         flash(message)
#
#     return render_template('bookingMade.html')

        # if user is not None:
        #     sdatetime = datetime.datetime.combine(date, stime)
        #     edatetime = datetime.datetime.combine(date, etime)
        #     bookingTime = edatetime - sdatetime
        #
        #
        #     #cost = (numbikes*3.5)+(bookingTime/2*numbikes*0.1)
        #     cost = (numbikes*3.5)+(1.5*numbikes*0.1)
        #
        #     b = models.Booking( cost= cost,
        #                         start_time=stime,
        #                         end_time=etime,
        #                         bike_amount=numbikes,
        #                         booking_time= bookingTime,
        #                         paid=False,
        #                         user_id= user.id,
        #                         end_location=elocation,
        #                         # bikes= ADD THIS
        #                         )
        #     db.session.add(b)
        #     db.session.commit()
        #     return render_template('index.html')


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

# @app.route('/test')
# def test():
#     #Just rendering test as a test
#     return render_template("test.html")

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
