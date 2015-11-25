package db;

import base.DBService;
import base.UserProfile;
import db.dao.UsersDAO;
import db.datasets.UsersDataSet;
import org.jetbrains.annotations.Nullable;
import resources.Config;

import java.sql.*;

/**
 * Created by fatman on 23/11/15.
 */
public class DBServiceImpl implements DBService {

    private String connectionUrl;

    public DBServiceImpl(Config config) {
        try {
            DriverManager.registerDriver((Driver) Class.forName(config.getDbDriver()).newInstance());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e)  {
            System.out.append("Can't register driver ").append(config.getDbDriver());
            System.exit(1);
        }

        StringBuilder url = new StringBuilder();
        url.
                append("jdbc:").append(config.getDbType()).append("://").
                append(config.getHost()).append(':').
                append(config.getDbPort()).append('/').
                append(config.getDbName()).append('?').
                append("user=").append(config.getDbUser()).append('&').
                append("password=").append(config.getDbPassword());

        connectionUrl = url.toString();

        System.out.append("URL: ").append(url).append("\n");
    }

    @Override
    @Nullable
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUser(UserProfile user) throws SQLException {

        Connection connection = getConnection();
        if (connection == null) throw new SQLException();

        UsersDAO usersDAO = new UsersDAO(connection);

        usersDAO.addUser(user, connection);
    }

    @Override
    public UserProfile getUserByName(String name) throws SQLException {
        Connection connection = getConnection();
        if (connection == null) throw new SQLException();

        UsersDAO usersDAO = new UsersDAO(connection);

        UsersDataSet usersDataSet = usersDAO.getUserByName(name);

        return new UserProfile(usersDataSet.getName(), usersDataSet.getPassword(), "");
    }

    @Override
    public int countUsers() throws SQLException {
        Connection connection = getConnection();
        if (connection == null) throw new SQLException();

        UsersDAO usersDAO = new UsersDAO(connection);

        return usersDAO.countUsers(connection);
    }
}
