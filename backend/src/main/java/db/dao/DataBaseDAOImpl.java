package db.dao;

import base.DataBaseDAO;
import db.DBException;
import db.executor.TExecutor;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseDAOImpl implements DataBaseDAO {
    private static final String CREATE_DATABASE =
            "CREATE SCHEMA IF NOT EXISTS " + Config.getInstance().getDbName();
    private static final String DROP_DATABASE =
            "DROP DATABASE " + Config.getInstance().getDbName();


    private Connection connection;

    public DataBaseDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createDB() throws DBException {
        UsersDAO usersDAO = makeUsersDAO();

        try {
            connection.setAutoCommit(false);
            TExecutor executor = makeTExecutor();
            executor.execUpdate(connection, CREATE_DATABASE);
            usersDAO.createTable();
            connection.commit();
        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }


    @Override
    public void dropDB() throws DBException {
        TExecutor executor = makeTExecutor();
        try {
            executor.execUpdate(connection, DROP_DATABASE);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public TExecutor makeTExecutor() {
        return new TExecutor();
    }

    public UsersDAO makeUsersDAO() {
        return new UsersDAO(connection);
    }
}
