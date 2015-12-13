package db;

import base.DBService;
import base.DataBaseCreator;
import db.dao.DataBaseCreatorImpl;
import db.dao.UsersDAO;
import db.handlers.ConnectionHandler;
import org.jetbrains.annotations.NotNull;
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
            DataBaseCreator dataBaseCreator = new DataBaseCreatorImpl(getConnection());
            dataBaseCreator.createDB();
        }

        if (Config.getInstance().isDoDeleteDB()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Bye bye!");
                try {
                    DataBaseCreator dataBaseCreator =
                            new DataBaseCreatorImpl(getConnection());
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

    private void connectAndPerform(ConnectionHandler handler) throws SQLException, DBException {
        try (Connection connection = getConnection()) {
            handler.handle(connection);
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
        return new UsersDAO(getConnection());
    }
}
