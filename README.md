BaseXRealm
==========

A custom realm for Tomcat where user can be stored in a BaseX collection

### Installation

* Download the project from my Github account

* Add All JAR file from <TOMCAT_HOME>/lib/ to the Java Build Path of the project

* Export the project as JAR File (don't choose the Runnable JAR File) and don't export the PasswordGenerator.java, it's only used for generating the passwords.

* Move the generated JAR file to <TOMCAT_HOME>/lib/.
* Create a BaseX collection like the one below (you can use the PasswordGenerator.java to generate the salt and the hashed password)

The collection that store the users has the following structure:
```xml
    <user>
        <username>USERNAME</username>
        <password>PASSWORD</password>
        <salt>SALT</salt>
        <roles>
            <role>ROLE_1</role>
            <role>ROLE_2</role>
        </roles>
    </user>
```

<b>USERNAME:</b> Username to log in as user<br/>
<b>PASSWORD:</b> Password to log in as user<br/>
<b>SALT:</b> Unique salt used to hash the password<br/>
<b>ROLE_*:</b> Name of the role the user has<br/>

* Set the Realm in your Context configuration like the one below:
```xml
    <Context docBase="/path/to/application">
        <Realm className="sahli.michel.realm.BaseXRealm"
            basexHost="BASEX_IP_ADDRESS"
            basexPort="BASEX_PORT"
            basexUsername="BASEX_USERNAME"
            basexPassword="BASEX_PASSWORD"
            basexUserDbName="BASEX_USER_DATABASE_NAME"
            digest="DIGEST"
            digestEncoding="DIGEST_ENCODING"/>
    </Context>
```

<b>BASEX_IP_ADDRESS:</b> IP address of the BaseX server ex. 127.0.0.1<br/>
<b>BASEX_PORT:</b> Port number of the BaseX server ex. 1984(default)<br/>
<b>BASEX_USERNAME:</b> Username to log into the BaseX server ex. admin(default)<br/>
<b>BASEX_PASSWORD:</b> Password to log into the BaseX server ex. admin(default)<br/>
<b>BASEX_USER_DATABASE_NAME:</b> Name of the BaseX database that store the users ex. user_security<br/>
<b>DIGEST:</b> Name of the used digest to hash the password ex. sha-512(recommended)<br/>
But you could simply not mention it and the password could be stored in plain text<br/>
<b>DIGEST_ENCODING:</b> Name of the used encoding for the password hasing ex. utf-8 <br/>
You don't need to set it if you store your passwords in plain text<br/>

After these steps you can now log in with the user that are stored in a BaseX Database

### License

BaseXRealm is licensed under the [MIT license.](https://github.com/michelsahli/BaseXRealm/blob/master/LICENSE)
