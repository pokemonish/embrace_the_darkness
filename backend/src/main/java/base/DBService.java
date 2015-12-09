package base;

import db.DBException;
import resources.Config;

import java.sql.Connection;


/**
 * Created by fatman on 23/11/15.
 */

public interface DBService {

    static String getUrl() {

        return "jdbc:" +
                Config.getInstance().getDbType() + "://" +
                Config.getInstance().getHost() + ':' +
                Config.getInstance().getDbPort() + '/' +
                Config.getInstance().getDbName() + '?' + "user=" +
                Config.getInstance().getDbUser() + '&' + "password=" +
                Config.getInstance().getDbPassword();
    }

    Connection getConnection() throws DBException;

    UserProfile getUserByName(String name) throws DBException;

    void addUser(UserProfile user) throws DBException;

    int countUsers() throws DBException;

    void deleteUserByName(String name) throws DBException;
}
