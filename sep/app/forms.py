from flask_wtf import Form
from .models import User
from wtforms import TextAreaField, StringField, BooleanField, validators, IntegerField, SelectField, FloatField, DateField, DateTimeField
from wtforms.validators import DataRequired, Length, Email, EqualTo, ValidationError, Optional
# from wtforms_components import TimeField

class addBikesForm(Form):
    amount = IntegerField('amount', [validators.NumberRange(max=10,min=-5)])
    location = SelectField('location', validators=[DataRequired()])

class addLocationForm(Form):
    name = StringField('name', validators=[DataRequired(), Length(min=1,max=50)])
    addr = TextAreaField('addr', validators=[DataRequired(), Length(min=10,max=80)])
    max_capacity = IntegerField('max_capacity', [validators.NumberRange(max=200,min=5)])
    longt = FloatField('longt', validators=[DataRequired()])
    latt = FloatField('latt', validators=[DataRequired()])

class addBookingForm(Form):
    email = StringField('email', validators=[DataRequired(), Email()])
    numbikes = IntegerField('numbikes', validators=[DataRequired()])
    stime = DateTimeField('stime', validators=[DataRequired()])
    etime = DateTimeField('etime', validators=[DataRequired()])
    slocation = SelectField('slocation', validators=[DataRequired()])
    elocation = SelectField('elocation', validators=[DataRequired()])

    # def validate_email(self, email):
    #     user = User.query.filter_by(email=email.data).first()
    #     if not user:
    #         raise ValidationError('An account does not exists for that email.')



# class testForm(Form):
#     stime = DateTimeField('stime', validators=[DataRequired()])
#     etime = DateTimeField('etime', validators=[DataRequired()])
    # day =
