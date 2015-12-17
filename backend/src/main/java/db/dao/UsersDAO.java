package db.dao;

import base.UserProfile;
import db.DBException;
import db.datasets.UsersDataSet;
import db.executor.TExecutor;
import org.jetbrains.annotations.Nullable;

public class UsersDAO {

    private TExecutor executor;

    private static final String GET_USER_BY_ID = "SELECT * FROM USERS WHERE ID =\"%s\"";
    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE user_name = \"%s\"";
    private static final String INSERT_USER = "INSERT INTO users (user_name, password) VALUES (\"%s\", \"%s\")";
    private static final String COUNT_USERS = "SELECT count(*) FROM users";
    private static final String DELETE_USER_BY_NAME = "DELETE FROM users WHERE user_name = \"%s\"";
    private static final String DELETE_ALL_USERS = "DELETE FROM users";

    public UsersDAO(TExecutor executor) {
        this.executor = executor;
    }


    @Nullable
    public UsersDataSet get(long id) throws DBException {
        return executor.execQuery(GET_USER_BY_ID + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(2));
        });
    }

    @Nullable
    public UserProfile getUserByName(String name) throws DBException {

        String query = String.format(GET_USER_BY_NAME, name);

        System.out.append("Get user is ").append(query).append('\n');

        return executor.execQuery(query,
            result -> {
                if (!result.next()) {
                    throw new DBException("There is no such user.");
                }

                return new UserProfile(result.getString(2), result.getString(3), "");
            }
        );
    }

    public void deleteUserByName(String name) throws DBException {

        String query = String.format(DELETE_USER_BY_NAME, name);

        executor.execUpdate(query);
    }

    public void deleteAllUsers() throws DBException {

        executor.execUpdate(DELETE_ALL_USERS);
    }

    public void addUser(UserProfile user) throws DBException {

        String query = String.format(INSERT_USER, user.getLogin(), user.getPassword());
        System.out.append("Insert query ").append(query).append('\n');

        executor.execUpdate(query);
    }

    public int countUsers() throws DBException {

        Integer number = executor.execQuery(COUNT_USERS, result -> {

            if (!result.next()) {
                throw new DBException("Can't count users");
            }
            return result.getInt(1);
        });
        if (number == null) throw new DBException("Can't count users");

        return number;
    }
}
