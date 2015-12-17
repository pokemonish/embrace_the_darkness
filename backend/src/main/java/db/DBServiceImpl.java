package db;

import base.DBService;
import base.DataBaseCreator;
import db.dao.DataBaseCreatorImpl;
import db.dao.HighscoresDAO;
import db.dao.UsersDAO;
import db.executor.TExecutor;
import db.handlers.ConnectionConsumer;
import db.handlers.ConnectionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import resources.Config;

import java.sql.*;

/**
 * Created by fatman on 23/11/15.
 */

public class DBServiceImpl implements DBService {

    private String connectionUrl;

    public static String getUrl() {

        return "jdbc:" +
                Config.getInstance().getDbType() + "://" +
                Config.getInstance().getHost() + ':' +
                Config.getInstance().getDbPort() + '/' +
                Config.getInstance().getDbName() + '?' + "user=" +
                Config.getInstance().getDbUser() + '&' + "password=" +
                Config.getInstance().getDbPassword();
    }

    public DBServiceImpl() throws DBException {
        try {
            DriverManager.registerDriver((Driver)
                    Class.forName(Config.getInstance().getDbDriver()).newInstance());
        } catch (SQLException | InstantiationException |
                    IllegalAccessException | ClassNotFoundException e)  {
            e.printStackTrace();
            System.out.append("Can't register driver ")
                    .append(Config.getInstance().getDbDriver());
            System.exit(1);
        }

        if (Config.getInstance().isDoCreateDB()) {
            connectionUrl =
                "jdbc:" + Config.getInstance().getDbType()
                + "://" + Config.getInstance().getHost() + ':' +
                Config.getInstance().getDbPort() + '?' +
                "user=" + Config.getInstance().getDbUser() + '&' +
                "password=" + Config.getInstance().getDbPassword();
            DataBaseCreator dataBaseCreator = new DataBaseCreatorImpl(new TExecutor(this));
            dataBaseCreator.createDB();
        }

        if (Config.getInstance().isDoDeleteDB()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Bye bye!");
                try {
                    DataBaseCreator dataBaseCreator =
                            new DataBaseCreatorImpl(new TExecutor(this));
                    dataBaseCreator.dropDB();
                } catch (DBException e) {
                    e.printStackTrace();
                    System.out.println("Database was not deleted");
                }
            }));
        }

        connectionUrl = getUrl();

        System.out.append("URL: ").append(getUrl()).append("\n");
    }

    @Nullable
    @Override
    public <T> T connectAndReturn(ConnectionHandler<T> handler) throws DBException {
        try (Connection connection = getConnection()) {
            return handler.handle(connection);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void connectAndUpdate(ConnectionConsumer handler) throws DBException {
        try (Connection connection = getConnection()) {
            handler.handle(connection);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @NotNull
    private Connection getConnection() throws DBException {
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public UsersDAO getUsersDAO() throws DBException {
        return new UsersDAO(new TExecutor(this));
    }

    @Override
    public HighscoresDAO getHighscoreDAO() throws DBException {
        return new HighscoresDAO(new TExecutor(this));
    }
}
