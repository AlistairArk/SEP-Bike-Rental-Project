from flask_wtf import Form
from .models import User
from wtforms import TextAreaField, StringField, BooleanField, validators, IntegerField, SelectField, FloatField, PasswordField
from wtforms.validators import DataRequired, Length, Email, EqualTo, ValidationError, Optional
import datetime
from datetime import timedelta
# from wtforms_components import TimeField

class addBikesForm(Form):
    amount = IntegerField('amount', [validators.NumberRange(max=10,min=-5)])
    location = SelectField('location', coerce=int, validators=[DataRequired()])

class addLocationForm(Form):
    name = StringField('name', validators=[DataRequired(), Length(min=1,max=50)])
    addr = TextAreaField('addr', validators=[DataRequired(), Length(min=10,max=80)])
    max_capacity = IntegerField('max_capacity', [validators.NumberRange(max=200,min=5)])
    longt = FloatField('longt', validators=[DataRequired()])
    latt = FloatField('latt', validators=[DataRequired()])

class addUserForm(Form):
    name = StringField('name', validators=[DataRequired(), Length(min=1,max=50)])
    username = StringField('username', validators=[DataRequired(), Length(min=1,max=50)])
    email = StringField('email', validators=[DataRequired(), Email()])
    phone = StringField('phone', validators=[DataRequired()])
    password = PasswordField('Password', validators=[DataRequired(), Length(min=2, max=30)])

    def validate_phone(self,phone):
        noSpaces=""
        for i in phone.data:
            if i!=" ":
                noSpaces+=i
        valid=True
        try:
            isInteger=int(noSpaces)
            if len(noSpaces)<9 or len(noSpaces)>11:
                raise ValidationError("Not a valid length for a UK phone.")
        except:
            raise ValidationError("Invalid characters entered.")

    def validate_username(self,username):
        for u in User.query.all():
            if username.data==u.username:
                raise ValidationError("This username is already taken.")

    def validate_email(self,email):
        for u in User.query.all():
            if email.data==u.email:
                raise ValidationError("This email is already taken.")


class addBookingForm(Form):
    email = StringField('email', validators=[DataRequired(), Email()])
    numbikes = IntegerField('numbikes', validators=[DataRequired()])
    stime = StringField('stime', validators=[DataRequired()])
    etime = StringField('etime', validators=[DataRequired()])
    slocation = SelectField('slocation', coerce=int, validators=[DataRequired()])
    elocation = SelectField('elocation', coerce=int, validators=[DataRequired()])

    def validate_email(self, email):
        user = User.query.filter_by(email=email.data).first()
        if not user:
            raise ValidationError('This email is not associated with an account.')

    def validate_numbikes(self,numbikes):
        if numbikes.data<1:
            raise ValidationError('Must add at least one bike to booking.')
        elif numbikes.data>4:
            raise ValidationError('Maximum 4 bikes per booking.')

    def validate_stime(self,stime):
        now = datetime.datetime.utcnow()
        sdatetime = datetime.datetime.strptime(stime.data,"%Y-%m-%dT%H:%M")
        if sdatetime < now:
            raise ValidationError("Bookings are only available to be made in the future.")
        if sdatetime>now+timedelta(weeks=2):
            raise ValidationError("Bookings can only be made up to 2 weeks in advance.")

    def validate_etime(self,etime):
        sdatetime = datetime.datetime.strptime(self.stime.data,"%Y-%m-%dT%H:%M")
        edatetime = datetime.datetime.strptime(etime.data,"%Y-%m-%dT%H:%M")
        if edatetime <= sdatetime:
            raise ValidationError('End time must be after starttime.')

class availabilityForm(Form):
    stime = StringField('stime', validators=[DataRequired()])
    etime = StringField('etime', validators=[DataRequired()])
    slocation = SelectField('slocation', coerce=int, validators=[DataRequired()])
    elocation = SelectField('elocation', coerce=int, validators=[DataRequired()])

    def validate_stime(self,stime):
        now = datetime.datetime.utcnow()
        sdatetime = datetime.datetime.strptime(stime.data,"%Y-%m-%dT%H:%M")
        if sdatetime < now:
            raise ValidationError("Availability can only be checked for the future.")
        if sdatetime>now+timedelta(weeks=2):
            raise ValidationError("Availability can only be checked up to 2 weeks in from now.")

    def validate_etime(self,etime):
        sdatetime = datetime.datetime.strptime(self.stime.data,"%Y-%m-%dT%H:%M")
        edatetime = datetime.datetime.strptime(etime.data,"%Y-%m-%dT%H:%M")
        if edatetime <= sdatetime:
            raise ValidationError('End time must be after the specified start time.')
        if (edatetime-sdatetime)>timedelta(weeks=1):
            raise ValidationError("Bikes can only be hired for maximum 7 days at a time.")
