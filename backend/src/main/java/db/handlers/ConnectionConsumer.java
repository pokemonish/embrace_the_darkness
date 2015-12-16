package db.handlers;

import db.DBException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 16/12/15.
 */
public interface ConnectionConsumer {
    void handle(Connection connection) throws SQLException, DBException;
}
