package base;

import db.DBException;
import db.dao.HighscoresDAO;
import db.dao.UsersDAO;
import db.handlers.ConnectionConsumer;
import db.handlers.ConnectionHandler;
import resources.Config;


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

    UsersDAO getUsersDAO() throws DBException;

    <T> T connectAndReturnSmth(ConnectionHandler<T> handler) throws DBException;

    void connectAndUpdate(ConnectionConsumer handler) throws DBException;

    HighscoresDAO getHighscoreDAO() throws DBException;
}
