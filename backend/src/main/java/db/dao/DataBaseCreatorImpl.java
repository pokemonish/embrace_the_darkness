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
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS " +
            Config.getInstance().getDbName() + ".users (" +
            "id BIGINT NOT NULL AUTO_INCREMENT UNIQUE," +
            "user_name VARCHAR(256) NOT NULL," +
            "PASSWORD VARCHAR(256) NOT NULL," +
            "PRIMARY KEY (id)," +
            "UNIQUE INDEX email_UNIQUE (user_name ASC))";
    private static final String DROP_TABLE =
            "DROP TABLE " + Config.getInstance().getDbName() + '.';
    private static final String CREATE_TABLE_HIGHSCORES = "CREATE TABLE " +
            "IF NOT EXISTS " + Config.getInstance().getDbName() + ".highscores (" +
            "id BIGINT NOT NULL AUTO_INCREMENT UNIQUE," +
            "user_name VARCHAR(256) NOT NULL," +
            "highscore BIGINT NOT NULL)";

    private TExecutor executor;

    public DataBaseCreatorImpl(TExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void createDB() throws DBException {

        executor.execTransaction(connection -> {
            executor.execUpdate(connection, CREATE_DATABASE);
            executor.execUpdate(connection, CREATE_TABLE_USERS);
            executor.execUpdate(connection, CREATE_TABLE_HIGHSCORES);
        });
    }


    @Override
    public void dropDB() throws DBException {
        executor.execUpdate(DROP_DATABASE);
        System.out.println("Drop is " + DROP_DATABASE);
    }


    public void createTableUsers() throws DBException {
        executor.execUpdate(CREATE_TABLE_USERS);
    }

    public void createTableHighscores() throws DBException {
        executor.execUpdate(CREATE_TABLE_HIGHSCORES);
    }

    public void dropTableUsers() throws DBException {
        executor.execUpdate(DROP_TABLE + "users");
    }

    public void dropTableHighscores() throws DBException {
        executor.execUpdate(DROP_TABLE + "highscores");
    }
}
