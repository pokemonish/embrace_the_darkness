package db.dao;

import base.UserProfile;
import db.datasets.UsersDataSet;
import db.executor.TExecutor;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

public final class UsersDAO {

    private Connection con;
    private TExecutor executor;

    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE user_name = \"%s\"";
    private static final String INSERT_USER = "INSERT INTO users (user_name, password) VALUES (\"%s\", \"%s\")";
    private static final String COUNT_USERS = "SELECT count(*) FROM users";
    private static final String CREATE_TABLE =
                                            "CREATE TABLE IF NOT EXISTS " +
                                            Config.getInstance().getDbName() + ".users (" +
                                            "id BIGINT NOT NULL AUTO_INCREMENT," +
                                            "user_name VARCHAR(256) NOT NULL," +
                                            "PASSWORD VARCHAR(256) NOT NULL," +
                                            "PRIMARY KEY (id))";
    private static final String DROP_TABLE = "DROP TABLE " + Config.getInstance().getDbName() + ".users";


    public static UsersDAO makeUsersDAO(Connection con) {
        return new  UsersDAO(con);
    }

    private UsersDAO(Connection con) {
        this.con = con;
        this.executor = new TExecutor();
    }

    public void createTable() throws SQLException {

        executor.execUpdate(con, CREATE_TABLE);
    }

    public void dropTable() throws SQLException {

        executor.execUpdate(con, DROP_TABLE);
    }

    public UsersDataSet get(long id) throws SQLException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(2));
        });
    }

    public UsersDataSet getUserByName(String name) throws SQLException {

        String query = String.format(GET_USER_BY_NAME, name);

        System.out.append("Get user is ").append(query).append('\n');

        return executor.execQuery(con, query,
            result -> {
                result.next();

                return new UsersDataSet(result.getInt(1),
                        result.getString(2), result.getString(3));
            }
        );
    }

    public void addUser(UserProfile user, Connection connection) throws SQLException {

        String query = String.format(INSERT_USER, user.getLogin(), user.getPassword());
        System.out.append("Insert query ").append(query);

        executor.execUpdate(connection, query);
    }

    public int countUsers(Connection connection) throws SQLException {

        return executor.execQuery(connection, COUNT_USERS, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }


}
