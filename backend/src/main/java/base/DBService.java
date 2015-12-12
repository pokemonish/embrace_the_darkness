package base;

import db.DBException;
import db.dao.UsersDAO;
import db.handlers.ConnectionHandler;
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

    public UsersDAO getUsersDAO() throws DBException;
}
