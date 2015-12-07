package db;

import base.DBService;
import base.DataBaseDAO;
import base.UserProfile;
import db.dao.DataBaseDAOImpl;
import db.dao.UsersDAO;
import db.datasets.UsersDataSet;
import org.jetbrains.annotations.NotNull;
import resources.Config;

import java.sql.*;

/**
 * Created by fatman on 23/11/15.
 */

public class DBServiceImpl implements DBService {

    private String connectionUrl;
    private Connection connection;


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
            DataBaseDAO dataBaseDAO = DataBaseDAOImpl.makeDataBaseDAO(getConnection());
            connectionUrl =
                    "jdbc:" + Config.getInstance().getDbType()
                    + "://" + Config.getInstance().getHost() + ':' +
                    Config.getInstance().getDbPort() + '?' +
                    "user=" + Config.getInstance().getDbUser() + '&' +
                    "password=" + Config.getInstance().getDbPassword();
            dataBaseDAO.createDB();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("Bye bye!");
            if (Config.getInstance().isDoDeleteDB()) {
                try {
                    DataBaseDAO dataBaseDAO =
                            DataBaseDAOImpl.makeDataBaseDAO(getConnection());
                    dataBaseDAO.dropDB();
                } catch (DBException e) {
                    System.out.print("Database was not deleted");
                }
            }
        }));

        StringBuilder url = new StringBuilder();
        url.
            append("jdbc:").append(Config.getInstance().getDbType()).append("://").
            append(Config.getInstance().getHost()).append(':').
            append(Config.getInstance().getDbPort()).append('/')
            .append(Config.getInstance().getDbName()).append('?').
            append("user=").append(Config.getInstance().getDbUser()).append('&').
            append("password=").append(Config.getInstance().getDbPassword());

        connectionUrl = url.toString();

        System.out.append("URL: ").append(url).append("\n");
        connection = getConnection();
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

        UsersDAO usersDAO = UsersDAO.makeUsersDAO(connection);

        try {
            usersDAO.addUser(user, connection);
        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

    @Override
    public UserProfile getUserByName(String name) throws DBException {

        UsersDAO usersDAO = UsersDAO.makeUsersDAO(connection);

        UsersDataSet usersDataSet;

        try {
            usersDataSet = usersDAO.getUserByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return new UserProfile(usersDataSet.getName(), usersDataSet.getPassword(), "");

    }

    @Override
    public int countUsers() throws DBException {

        UsersDAO usersDAO = UsersDAO.makeUsersDAO(connection);

        try {
            return usersDAO.countUsers(connection);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

}
