BaseXRealm
==========

A custom realm for Tomcat where user can be stored in a BaseX collection

## Installation

1. Download the project from my Github account

 2. Add All JAR file from <TOMCAT_HOME>/lib/ to the Java Build Path of the project

 3. Export the project as JAR File (don't choose the Runnable JAR File) and don't export the PasswordGenerator.java, it's only used for generating the passwords.

 4. Move the generated JAR file to <TOMCAT_HOME>/lib/.
 5. Create a BaseX collection like the one below (you can use the PasswordGenerator.java to generate the salt and the hashed password)

The collection that store the users has the following structure:

    <user>
        <username>USERNAME</username>
        <password>PASSWORD</password>
        <salt>SALT</salt>
        <roles>
            <role>ROLE_1</role>
            <role>ROLE_2</role>
        </roles>
    </user>

USERNAME: Username to log in as user
PASSWORD: Password to log in as user
SALT: Unique salt used to hash the password
ROLE_*: Name of the role the user has

6. Set the Realm in your Context configuration like the one below:

<Context docBase="/path/to/application">
    <Realm className="realm.BaseXRealm"
        basexHost="BASEX_IP_ADDRESS"
        basexPort="BASEX_PORT"
        basexUsername="BASEX_USERNAME"
        basexPassword="BASEX_PASSWORD"
        basexUserDbName="BASEX_USER_DATABASE_NAME"
        digest="DIGEST"
        digestEncoding="DIGEST_ENCODING"/>
</Context>

BASEX_IP_ADDRESS: IP address of the BaseX server ex. 127.0.0.1
BASEX_PORT: Port number of the BaseX server ex. 1984(default)
BASEX_USERNAME: Username to log into the BaseX server ex. admin(default)
BASEX_PASSWORD: Password to log into the BaseX server ex. admin(default)
BASEX_USER_DATABASE_NAME : Name of the BaseX database that store the users ex. user_security
DIGEST: Name of the used digest to hash the password ex. sha-512(recommended)
But you could simply not mention it and the password could be stored in plain text
DIGEST_ENCODING: Name of the used encoding for the password hasing ex. utf-8 
You don't need to set it if you store your passwords in plain text

After these steps you can now log in with the user that are stored in a BaseX Database
