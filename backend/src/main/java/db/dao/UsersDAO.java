package db.dao;

import base.UserProfile;
import db.datasets.UsersDataSet;
import db.executor.TExecutor;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {

    private Connection con;
    private TExecutor executor;

    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE user_name = \"%s\"";
    private static final String INSERT_USER = "INSERT INTO users (user_name, password) VALUES (\"%s\", \"%s\")";
    private static final String COUNT_USERS = "SELECT count(*) FROM users";
    private static final String DELETE_USER_BY_NAME = "DELETE FROM users WHERE user_name = \"%s\"";


    public UsersDAO(Connection con) {
        this.con = con;
        this.executor = new TExecutor();
    }


    @Nullable
    public UsersDataSet get(long id) throws SQLException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(2));
        });
    }

    @Nullable
    public UsersDataSet getUserByName(String name) throws SQLException {

        String query = String.format(GET_USER_BY_NAME, name);

        System.out.append("Get user is ").append(query).append('\n');

        return executor.execQuery(con, query,
            result -> {
                if (!result.next()) {
                    throw new SQLException("There is no such user.");
                }

                return new UsersDataSet(result.getInt(1),
                        result.getString(2), result.getString(3));
            }
        );
    }

    public void deleteUserByName(String name) throws SQLException {

        String query = String.format(DELETE_USER_BY_NAME, name);

        executor.execUpdate(con, query);

    }

    public void addUser(UserProfile user, Connection connection) throws SQLException {

        String query = String.format(INSERT_USER, user.getLogin(), user.getPassword());
        System.out.append("Insert query ").append(query).append('\n');

        executor.execUpdate(connection, query);
    }

    public int countUsers(Connection connection) throws SQLException {

        Integer number = executor.execQuery(connection, COUNT_USERS, result -> {

            if (!result.next()) {
                throw new SQLException("Can't count users");
            }
            return result.getInt(1);
        });
        if (number == null) throw new SQLException("Can't count users");

        return number;
    }
}
