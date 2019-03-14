from flask_wtf import Form
from wtforms import TextAreaField, TextField, BooleanField, validators, IntegerField, SelectField
from wtforms.validators import DataRequired

class addBikesForm(Form):
    amount = IntegerField('amount', [validators.NumberRange(max=10,min=-5)])
    location = SelectField('location', validators=[DataRequired()])
