from flask_wtf import Form
from wtforms import TextAreaField, TextField, BooleanField, validators, IntegerField
from wtforms.validators import DataRequired

class addBikesForm(Form):
    amount = IntegerField('amount',validators=[DataRequired()])
    location = TextField('location',validators=[DataRequired()])
