from flask import Flask
from flask_mail import Mail, Message

import MySQLdb
from . import db, models


'''
Test data in user table: 

+----+--------------------+----------+-----------------------+--------------+--------------+--------------+-----------+
| id | name               | username | email                 | password     | image        | phone        | user_type |
+----+--------------------+----------+-----------------------+--------------+--------------+--------------+-----------+
|  1 | Paul Rudd          | prudd    | prudd@gmail.com       | password     | default_icon | 01234567890  | manager   |
|  2 | Tahani Al Jamil    | Tahani   | taj@gmail.com         | plaintext    | default_icon | 98765432100  | employee  |
|  3 | Ezekiel Figuero    | Zeke     | books@gmail.com       | mylenecruz   | default_icon | 2468135901   | customer  |
|  4 | Rogelio De La Vega | Rogelio  | rdlv@gmail.com        | rogeliodlv   | default_icon | 019283746510 | customer  |
|  5 | Isabelle Lightwood | Izzy     | i.lightwood@gmail.com | sizzyforever | default_icon | 00000000000  | employee  |
|  6 | NULL               | Alec     | a.lightwood@gmail.com | magnusbane   | default_icon | 11111111111  | employee  |
+----+--------------------+----------+-----------------------+--------------+--------------+--------------+-----------+
'''

def login(*args, **kwargs):
    username = kwargs.get("username", 0)
    password = kwargs.get("password", 0)

    # Check if username & password are true
    user = models.User.query.filter_by(username=username, password=password).first()
    
    if user==None: # User not found
        return [0, 0, 0]
    else:
        return [1, user.user_type, user.username]  # return user type






def emailExists(email):
    # Perform database check to validate if input email exists
    # Return 1 if found and 0 if not found.

    return 1 # Assume email exists as a test


def addEmployee(*args, **kwargs):
    name      = kwargs.get("name",      "")
    username  = kwargs.get("username",  "")
    email     = kwargs.get("email",     "")
    password  = kwargs.get("password",  "")
    image     = kwargs.get("image",     "")
    phone     = kwargs.get("phone",     "")
    userType  = kwargs.get("userType",  "")

    user = models.User.query.filter_by(username=username).first()
    if user==None: # User not found
        # add new employee as normal 

        l = models.Location(name      = name,
                            username  = username,
                            email     = email,
                            password  = password,
                            image     = image,
                            phone     = phone,
                            user_type = userType)

        db.session.add(l)
        db.session.commit()


        return 1    # if employee was added successfully return 1
    else: 
        return 0    # if employee already exists return 0


def resetRequest(*args, **kwargs):
    email = kwargs.get("email", 0)

    
    if emailExists(email):

        # Generate and send email

        return 1
    else:
        return 0 # Email could not be found
