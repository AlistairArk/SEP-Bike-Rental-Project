from app import db
from sqlalchemy import Table
from flask_login import UserMixin

#class for all users, different privilege levels for different user types
#has one to many relationships with the Card and Booking classes
#default icon from: www.iconfinder.com by Paomedia
class User(UserMixin,db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    name = db.Column(db.String(40))
    email = db.Column(db.String(50), unique=True, nullable=False)
    password = db.Column(db.String(80), nullable=False)
    image = db.Column(db.String(75), nullable=False, default='default_icon')
    phone = db.Column(db.String(20), nullable=False)
    user_type = db.Column(db.String(20), nullable=False)

    cards = db.relationship('Card', backref='user', lazy=True)
    bookings = db.relationship('Booking', backref='user', lazy=True)



#class to store all information about a customer's card
#has a many to one relationship with User, and is a foreign key in Booking
class Card(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    card_number = db.Column(db.String(15), unique=True, nullable=False)
    card_name = db.Column(db.String(40), nullable=False)
    security_no = db.Column(db.Integer, nullable=False)
    expiry_date = db.Column(db.Date, nullable=False)
    card_type = db.Column(db.String(20), nullable=False)
    addr_first = db.Column(db.String(20), nullable=False)
    addr_second = db.Column(db.String(20))
    addr_towncity = db.Column(db.String(20), nullable=False)
    addr_county = db.Column(db.String(20))
    addr_postcode = db.Column(db.String(10), nullable=False)

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

    bookings = db.relationship('Booking', backref='card', lazy=True)


#class to store the locations of the business where bikes are kept
#has a one to many relationship with Bike
#default place from: www.iconfinder.com by Enes Dal
class Location(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(30))
    bike_amount = db.Column(db.Integer, nullable=False)
    max_capacity = db.Column(db.Integer, nullable=False)
    image = db.Column(db.String(80), nullable=False, default='default_place')
    addr = db.Column(db.Text, nullable=False)
    longt = db.Column(db.Float)
    latt = db.Column(db.Float)

    bikes = db.relationship('Bike', backref='location', lazy=True)
    bookings = db.relationship('Booking', backref='location', lazy=True)



#stores information about a single bike each time it is involved in a booking
#implements many to many relationship between booking and bike with an association table
booked_bike = db.Table('booked_bike',
    db.Column('booking_id', db.Integer, db.ForeignKey('booking.id'), primary_key=True),
    db.Column('bike_id', db.Integer, db.ForeignKey('bike.id'), primary_key=True),
    db.Column('taken', db.Boolean, nullable=False, default=False),
    db.Column('returned', db.Boolean, nullable=False, default=False),
    db.Column('return_time', db.DateTime)
)


#stores information about a user's booking
#has a many to one relationship with User and Card, a many to many relationship with Bike
class Booking(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    cost = db.Column(db.Numeric(10,2), nullable=False)
    start_time = db.Column(db.DateTime, nullable=False)
    end_time = db.Column(db.DateTime, nullable=False)
    bike_amount = db.Column(db.Integer, nullable=False)
    late_fee = db.Column(db.Numeric(10,2), default=0.00)
    booking_time = db.Column(db.DateTime, nullable=False)
    paid = db.Column(db.Boolean, nullable=False)
    end_location = db.Column(db.Integer, nullable=False)
    complete = db.Column(db.Boolean, default=False)

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    card_id = db.Column(db.Integer, db.ForeignKey('card.id'))
    start_location = db.Column(db.Integer, db.ForeignKey('location.id'), nullable=False)

    bikes = db.relationship('Bike',secondary=booked_bike, backref=db.backref('booked',lazy='dynamic'))

#stores information about each bike owned by the company
#has a many to one relationship with Location, and a many to many with Booking
class Bike(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    in_use = db.Column(db.Boolean, nullable=False)
    status = db.Column(db.String(20), nullable=False)

    location_id = db.Column(db.Integer, db.ForeignKey('location.id'), nullable=False)
