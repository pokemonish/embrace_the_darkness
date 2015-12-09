package db;

import base.DBService;
import base.DataBaseDAO;
import base.UserProfile;
import db.dao.DataBaseDAOImpl;
import db.dao.UsersDAO;
import db.datasets.UsersDataSet;
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
            DataBaseDAO dataBaseDAO = new DataBaseDAOImpl(getConnection());
            dataBaseDAO.createDB();
        }

        if (Config.getInstance().isDoDeleteDB()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Bye bye!");
                try {
                    DataBaseDAO dataBaseDAO =
                            new DataBaseDAOImpl(getConnection());
                    dataBaseDAO.dropDB();
                } catch (DBException e) {
                    System.out.println("Database was not deleted");
                }
            }));
        }

        connectionUrl = getUrl();

        System.out.append("URL: ").append(getUrl()).append("\n");
    }

    private void connectAndPerform(ConnectionHandler handler) throws DBException {
        try (Connection connection = getConnection()) {
            handler.handle(connection);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    @NotNull
    public Connection getConnection() throws DBException{
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void addUser(UserProfile user) throws DBException {
        connectAndPerform(connection -> {
            UsersDAO usersDAO = new UsersDAO(connection);

            try {
                usersDAO.addUser(user, connection);
            } catch (SQLException e) {
                throw new DBException(e);
            }
        });
    }

    @Override
    public void deleteUserByName(String name) throws DBException {
        connectAndPerform(connection -> {
            UsersDAO usersDAO = new UsersDAO(connection);

            try {
                usersDAO.deleteUserByName(name);
            } catch (SQLException e) {
                throw new DBException(e);
            }
        });
    }

    @Nullable
    @Override
    public UserProfile getUserByName(String name) throws DBException {
        final UsersDataSet[] usersDataSet = new UsersDataSet[1];

        connectAndPerform(connection -> {
            UsersDAO usersDAO = new UsersDAO(connection);

            try {
                usersDataSet[0] = usersDAO.getUserByName(name);
            } catch (SQLException e) {
                throw new DBException(e);
            }
        });

        if (usersDataSet[0] == null) {
            return null;
        }
        return new UserProfile(usersDataSet[0].getName(), usersDataSet[0].getPassword(), "");

    }

    @Override
    public int countUsers() throws DBException {
        Integer[] result = new Integer[1];

        connectAndPerform(connection -> {UsersDAO usersDAO = new UsersDAO(connection);

            try {
                result[0] = usersDAO.countUsers(connection);
            } catch (SQLException e) {
                throw new DBException(e);
            }});

        return result[0];
    }
}
