package sahli.michel.realm;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.eiafr.db.BaseXClient;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseXRealm extends RealmBase {
    private static Log log = LogFactory.getLog(BaseXRealm.class);
    private String basexHost;
    private int basexPort;
    private String basexUsername;
    private String basexPassword;
    private String basexUserDbName;
    private String validDbPassword;
    private BaseXClient session;
    
    @Override
    public Principal authenticate(String username, String credentials) {
        Principal validPrincipal;
        String[] dbUsernames = null;
        String dbPassword = "";
        String dbSalt = "";
        boolean isUserValid = false;
        boolean isPasswordValid = false;
        
        if(username == null || credentials == null) {
            log.warn("Username or/and password aren't set");
            return null;
        }
        
        openBasexDb();
        
        dbUsernames = getName().split(";");
        if(dbUsernames.length != 1 || !dbUsernames[0].equals("")) {
            
            for(int i = 0; i < dbUsernames.length; i++) {
                if(username.equals(dbUsernames[i])) {
                    isUserValid = true;
                    break;
                }
            }
            if(isUserValid) {
                dbPassword = getPassword(username);
                dbSalt = getSalt(username);
                
                if(hasMessageDigest()) {
                    isPasswordValid = (digest(username + ".+." + credentials + "*+*" + dbSalt).equalsIgnoreCase(dbPassword));
                }
                else {
                    isPasswordValid = credentials.equals(dbPassword);
                }
                
                if(isPasswordValid) {
                    this.validDbPassword = dbPassword;
                    validPrincipal = getPrincipal(username);
                    
                    closeBasexDb();
                    
                    return validPrincipal;
                }
            }
        }
        log.warn("Username or password doesn't match the informations in the BaseX Database");
        closeBasexDb();
        return null;
    }
    
    @Override
    protected String getName() {
        String dbUsernames = "";
        try {
            dbUsernames = this.session.execute("xquery declare option output:item-separator \";\";//username/text()");
        }
        catch (Exception e) {
            log.error(sm.getString("Can't connect to the BaseX Database",e));
        }
        return dbUsernames;
    }
   
    @Override
    protected String getPassword(final String username) {
        String dbPassword = "";
        try {
            dbPassword = this.session.execute("xquery /user[username = \"" + username + "\"]/password/text()");
        }
        catch (Exception e) {
            log.error(sm.getString("Can't connect to the BaseX Database",e));
        }
        return dbPassword;
    }
   
    protected String getSalt(final String username) {
        String dbSalt = "";
        try {
            dbSalt = this.session.execute("xquery /user[username = \"" + username + "\"]/salt/text()");
        }
        catch (Exception e) {
            log.error(sm.getString("Can't connect to the BaseX Database",e));
        }
        return dbSalt;
    }
    
    @Override
    protected Principal getPrincipal(final String username) {
        List<String> dbRoles = new ArrayList<>();
        try {
            dbRoles = Arrays.asList(this.session.execute("xquery declare option output:item-separator \";\";for $x in /user[username = \"" + username + "\"]/roles return $x/role/text()").split(";"));
        }
        catch (Exception e) {
            log.error(sm.getString("Can't connect to the BaseX Database",e));
        }
        return new GenericPrincipal(username, this.validDbPassword, dbRoles);
    }
    
    public void openBasexDb() {
        try {
            this.session = new BaseXClient(this.basexHost, this.basexPort, this.basexUsername, this.basexPassword);
            this.session.execute("open " + this.basexUserDbName);
        }
        catch (IOException e) {
            log.error(sm.getString("Can't connect to the BaseX Database",e));
        }
    }
    
    public void closeBasexDb() {
        if(this.session != null) {
            try {
                this.session.execute("close");
                this.session.close();
            }
            catch (IOException e) {
                log.error(sm.getString("Can't close the connection to the BaseX Database",e));
            }
        }
    }
    
    public void setBasexHost(String basexHost) {
        this.basexHost = basexHost;
    }
    
    public void setBasexPort(int basexPort) {
        this.basexPort = basexPort;
    }
    
    public void setBasexUsername(String basexUsername) {
        this.basexUsername = basexUsername;
    }
    
    public void setBasexPassword(String basexPassword) {
        this.basexPassword = basexPassword;
    }
    
    public void setBasexUserDbName(String basexDbName) {
        this.basexUserDbName = basexDbName;
    }
}
