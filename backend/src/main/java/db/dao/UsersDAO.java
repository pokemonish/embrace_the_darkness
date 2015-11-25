package db.dao;

import base.UserProfile;
import db.datasets.UsersDataSet;
import db.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {

    private Connection con;

    private static final String GET_USER_BY_NAME = "select * from users where user_name = \"%s\"";
    private static final String INSERT_USER = "insert into users (user_name, password) values (\"%s\", \"%s\")";
    private static final String COUNT_USERS = "select count(*) from users";

    public UsersDAO(Connection con) {
        this.con = con;
    }

    public UsersDataSet get(long id) throws SQLException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(2));
        });
    }

    public UsersDataSet getUserByName(String name) throws SQLException {
        TExecutor executor = new TExecutor();

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
        TExecutor executor = new TExecutor();

        String query = String.format(INSERT_USER, user.getLogin(), user.getPassword());
        System.out.append("Insert query ").append(query);

        executor.execUpdate(connection, query);
    }

    public int countUsers(Connection connection) throws SQLException {
        TExecutor executor = new TExecutor();

        return executor.execQuery(connection, COUNT_USERS, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }


}
