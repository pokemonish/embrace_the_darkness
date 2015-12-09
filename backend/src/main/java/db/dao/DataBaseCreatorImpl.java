package db.dao;

import base.DataBaseCreator;
import db.DBException;
import db.executor.TExecutor;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseCreatorImpl implements DataBaseCreator {
    private static final String CREATE_DATABASE =
            "CREATE SCHEMA IF NOT EXISTS " + Config.getInstance().getDbName();
    private static final String DROP_DATABASE =
            "DROP DATABASE " + Config.getInstance().getDbName();
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    Config.getInstance().getDbName() + ".users (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT UNIQUE," +
                    "user_name VARCHAR(256) NOT NULL," +
                    "PASSWORD VARCHAR(256) NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE INDEX email_UNIQUE (user_name ASC))";
    private static final String DROP_TABLE =
            "DROP TABLE " + Config.getInstance().getDbName() + ".users";

    private Connection connection;
    private TExecutor executor;

    public DataBaseCreatorImpl(Connection connection) {
        this.connection = connection;
        this.executor = new TExecutor();
    }

    public DataBaseCreatorImpl(Connection connection, TExecutor executor) {
        this.connection = connection;
        this.executor = executor;
    }

    @Override
    public void createDB() throws DBException {

        try {
            connection.setAutoCommit(false);
            executor.execUpdate(connection, CREATE_DATABASE);
            createTableUsers();
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
        try {
            executor.execUpdate(connection, DROP_DATABASE);
            System.out.println("Drop is " + DROP_DATABASE);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    public void createTableUsers() throws SQLException {
        executor.execUpdate(connection, CREATE_TABLE);
    }

    public void dropTableUsers() throws SQLException {

        executor.execUpdate(connection, DROP_TABLE);
    }
}
