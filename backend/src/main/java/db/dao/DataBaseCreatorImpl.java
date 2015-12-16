package db.dao;

import base.DataBaseCreator;
import db.DBException;
import db.executor.TExecutor;
import resources.Config;

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

    private TExecutor executor;

    public DataBaseCreatorImpl(TExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void createDB() throws DBException {

        executor.execTransaction(connection -> {
            executor.execUpdate(CREATE_DATABASE);
            createTableUsers();
        });
    }


    @Override
    public void dropDB() throws DBException {
        executor.execUpdate(DROP_DATABASE);
        System.out.println("Drop is " + DROP_DATABASE);
    }


    public void createTableUsers() throws DBException {
        executor.execUpdate(CREATE_TABLE);
    }

    public void dropTableUsers() throws DBException {

        executor.execUpdate(DROP_TABLE);
    }
}
