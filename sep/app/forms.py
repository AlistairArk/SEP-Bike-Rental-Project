from flask_wtf import Form
from wtforms import TextAreaField, StringField, BooleanField, validators, IntegerField, SelectField, FloatField, PasswordField
from wtforms.validators import DataRequired, Length, Email

class addBikesForm(Form):
    amount = IntegerField('amount', [validators.NumberRange(max=10,min=-5)])
    location = SelectField('location', validators=[DataRequired()])

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

class addEmployeeForm(Form):
    name = StringField('name', validators=[DataRequired(), Length(min=1,max=50)])
    username = StringField('username', validators=[DataRequired(), Length(min=1,max=50)])
    email = StringField('email', validators=[DataRequired(), Email()])
    phone = StringField('phone', validators=[DataRequired()])
    password = PasswordField('Password', validators=[DataRequired(), Length(min=2, max=30)])
